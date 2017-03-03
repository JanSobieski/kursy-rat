package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.wd.web.util.MessageUtils;
import org.wd.web.util.MultiLineMessageBox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.ui.interf.ChoiceDialogInt;
import pl.wd.kursy.web.ui.util.BaseCtrl;
import pl.wd.kursy.web.ui.util.WebUtil;

public class CourseChoiceCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = 7489214566126516255L;
	final static String CHOICE_DIALOG_INT = "choiceDialogInt";

	private static final Logger logger = Logger.getLogger(CourseChoiceCtrl.class);

	protected Window courseChoiceWindow; // autowired
	protected Combobox cmbCourse; // aurowired
	
	protected ChoiceDialogInt _choiceDialogInt;

	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners
			
			@SuppressWarnings("unchecked")
			Map<String, Object> args = (Map<String, Object>) Executions.getCurrent().getArg();

			if (args.containsKey(CHOICE_DIALOG_INT)) {
				_choiceDialogInt = (ChoiceDialogInt) args.get(CHOICE_DIALOG_INT);
			}			
			
			setupComponents();
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

	public void doShowDialog() throws InterruptedException {

		courseChoiceWindow.doModal(); // open the dialog in modal mode
	}

	private void closeWindow() {
		courseChoiceWindow.onClose();
	}
	
	private void setupComponents() throws Exception {
		WebUtil.addCbItemRenderer(cmbCourse);
	}
	
	private void initData() throws Exception {
		List<Course> coursesDb = getUserWorkspace().getDataServiceProvider().getCourses();
		List<Course> courses = new ArrayList<Course>();
		if ( coursesDb.size() > 1 ) {
			Course c = new Course();
			courses.add(c);
		}
		
		courses.addAll(coursesDb);

		ListModel<Course> courseModel = new ListModelArray<Course>(courses);
		cmbCourse.setModel(courseModel);

		if ( coursesDb.size() == 1 ) {
			WebUtil.selectCombo(cmbCourse, coursesDb.get(0));
		}
	}
	

	public void onClick$btnOk(Event event) throws InterruptedException {
		int courseId = 0;
		Course course = (Course) WebUtil.getCmbValue(cmbCourse);
		if (course != null) {
			courseId = course.getId();
		}
		if ( courseId == 0 ) {
			MessageUtils.showErrorMessage("Prosze wybrać kurs");
			return;
		}
		getUserWorkspace().setCourseId(courseId);
		try {
			getUserWorkspace().getIndexCtrl().setCourseName(courseId);
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}
		
		closeWindow();
		
		if ( _choiceDialogInt != null ) {
			_choiceDialogInt.onOkClose();
		}
	}
	
	public static void showDialog(ChoiceDialogInt choiceDialogInt) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(CHOICE_DIALOG_INT, choiceDialogInt);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/secure/admin/courseChoicePanel.zul", null, map);
		} catch (final Exception e) {
			logger.error("onOpenWindow:: error opening window / " + e.getMessage());

			// Show a error box
			String msg = e.getMessage();
			String title = Labels.getLabel("messag.Error");

			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "ERROR", true);
		}
	}
	
	public static boolean checkCourse( UserWorkspace userWorkspace, String tabId, ChoiceDialogInt choiceDialogInt ) throws Exception {
	    if ( userWorkspace.getCourseId() == 0 ) {
	    	if ( userWorkspace.getDataServiceProvider().getCourses().size() == 0 ) {
	    		WebUtil.closeTab(tabId);
				MessageUtils.showErrorMessage("Brak kursu do wyboru. Proszę wprowadzić kurs.");

		    	return false;
	    	}
	    	showDialog(choiceDialogInt);
	    	return false;
	    }
		
		return true;
	}
	
}
