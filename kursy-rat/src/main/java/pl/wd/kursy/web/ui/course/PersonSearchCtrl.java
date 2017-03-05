package pl.wd.kursy.web.ui.course;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.wd.web.util.MultiLineMessageBox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.StudentGroup;
import pl.wd.kursy.data.User;
import pl.wd.kursy.data.criteria.StudentCriteria;
import pl.wd.kursy.data.wrapper.StudentWrapper;
import pl.wd.kursy.web.ui.admin.CourseChoiceCtrl;
import pl.wd.kursy.web.ui.admin.StudentGroupListCtrl;
import pl.wd.kursy.web.ui.interf.ChoiceDialogInt;
import pl.wd.kursy.web.ui.model.PersonListViewModel;
import pl.wd.kursy.web.ui.renderer.StudentListItemRenderer;
import pl.wd.kursy.web.ui.util.BaseCtrl;
import pl.wd.kursy.web.ui.util.WebUtil;

public class PersonSearchCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = -2720047760223005366L;

	private static final Logger logger = Logger.getLogger(StudentGroupListCtrl.class);

	protected Window 			personSearchWindow; // autowired
	protected Combobox 		cmbGroup; // aurowired
	protected Listbox 			listBoxStudents; // aurowired
	
	
	private PersonListViewModel<StudentWrapper> _model;



	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners
			
			_model = new PersonListViewModel<StudentWrapper>(_userWorkspace);

			listBoxStudents.setItemRenderer(new StudentListItemRenderer());

		    ChoiceDialogInt choiceDialogInt = new ChoiceDialogInt() {
				@Override
				public void onOkClose() {
					try {
						setupComponents();
						initData();
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
		    if ( CourseChoiceCtrl.checkCourse( getUserWorkspace(), WebUtil.getTabId(personSearchWindow),  choiceDialogInt ) ) {
				setupComponents();
				initData();
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
	
	private void setupComponents() throws Exception {
		WebUtil.addCbItemRenderer(cmbGroup);
	}
	
	private void initData() throws Exception {
		List<StudentGroup> groups = getUserWorkspace().getDataServiceProvider().getStudentGroups(getUserWorkspace().getCourseId());
		StudentGroup emptyGroup = new StudentGroup();
		groups.add(0, emptyGroup);
		
		ListModel<StudentGroup> groupsModel = new ListModelArray<StudentGroup>(groups);
		cmbGroup.setModel(groupsModel);
		
		 filterData();
	}
	
	private void filterData() {
		try {
			StudentCriteria filter = getFilter();
			getUserWorkspace().getDataServiceProvider().closeDbSession();
			_model.loadData(filter);
			listBoxStudents.setModel(_model);
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			try {
	      getUserWorkspace().getDataServiceProvider().closeDbSession();
			} catch (Exception e2) {
				logger.error("Error", e2);
			}
		}
	}
	
	private StudentCriteria getFilter() {
		StudentCriteria filter = new StudentCriteria();
		filter.setCourseId(getUserWorkspace().getCourseId());
		StudentGroup group = (StudentGroup) WebUtil.getCmbValue(cmbGroup);
		if ( group != null ) {
			filter.setGroupId(group.getId());
		}

		return filter;
	}
	
	public void onSelect$cmbGroup(Event evt) {
		filterData();
	}
	
	public void onStudentListItemDoubleClicked(Event event) throws Exception {
		// get the selected object
		Listitem item = listBoxStudents.getSelectedItem();

		if (item != null) {
			StudentWrapper student = (StudentWrapper) item.getValue();
			showRateCardView(student);
		}
	}
	
	private void showRateCardView(StudentWrapper student) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("student", student);
		map.put("listBoxStudents", listBoxStudents);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/secure/course/personRatingCard.zul", null, map);
		} catch (final Exception e) {
			logger.error("onOpenWindow:: error opening window / " + e.getMessage());

			// Show a error box
			String msg = e.getMessage();
			String title = Labels.getLabel("messag.Error");

			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "ERROR", true);
		}
	}
	
	
}
