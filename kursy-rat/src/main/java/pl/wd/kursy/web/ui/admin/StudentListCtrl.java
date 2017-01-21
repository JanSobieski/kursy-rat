package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.wd.web.util.MultiLineMessageBox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.User;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.data.filter.StudentFilter;
import pl.wd.kursy.web.ui.admin.model.StudentListViewModel;
import pl.wd.kursy.web.ui.admin.model.UserListViewModel;
import pl.wd.kursy.web.ui.admin.renderer.StudentListModelItemRenderer;
import pl.wd.kursy.web.ui.util.BaseListCtrl;



/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /secure/admin/studentList.zul
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 */
public class StudentListCtrl extends BaseListCtrl<Student> implements Serializable {
	private static final long serialVersionUID = 5409033372789296247L;

	private static final Logger logger = Logger.getLogger(StudentListCtrl.class);
	
	final static String LIST_BOX_STUDENTS = "listBoxStudent";

	
	private StudentListViewModel<Student> _model;

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window studentListWindow; // autowired
	protected Listbox listBoxStudents; // aurowired

	// filter components
	
	protected Textbox tbStudentID; // aurowired
	protected Textbox tbFirstName; // aurowired
	protected Textbox tbLastName; // aurowired
	
	protected Button btnNew; // autowired


	// listbox listBoxStudents
	protected Listheader listheader_StudentList_FirstName; // autowired
	protected Listheader listheader_StudentList_LastName; // autowired

	/**
	 * default constructor.<br>
	 */
	public StudentListCtrl() {
		super();
	}

	//public void onCreate$userListWindow(Event event) throws Exception {
	
	public void doAfterCompose(Window comp) {
		try {
	    super.doAfterCompose(comp); //wire variables and event listners
	    
	    if ( getUserWorkspace().getCourseId() == 0 ) {
	    	CourseChoiceCtrl.showDialog();
	    	studentListWindow.onClose();
	    }

			/* set comps visible dependent of the users rights */
			_model = new StudentListViewModel<Student>(getUserWorkspace());

//			listheader_UserList_usrLoginname.setSortAscending(new UserWebComparator(true, UserWebComparator.TYPE_LOGIN));
//			listheader_UserList_usrLoginname.setSortDescending(new UserWebComparator(false, UserWebComparator.TYPE_LOGIN));
//			listheader_UserList_usrLoginname.setSortDirection("ascending");

			doCheckRights();
			_model.showData(listBoxStudents, null);
			
			listBoxStudents.setItemRenderer(new StudentListModelItemRenderer());
		} catch (Exception e) {
			logger.error(e);
			Messagebox.show(e.toString());
		}
	}

	/**
	 * SetVisible for components by checking if there's a right for it.
	 */
	private void doCheckRights() {

		final UserWorkspace workspace = getUserWorkspace();
	}

	/**
	 * Call the User dialog with the selected entry. <br>
	 * <br>
	 * This method is forwarded from the listboxes item renderer. <br>
	 * use: 	@Secured({ "UserList_listBoxUser.onDoubleClick" }) to check rights
	 * @param event
	 * @throws Exception
	 */
	public void onStudentListItemDoubleClicked(Event event) throws Exception {
		// get the selected object
		Listitem item = listBoxStudents.getSelectedItem();

		if (item != null) {
			Student student = (Student) item.getAttribute("data");
			showDetailView(student);
		}
	}

	/**
	 * when the "new" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnNew( Event event ) throws Exception {
		Student student = new Student();
		student.setCourseId(getUserWorkspace().getCourseId());
		showDetailView(student);
	}
	

	/**
	 * Opens the detail view. <br>
	 * Overhanded some params in a map if needed. <br>
	 * 
	 * @param anUser
	 * @throws Exception
	 */
	private void showDetailView(Student student) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("student", student);
		map.put(LIST_BOX_STUDENTS, listBoxStudents);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/secure/admin/studentDialog.zul", null, map);
		} catch (final Exception e) {
			logger.error("onOpenWindow:: error opening window / " + e.getMessage());

			// Show a error box
			String msg = e.getMessage();
			String title = Labels.getLabel("messag.Error");

			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "ERROR", true);
		}
	}

	/**
	 * when the "refresh" button is clicked. <br>
	 * <br>
	 * Refreshes the view by calling the onCreate event manually.
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnRefresh(Event event) throws InterruptedException {

		Events.postEvent("onCreate", studentListWindow, event);
		studentListWindow.invalidate();
	}

	/*
	 * Filter the user list with 'like Loginname'
	 */
	public void onClick$button_UserList_SearchLoginname(Event event) throws Exception {

		// if not empty
//		if (!tbUserLogin.getValue().isEmpty()) {
//			_model.showData(listBoxUsers, null);
//		}
	}
	
	private void filterData() {
		StudentFilter filter = new StudentFilter();
		filter.setSid(tbStudentID.getValue());
		filter.setFirstName(tbFirstName.getValue() );
		filter.setLastName(tbLastName.getValue() );

		_model.showData(listBoxStudents, filter);
	}
	
	public void onChange$tbUserID(Event event) throws Exception {
		filterData();
	}

	public void onChange$tbUserLogin(Event event) throws Exception {
		filterData();
	}

	public void onChange$tbPersonName(Event event) throws Exception {
		filterData();
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public StudentListViewModel getModel() {
		return _model;
	}
	
}
