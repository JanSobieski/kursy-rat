package pl.wd.kursy.web.ui.course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
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
import pl.wd.kursy.data.Skill;
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

	private RateCardItem 		_rateCardItem;
	private List<RateCardItem> 		_rateCardItems = new ArrayList<>();
	private SkillExerciseGridModel<Skill> _model;
	private ExerciseListModel _exerciseListModel;
	private SkillListViewModel _skillModel;
	private Column _firstColumn = null; 
	private int colCount = 1;
	private HashSet<Skill> _existingSkills = new HashSet<>();
	                                        

	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners
			
			_model = new SkillExerciseGridModel<Skill>(_userWorkspace);
			_exerciseListModel = new ExerciseListModel(_userWorkspace);
			_skillModel = new SkillListViewModel(_userWorkspace);

			gridSkillsExercises.setRowRenderer(new SkillsExercisesItemRenderer(this));
			listBoxExercises.setItemRenderer(new ExerciseListItemRenderer());
			listBoxSkills.setItemRenderer(new SkillListItemSimpleRenderer());

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
	
	private void initData() {
		_skillModel.setMultiple(true);
		_skillModel.showData(listBoxSkills, null );
		
		gridSkillsExercises.setModel(_model);
		listBoxExercises.setModel(_exerciseListModel);
		
		sampleData();
		
	}

	public void onClick$btnAddExercise( Event event ) {
		if ( _rateCardItem != null ) {
			return;
		}
		Listitem selItem = listBoxExercises.getSelectedItem();
		if ( selItem == null ) {
			return;
		}
		Exercise exercise  = (Exercise)selItem.getValue();
		_rateCardItem = new RateCardItem();
		_rateCardItem.setExerciseId(exercise.getId());
		//_rateCardItem.setStudentId(studentId);
		
		Column column = new Column();
		String colId = "col" + colCount;
		colCount++;
		column.setId(colId);
		column.setLabel(exercise.getName());
		
		if ( _firstColumn != null ) {
			gridSkillsExercises.getColumns().insertBefore(column, _firstColumn);
		}
		else {
			gridSkillsExercises.getColumns().appendChild(column);
		}
		exercise.getSkills().stream().forEach((skill) -> {
			_rateCardItem.getSkills().add(skill);
			if ( !_existingSkills.contains(skill) ) {
				_existingSkills.add(skill);
				_model.add(skill);
			}
			else {
				int ind = _model.lastIndexOf(skill);
				if ( ind > -1 ) {
					_model.fireEventExt(ListDataEvent.CONTENTS_CHANGED, ind, ind);
				}
			}
		});
		
		
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
				_rateCardItem.getSkills().add(skill);
			}
		});

		
//		Listitem selItem = listBoxExercises.getSelectedItem();
//		if ( selItem == null ) {
//			return;
//		}
//		Exercise exercise  = (Exercise)selItem.getValue();
//		Column column = new Column();
//		if ( _firstColumn == null ) {
//			_firstColumn = column;
//		}
//		String colId = "col" + colCount;
//		colCount++;
//		column.setId(colId);
//		column.setLabel(exercise.getName());
//		gridSkillsExercises.getColumns().appendChild(column);
		
	}

	public RateCardItem getRateCardItem() {
		return _rateCardItem;
	}
	
	private void closeWindow() {
		String tabId = WebUtil.getTabId(personRatingWindow);
		WebUtil.closeTab(tabId);
	}

	public void onClick$btnEdit( Event event ) {
		doEdit();
	}

	public void onClick$btnSave( Event event ) {
		try {
			if ( doSave() ) {
				closeWindow();
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}
	
	public List<RateCardItem> getData(boolean checkEmpty) {
		List<RateCardItem> data = new ArrayList<>();
		RateCardItem rateCardItem = new RateCardItem();
		
		
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
					}
					
				}
			}
		}
		

		return data;
	}
	
	public boolean doSave() {
		List<RateCardItem> data = getData(true);
		if ( data == null ) {
			return false;
		}
		try {
			//getUserWorkspace().getDataServiceProvider().saveStudentGroups(data, getUserWorkspace().getCourseId());
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

			Column column = new Column();
			if ( _firstColumn == null ) {
				_firstColumn = column;
			}
			String colId = "col" + colCount;
			colCount++;
			column.setId(colId);
			column.setLabel(exercise.getName());
			gridSkillsExercises.getColumns().appendChild(column);
			exercise.getSkills().stream().forEach((skill) -> {
				if ( !_existingSkills.contains(skill) ) {
					_existingSkills.add(skill);
					_model.add(skill);
				}
			});
			
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}
		
	}
	

}
