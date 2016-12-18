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

import pl.wd.kursy.data.User;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.data.comp.UserWebComparator;
import pl.wd.kursy.web.data.filter.UserWebFilter;
import pl.wd.kursy.web.ui.admin.model.UserListViewModel;
import pl.wd.kursy.web.ui.admin.renderer.UserListModelItemRenderer;
import pl.wd.kursy.web.ui.util.BaseListCtrl;



/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /secure/admin/userList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 */
public class UserListCtrl extends BaseListCtrl<User> implements Serializable {

	private static final long serialVersionUID = 2038742641853727975L;
	private static final Logger logger = Logger.getLogger(UserListCtrl.class);
	
	private UserListViewModel<User> _model;

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window userListWindow; // autowired
	protected Listbox listBoxUsers; // aurowired

	// filter components
	
	protected Textbox tbUserID; // aurowired
	protected Textbox tbUserLogin; // aurowired
	//protected Textbox tbPersonName; // autowired
	
	protected Button btnNew; // autowired


	// listbox userList
	protected Listheader listheader_UserList_usrLoginname; // autowired
	protected Listheader listheader_UserList_usrPerson; // autowired

	/**
	 * default constructor.<br>
	 */
	public UserListCtrl() {
		super();
	}

	//public void onCreate$userListWindow(Event event) throws Exception {
	
	public void doAfterCompose(Window comp) {
		try {
	    super.doAfterCompose(comp); //wire variables and event listners

			/* set comps visible dependent of the users rights */
			_model = new UserListViewModel<User>(getUserWorkspace());

			listheader_UserList_usrLoginname.setSortAscending(new UserWebComparator(true, UserWebComparator.TYPE_LOGIN));
			listheader_UserList_usrLoginname.setSortDescending(new UserWebComparator(false, UserWebComparator.TYPE_LOGIN));
			listheader_UserList_usrLoginname.setSortDirection("ascending");
//			listheader_UserList_usrPerson.setSortAscending(new UserWebComparator(true, UserWebComparator.TYPE_PERSON));
//			listheader_UserList_usrPerson.setSortDescending(new UserWebComparator(false, UserWebComparator.TYPE_PERSON));

			doCheckRights();
			_model.showData(listBoxUsers, null);
			
			listBoxUsers.setItemRenderer(new UserListModelItemRenderer<User>());
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

		//userListWindow.setVisible(workspace.isAllowed("userListWindow"));
	}

	/**
	 * Call the User dialog with the selected entry. <br>
	 * <br>
	 * This method is forwarded from the listboxes item renderer. <br>
	 * use: 	@Secured({ "UserList_listBoxUser.onDoubleClick" }) to check rights
	 * @param event
	 * @throws Exception
	 */
	public void onUserListItemDoubleClicked(Event event) throws Exception {
		// get the selected object
		Listitem item = listBoxUsers.getSelectedItem();

		if (item != null) {
			User anUser = (User) item.getAttribute("data");
			showDetailView(anUser);
		}
	}

	/**
	 * when the "new" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnNew( Event event ) throws Exception {
		User user = new User();
		//user.setActive(true);
		showDetailView(user);
	}
	

	/**
	 * Opens the detail view. <br>
	 * Overhanded some params in a map if needed. <br>
	 * 
	 * @param anUser
	 * @throws Exception
	 */
	private void showDetailView(User anUser) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user", anUser);
		map.put("listBoxUsers", listBoxUsers);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/secure/admin/userDialog.zul", null, map);
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

		Events.postEvent("onCreate", userListWindow, event);
		userListWindow.invalidate();
	}

	/*
	 * Filter the user list with 'like Loginname'
	 */
	public void onClick$button_UserList_SearchLoginname(Event event) throws Exception {

		// if not empty
		if (!tbUserLogin.getValue().isEmpty()) {

			_model.showData(listBoxUsers, null);
		}
	}
	
	private void filterData() {
		UserWebFilter filter = new UserWebFilter();
		filter.setId(tbUserID.getValue());
		filter.setName(tbUserLogin.getValue());
		//filter.setPersonName(tbPersonName.getValue());
		_model.showData(listBoxUsers, filter);
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

	public UserListViewModel getModel() {
		return _model;
	}
	
}
