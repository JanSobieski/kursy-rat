package pl.wd.kursy.web.ui.index;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
//import org.jfree.data.time.Day;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wd.web.util.MessageUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Timer;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.security.UserAuthenticationFilter;
import pl.wd.kursy.web.ui.menu.MainMenuCtrl;
import pl.wd.kursy.web.ui.util.BaseCtrl;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/index.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 */
public class IndexCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = 8206571725402725705L;

	private final static Logger logger = Logger.getLogger(IndexCtrl.class);
	

	protected Menubar mainMenuBar; // autowired
	protected Label label_AppName; // autowired
	protected Intbox currentDesktopHeight; // autowired
	protected Intbox currentDesktopWidth; // autowired
	protected Checkbox CBtreeMenu; // autowired
	protected Tabs tabsIndexCenter; // autowired
	protected Label lbCourseName;
	 
	protected Timer timer;

	private final int centerAreaHeightOffset = 50;

	// Controllers
	private MainMenuCtrl mainMenuCtrl;

	public IndexCtrl() {
		super();
	}

	public void onCreate$outerIndexWindow(Event event) throws Exception {
	}
	
	public void doAfterCompose(Window comp) {
		try {
	    super.doAfterCompose(comp); //wire variables and event listners
			
			this.mainMenuBar.setVisible(false);
			comp.getWidth();
			
			Session ses = Sessions.getCurrent();
			if ( ses != null ) {
				String cl_id = (String)ses.getAttribute(UserAuthenticationFilter.CLIENT_ID);
				_userWorkspace.setClientId( Long.parseLong(cl_id) );
				String courseId = (String)ses.getAttribute(UserAuthenticationFilter.SPRING_SECURITY_FORM_COURSE_ID);
				int cId = Integer.parseInt(courseId);
				_userWorkspace.setCourseId( cId );
				_userWorkspace.loadUser();
				setCourseName( cId );
				
			}

			createMainTreeMenu();
		} catch (Exception e) {
			logger.error(e);
			Messagebox.show(e.toString());
		}
		finally {
			try {
				_userWorkspace.getDataServiceProvider().closeDbSession();
			} catch (Exception e2) {
				logger.error(e2);
				Messagebox.show(e2.toString());
			}
		}
	}
	
	public void setCourseName( int courseId ) throws Exception {
		Course course = _userWorkspace.getDataServiceProvider().getCourse(courseId);
		if ( course != null ) {
			lbCourseName.setValue(course.getName());
		}
		
	}

	/**
	 * Gets the current desktop height and width and <br>
	 * stores it in two hidden intboxes components. <br>
	 * We use these values for calculating the count of rows in the listboxes. <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClientInfo(ClientInfoEvent event) throws Exception {

		// logger.debug("Current desktop height :" + event.getDesktopHeight() +
		// "  - width  :" + event.getDesktopWidth());

		setCurrentDesktopHeight(event.getDesktopHeight() - this.centerAreaHeightOffset);
		setCurrentDesktopWidth(event.getDesktopWidth());
	}

	/**
	 * Returns the used ZK framework version and build number.<br>
	 * 
	 * @return
	 */
	private String doGetZkVersion() {

		final String version = Executions.getCurrent().getDesktop().getWebApp().getVersion();
		final String build = Executions.getCurrent().getDesktop().getWebApp().getBuild();
		return "ZK " + version + " EE" + " / build : " + build;
	}

	/**
	 * Returns the spring-security managed logged in user.<br>
	 */
	public String doGetLoggedInUser() {

		final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userName;
	}

	public void onClick$btnLogout() throws IOException, InterruptedException {
		_userWorkspace.doLogout();
	}
	
	/**
	 * When the 'close all tabs' button is clicked.<br>
	 * 1. Get a list of all open 'Tab'.<br>
	 * 2. Iterate through it and close the Tab if it's not the Dashboard.<br>
	 * 3. The Dashboard itself is modified after creating to not closable.<br>
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void onClick$btnCloseAllTabs() throws IOException, InterruptedException {

		List<Component> list = tabsIndexCenter.getChildren();

		try {
			while (!list.isEmpty()) {

				// get the sum of all Tab
				int i = list.size();

				// close all tabs, beginning with the last
				// because Dashboard is all times the first
				if (list.get(i - 1) instanceof Tab) {
					if ("tab_menu_Item_Home".equals(((Tab) list.get(i - 1)).getId())) {
						break;
					} else {
						((Tab) list.get(i - 1)).onClose();
					}
				}
			}
		} catch (Exception e) {
			MessageUtils.showErrorMessage(e.toString());
		}
	}

	/**
	 * Creates the MainMenu as TreeMenu as default. <br>
	 */
	private void createMainTreeMenu() {

		// get an instance of the borderlayout defined in the index.zul-file
		Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");

		// get an instance of the searched west layout area
		West west = bl.getWest();
		west.setFlex(true);
		// clear the WEST child comps
		west.getChildren().clear();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("indexController", this);

		// create the components from the src/main/resources/mainmenu.xml and
		// put it in the WEST layout area
		// Overhand this controller self in a map
		Executions.createComponents("/secure/mainTreeMenu.zul", west, map);

	}

	/**
	 * Shows the welcome page in the borderlayouts CENTER area.<br>
	 * 
	 * @throws InterruptedException
	 */
	public void showWelcomePage() throws InterruptedException {
		// get an instance of the borderlayout defined in the zul-file
		Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
		// get an instance of the searched CENTER layout area
		Center center = bl.getCenter();
		// clear the center child comps
		center.getChildren().clear();
		// call the zul-file and put it in the center layout area
		Executions.createComponents("/WEB-INF/pages/welcome.zul", center, null);
	}

	/**
	 * When the 'My Settings' toolbarButton is clicked.<br>
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void onClick$btnUserSettings() throws IOException, InterruptedException {
		Window win = null;
		Window parentWin = (Window) Path.getComponent("/outerIndexWindow");

		try {
			win = (Window) Executions.createComponents("/WEB-INF/pages/sec_user/mySettings.zul", parentWin, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			MessageUtils.showErrorMessage(e.getLocalizedMessage());
			win.detach();
		}
	}

	/**
	 * When the 'My Settings' toolbarButton is clicked.<br>
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void onClick$btnTestMain() throws InterruptedException {
		Window win = null;
		Window parentWin = (Window) Path.getComponent("/outerIndexWindow");

		try {
			win = (Window) Executions.createComponents("/WEB-INF/pages/test/testMain.zul", parentWin, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			MessageUtils.showErrorMessage(e.getLocalizedMessage());
			win.detach();
		}
	}

	/**
	 * When the 'about' toolbarButton is clicked.<br>
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void onClick$btnAbout(Event event) throws IOException, InterruptedException {

		/* get an instance of the borderlayout defined in the zul-file */
		Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
		/* get an instance of the searched CENTER layout area */
		Center center = bl.getCenter();

		Executions.createComponents("/secure/aboutWeb.zul", null, null);
	}

	/**
	 * Creates a page from a zul-file in a tab in the center area of the
	 * borderlayout. Checks if the tab is opened before. If yes than it selects
	 * this tab.
	 * 
	 * @param zulFilePathName
	 *            The ZulFile Name with path.
	 * @param tabName
	 *            The tab name.
	 * @throws InterruptedException
	 */
	private void showPage(String zulFilePathName, String tabName) throws InterruptedException {

		try {
			// TODO get the parameter for working with tabs from the application
			// params
			final int workWithTabs = 1;

			if (workWithTabs == 1) {

				/* get an instance of the borderlayout defined in the zul-file */
				Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
				/* get an instance of the searched CENTER layout area */
				Center center = bl.getCenter();
				// get the tabs component
				Tabs tabs = (Tabs) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter");

				/**
				 * Check if the tab is already opened than select them and<br>
				 * go out of here. If not than create them.<br>
				 */

				Tab checkTab = null;
				try {
					// checkTab = (Tab) tabs.getFellow(tabName);
					checkTab = (Tab) tabs.getFellow("tab_" + tabName.trim());
					checkTab.setSelected(true);
				} catch (final ComponentNotFoundException ex) {
					// Ignore if can not get tab.
				}

				if (checkTab == null) {

					Tab tab = new Tab();
					tab.setId("tab_" + tabName.trim());
					tab.setLabel(tabName.trim());
					tab.setClosable(true);

					tab.setParent(tabs);

					Tabpanels tabpanels = (Tabpanels) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter").getFellow("tabpanelsBoxIndexCenter");
					Tabpanel tabpanel = new Tabpanel();
					tabpanel.setHeight("100%");
					tabpanel.setStyle("padding: 0px;");
					tabpanel.setParent(tabpanels);

					/**
					 * Create the page and put it in the tabs area. If zul-file
					 * is not found, detach the created tab
					 */
					try {
						Executions.createComponents(zulFilePathName, tabpanel, null);
						tab.setSelected(true);
					} catch (final Exception e) {
						tab.detach();
					}

				}
			} else {
				/* get an instance of the borderlayout defined in the zul-file */
				Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
				/* get an instance of the searched CENTER layout area */
				Center center = bl.getCenter();
				/* clear the center child comps */
				center.getChildren().clear();
				/**
				 * create the page and put it in the center layout area
				 */
				Executions.createComponents(zulFilePathName, center, null);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("--> calling zul-file: " + zulFilePathName);
			}
		} catch (final Exception e) {
			Messagebox.show(e.toString());
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public void setCurrentDesktopHeight(int desktopHeight) {
		if (isTreeMenu() == true) {
			this.currentDesktopHeight.setValue(Integer.valueOf(desktopHeight));
		} else {
			this.currentDesktopHeight.setValue(Integer.valueOf(desktopHeight - 30));
		}
	}

	public int getCurrentDesktopHeight() {
		return this.currentDesktopHeight.getValue().intValue();
	}

	public void setCurrentDesktopWidth(int currentDesktopWidth) {
		this.currentDesktopWidth.setValue(Integer.valueOf(currentDesktopWidth));
	}

	public int getCurrentDesktopWidth() {
		return this.currentDesktopWidth.getValue().intValue();
	}

	public void setTreeMenu(boolean treeMenu) {
		this.CBtreeMenu.setChecked(treeMenu);
	}

	public boolean isTreeMenu() {
		return this.CBtreeMenu.isChecked();
	}

	public void setMainMenuCtrl(MainMenuCtrl mainMenuCtrl) {
		this.mainMenuCtrl = mainMenuCtrl;
	}

	public MainMenuCtrl getMainMenuCtrl() {
		return mainMenuCtrl;
	}
  
//	@Override
//  @WireVariable("userWorkspace")
	public void setUserWorkspace(UserWorkspace userWorkspace) {
		_userWorkspace = userWorkspace;
	}
}
