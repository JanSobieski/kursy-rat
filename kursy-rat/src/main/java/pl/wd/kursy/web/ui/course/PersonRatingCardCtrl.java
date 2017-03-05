package pl.wd.kursy.web.ui.course;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.Exercise;
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

			gridSkillsExercises.setRowRenderer(new SkillsExercisesItemRenderer());
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
		
	}

	public void onClick$btnAddExercise( Event event ) {
		Listitem selItem = listBoxExercises.getSelectedItem();
		if ( selItem == null ) {
			return;
		}
		Exercise exercise  = (Exercise)selItem.getValue();
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
		//List<Skill> _toAdd = new ArrayList<>();
		exercise.getSkills().stream().forEach((skill) -> {
			if ( !_existingSkills.contains(skill) ) {
				//_toAdd.add(skill);
				_existingSkills.add(skill);
				_model.add(skill);
			}
		});
		
		
	}
	
	public void onClick$btnAddSkill( Event event ) {
		Set<Listitem> selItems = listBoxSkills.getSelectedItems();
		selItems.stream().forEach((item) -> {
			Skill skill = (Skill)item.getValue();
			if ( !_existingSkills.contains(skill) ) {
				_existingSkills.add(skill);
				_model.add(skill);
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
	

}
