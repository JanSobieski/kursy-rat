package pl.wd.kursy.web.ui.course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.ListDataEvent;

import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.RateCardItem;
import pl.wd.kursy.data.RateCardItemStatus;
import pl.wd.kursy.data.RateCardSkillItem;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.data.comp.SkillComparator;
import pl.wd.kursy.data.constants.SystemConstants;
import pl.wd.kursy.data.wrapper.StudentWrapper;
import pl.wd.kursy.web.ui.admin.CourseChoiceCtrl;
import pl.wd.kursy.web.ui.admin.StudentGroupListCtrl;
import pl.wd.kursy.web.ui.admin.model.SkillListViewModel;
import pl.wd.kursy.web.ui.admin.renderer.SkillListItemSimpleRenderer;
import pl.wd.kursy.web.ui.interf.ChoiceDialogInt;
import pl.wd.kursy.web.ui.model.ExerciseListModel;
import pl.wd.kursy.web.ui.model.SkillExerciseGridModel;
import pl.wd.kursy.web.ui.renderer.ExerciseListItemRenderer;
import pl.wd.kursy.web.ui.renderer.SkillsExercisesItemRenderer;
import pl.wd.kursy.web.ui.util.BaseCtrl;
import pl.wd.kursy.web.ui.util.WebUtil;

public class PersonRatingCardCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = 8543251546931927810L;

	private static final Logger logger = Logger.getLogger(StudentGroupListCtrl.class);

	protected Window 			personRatingWindow; // autowired
	protected Grid 				gridSkillsExercises; // autowired
	protected Listbox 			listBoxExercises; // aurowired
	protected Listbox 			listBoxSkills; // aurowired
	
	protected Button btnSave; // aurowired
	protected Button btnEdit; // aurowired
	
	private StudentWrapper _studentW;
	private RateCardItem 		_rateCardItem;
	private List<RateCardItem> 		_rateCardItems = new ArrayList<>();
	private SkillExerciseGridModel<Skill> _model;
	private ExerciseListModel _exerciseListModel;
	private SkillListViewModel _skillModel;
	private Column _firstColumn = null; 
	private Auxheader _firstHeadColumn = null;
	private Auxheader _firstHeadColumn2r = null;
	private boolean _rko = false;
	
	private int colCount = 1;
	private HashSet<Skill> _existingSkills = new HashSet<>();
	                                        

	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners
			
			_model = new SkillExerciseGridModel<Skill>(_userWorkspace);
			_skillModel = new SkillListViewModel(_userWorkspace);

			gridSkillsExercises.setRowRenderer(new SkillsExercisesItemRenderer(this));
			listBoxExercises.setItemRenderer(new ExerciseListItemRenderer());
			if ( listBoxSkills!=null ) {
				listBoxSkills.setItemRenderer(new SkillListItemSimpleRenderer());
			}
			
		    @SuppressWarnings("unchecked")
			Map<String, Object> args = (Map<String, Object>)Executions.getCurrent().getArg();

			if ( args.containsKey("student") ) {
				_studentW =  (StudentWrapper)args.get("student");
			}

			if ( args.containsKey("rko") ) {
				Boolean rko = (Boolean)args.get("rko");
				_rko = rko.booleanValue();
			}
			_exerciseListModel = new ExerciseListModel(_userWorkspace, _rko);

		    ChoiceDialogInt choiceDialogInt = new ChoiceDialogInt() {
				@Override
				public void onOkClose() {
					try {
//						setupComponents();
						initData();
						doShowDialog();
					} catch (final Exception e) {
						logger.error("Error", e);
						Messagebox.show(e.toString());
					} finally {
						try {
							getUserWorkspace().getDataServiceProvider().closeDbSession();
						} catch (Exception e2) {
							logger.error("Error", e2);
						}
					}
				}
		    };
		    if ( CourseChoiceCtrl.checkCourse( getUserWorkspace(), WebUtil.getTabId(personRatingWindow),  choiceDialogInt ) ) {
//				setupComponents();
				initData();
				doShowDialog();
		    }
			
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		} finally {
			try {
				getUserWorkspace().getDataServiceProvider().closeDbSession();
			} catch (Exception e2) {
				logger.error("Error", e2);
			}
		}
	}
	
	public void doShowDialog( ) throws InterruptedException {

//		if ( user.getId() == 0 ) {
//			doEdit();
//		} else {
//			// btnCtrl.setInitEdit();
//			doReadOnly();
//		}

		try {
			// fill the components with the data
			//doWriteBeanToComponents(user);

			personRatingWindow.doModal(); // open the dialog in modal mode
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}
	}
	
	private void initData() throws Exception {
		//String title = Labels.getLabel("personEditWindow.title") + " - " + pw.getPerson().get_first_last_name()
		String title =  personRatingWindow.getTitle();
		title += " - " +  _studentW.getStudent().getLastFirstName();
		personRatingWindow.setTitle(title);

		List<Skill> skills = new ArrayList<>(); 
		_model.initData(_rko);
		
		List<RateCardItem> data = getUserWorkspace().getDataServiceProvider().getRateCard(_studentW.getStudent().getId());
		data.stream().forEach((item) -> {
			Exercise exercise = _model.getId2ex().get(item.getExerciseId());
			if ( exercise != null ) {
				addExcerise(exercise, item);
				item.getSkills().stream().forEach((rcItem) -> {
					Skill skill = _model.getId2skill().get(rcItem.getSkillId() );
					if ( !_existingSkills.contains(skill) ) {
						_existingSkills.add(skill);
						skills.add(skill);
					}
				});
			}
		});
		Collections.sort(skills, new SkillComparator(true, SkillComparator.TYPE_ORDER));
		skills.stream().forEach((skill) ->  _model.add(skill) );

		if ( listBoxSkills!=null ) {
			_skillModel.setMultiple(true);
			_skillModel.clear();
			_skillModel.addAll(_model.getSkills());
			listBoxSkills.setModel(_skillModel);
		}
		
		gridSkillsExercises.setModel(_model);
		listBoxExercises.setModel(_exerciseListModel);
		
		//sampleData();
	}

	public void onExerciseListItemDoubleClicked(Event event) throws Exception {
		onClick$btnAddExercise( event );
	}

	public void onClick$btnAddExercise( Event event ) {
		if ( _rateCardItem != null ) {
			return;
		}
		List<Skill> skills = new ArrayList<>(); 
		
		Listitem selItem = listBoxExercises.getSelectedItem();
		if ( selItem == null ) {
			return;
		}
		Exercise exercise  = (Exercise)selItem.getValue();
		_rateCardItem = new RateCardItem();
		_rateCardItem.setDate_created(new Date());
		_rateCardItem.setExerciseId(exercise.getId());
		
		Column column = new Column();
		String colId = "col" + colCount;
		colCount++;
		column.setId(colId);
		
		column.setLabel(SystemConstants.SHOW_DATE_FORMAT_MMddHHmm.format(new Date()));
		Label lb = new Label(exercise.getName());
		Auxheader ah = new Auxheader();
		ah.getChildren().add(lb);
		lb = new Label("");
		Auxheader ah2 = new Auxheader();
		ah2.getChildren().add(lb);
		Iterator<Component> itr = gridSkillsExercises.getHeads().iterator();
		if ( _firstColumn != null ) {
			gridSkillsExercises.getColumns().insertBefore(column, _firstColumn);
			itr.next().insertBefore(ah, _firstHeadColumn);
			if ( _rko && itr.hasNext() ) {
				itr.next().insertBefore(ah2, _firstHeadColumn2r);
			}
		}
		else {
			gridSkillsExercises.getColumns().appendChild(column);
			itr.next().appendChild(ah);
			if ( _rko && itr.hasNext() ) {
				itr.next().appendChild(ah2);
			}
		}
		HashSet<Skill> checkedSkills = new HashSet<>();
		exercise.getSkills().stream().forEach((skill) -> {
			_rateCardItem.addSkill(skill);
			checkedSkills.add(skill);
			if ( !_existingSkills.contains(skill) ) {
				_existingSkills.add(skill);
				skills.add(skill);
			}
			else {
				int ind = _model.lastIndexOf(skill);
				if ( ind > -1 ) {
					_model.fireEventExt(ListDataEvent.CONTENTS_CHANGED, ind, ind);
				}
			}
		});
		Collections.sort(skills, new SkillComparator(true, SkillComparator.TYPE_ORDER));
		skills.stream().forEach((skill) ->  _model.add(skill) );
		
		for (Skill skill : _model) {
			if ( !checkedSkills.contains(skill) ) {
				int ind = _model.lastIndexOf(skill);
				if ( ind > -1 ) {
					_model.fireEventExt(ListDataEvent.CONTENTS_CHANGED, ind, ind);
				}
			}
		}
	}
	
	public void onClick$btnAddSkill( Event event ) {
		if ( _rateCardItem == null ) {
			return;
		}
		Set<Listitem> selItems = listBoxSkills.getSelectedItems();
		selItems.stream().forEach((item) -> {
			Skill skill = (Skill)item.getValue();
			if ( !_existingSkills.contains(skill) ) {
				_existingSkills.add(skill);
				_model.add(skill);
				_rateCardItem.addSkill(skill);
			}
		});

		
		
	}

	public RateCardItem getRateCardItem() {
		return _rateCardItem;
	}
	
	public List<RateCardItem> getRateCardItems() {
		return _rateCardItems;
	}

	private void closeWindow() {
		personRatingWindow.onClose();
	}

	public void onClick$btnEdit( Event event ) {
		doEdit();
	}
	
	public void onClick$btnAddRKO( Event event ) {
		List<Listitem> items = listBoxExercises.getItems();
		if ( items.size() == 1 ) {
			listBoxExercises.setSelectedIndex(0);
			onClick$btnAddExercise( event );
			return;
		}
	}

	public void onClick$btnClose( Event event ) {
		closeWindow();
	}
	
	public void onClick$btnSave( Event event ) {
		try {
			if ( doSave() ) {
				closeWindow();
			}
		} catch (Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}
	}
	
	public List<RateCardItem> getData(boolean checkEmpty) {
		List<RateCardItem> data = new ArrayList<>();
		if ( _rateCardItem == null ) {
			return data;
		}
		
		List<RateCardSkillItem> toAdd = new ArrayList<>();
		
		Rows rows = gridSkillsExercises.getRows();
		for (Component compRow : rows.getChildren() ) {
			if ( compRow instanceof Row ) {
				Row row = (Row)compRow;
				Skill skill = (Skill)row.getValue();
				
				Component comp = row.getChildren().get(1);
				if ( comp instanceof Combobox ) {
					Combobox cmb = (Combobox)comp;
					Comboitem item = cmb.getSelectedItem();
					if ( item == null ) {
						if ( checkEmpty ) {
							Messagebox.show("Status nie może być pusty");
							return null;
						}
					}
					else {
						RateCardItemStatus status = (RateCardItemStatus)item.getValue();
						RateCardSkillItem rcsi = new RateCardSkillItem();
						rcsi.setStatusId(status.getId());
						rcsi.setSkillId(skill.getId());
						toAdd.add(rcsi);
					}
				}
			}
		}
		_rateCardItem.getSkills().clear();
		_rateCardItem.getSkills().addAll(toAdd);
		data.add(_rateCardItem);

		return data;
	}
	
	public boolean doSave() {
		List<RateCardItem> data = getData(true);
		if ( data == null ) {
			return false;
		}
		try {
			getUserWorkspace().getDataServiceProvider().saveRateCard(data, _studentW.getStudent().getId() );
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		} finally {
			try {
				getUserWorkspace().getDataServiceProvider().closeDbSession();
			} catch (Exception e2) {
				logger.error("Error", e2);
			}
		}

		return true;
	}

	private void doEdit() {
//		if (_project.getProject().get_id() > 0) {
//			if (!lockRecord(Constants.TABLE_PROJECTS, _project.getProject().get_id())) {
//				return;
//			}
//			loadFromDB();
//		}
		btnSave.setDisabled(false);
		btnEdit.setDisabled(true);
	}

	private void sampleData() {
		try {
		List<Exercise> exercises = getUserWorkspace().getDataServiceProvider().getExercises();
		Exercise exercise  = exercises.get(0);
		
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}
	}
	
	private void addExcerise(Exercise exercise, RateCardItem rateCardItem) {
		String status = SystemConstants.STATUS_PASSED;
		String style = "";
		if ( _rko ) {
			for (RateCardSkillItem item : rateCardItem.getSkills() ) {
				if ( item.getStatusId() != RateCardItemStatus.K.getId() ) {
					status = SystemConstants.STATUS_NOT_PASSED;
					style = "color:red";
					break;
				}
			}
		}
		_rateCardItems.add(rateCardItem);
		Label lb = new Label(exercise.getName());
		Auxheader ah = new Auxheader();
		ah.getChildren().add(lb);

		Column column = new Column();
		if (_firstColumn == null) {
			_firstColumn = column;
			_firstHeadColumn = ah;
		}
		String colId = "col" + colCount;
		colCount++;
		column.setId(colId);
		column.setLabel(SystemConstants.SHOW_DATE_FORMAT_MMddHHmm.format(rateCardItem.getDate_created()));

		Iterator<Component> itr = gridSkillsExercises.getHeads().iterator();
		itr.next().appendChild(ah);
		
		if ( _rko && itr.hasNext() ) {
			ah = new Auxheader();
			if ( _firstHeadColumn2r == null ) {
				_firstHeadColumn2r = ah;
			}
			
			lb = new Label(status);
			lb.setStyle(style);
			
			ah.getChildren().add(lb);
			itr.next().appendChild(ah);
		}
		
		
		gridSkillsExercises.getColumns().appendChild(column);
	}

	public boolean isRko() {
		return _rko;
	}
}
