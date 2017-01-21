package  pl.wd.kursy.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.User;
import pl.wd.kursy.service.interf.DataServiceInt;
import pl.wd.kursy.web.security.Context;

/**
 * Workspace for the user. One workspace per userSession. <br>
 * <br>
 * Every logged in user have his own workspace. <br>
 * Here are stored several properties for the user. <br>
 * <br>
 * 1. Access the rights that the user have. <br>
 * 2. The office for that the user are logged in. <br>
 * 
 */

@SessionScoped @Named("userWorkspace")
public class UserWorkspace implements Serializable, DisposableBean {
	private long _client_id;
	private int _courseId;
	private User _user = new User();
	
	private DataServiceInt _dataServiceProvider;

	private static final long serialVersionUID = -3936210543827830197L;
	private final static Logger logger = Logger.getLogger(UserWorkspace.class);

	static private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * Get a logged-in users WorkSpace which holds all necessary vars. <br>
	 * 
	 * @return the users WorkSpace
	 * @deprecated should be replaced by Spring !
	 */
	@Deprecated
	public static UserWorkspace getInstance() {
		return (UserWorkspace)SpringUtil.getBean("userWorkspace", UserWorkspace.class);
	}

	private String userLanguage;
	private String browserType;

	/**
	 * Indicates that as mainMenu the TreeMenu is used, otherwise BarMenu.
	 * 
	 * true = init.
	 */
	//private boolean treeMenu = true;

	/**
	 * difference in the height between TreeMenu and BarMenu.
	 */
	private Set<String> grantedAuthoritySet = null;

	/**
	 * Default Constructor
	 */
	public UserWorkspace() {
		if (logger.isDebugEnabled()) {
			logger.debug("create new UserWorkspace [" + this + "]");
		}

		// speed up the ModalDialogs while disabling the animation
		Window.setDefaultActionOnShow("");
	}

	/**
	 * Logout with the spring-security logout action-URL.<br>
	 * Therefore we make a sendRedirect() to the logout uri we <br>
	 * have configured in the spring-config.br>
	 */
	public void doLogout() {
		try {
			destroy();
			//Context.get_instance().set_client_id(_client_id);
			//ConnectionUtil.logout();

			Executions.sendRedirect("/j_spring_security_logout");
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	/**
	 * Copied the grantedAuthorities to a Set of strings <br>
	 * for a faster searching in it.
	 * 
	 * @return String set of GrantedAuthorities (rightNames)
	 */
	private Set<String> getGrantedAuthoritySet() {
		//TODO nie uzywane
		
//		if (this.grantedAuthoritySet == null) {
//
//			final Collection<GrantedAuthority> list = getAuthentication().getAuthorities();
//			this.grantedAuthoritySet = new HashSet<String>(list.size());
//
//			for (final GrantedAuthority grantedAuthority : list) {
//				this.grantedAuthoritySet.add(grantedAuthority.getAuthority());
//			}
//		}
		return this.grantedAuthoritySet;
	}

	/**
	 * Checks if a right is in the <b>granted rights</b> that the logged in user
	 * have. <br>
	 * 
	 * @param rightName
	 * @return true, if the right is in the granted user rights.<br>
	 *         false, if the right is not granted to the user.<br>
	 */
	public boolean isAllowed(String rightName) {
		
		
		return getGrantedAuthoritySet().contains(rightName);
	}

	public Properties getUserLanguageProperty() {

		// // TODO only for testing. we must get the language from
		// // the users table filed
		// userLanguageProperty =
		// ApplicationWorkspace.getInstance().getPropEnglish();
		// userLanguageProperty =
		// ApplicationWorkspace.getInstance().getPropGerman();
		//
		// return userLanguageProperty;
		return null;
	}

	@Override
	public void destroy() {
		this.grantedAuthoritySet = null;
		SecurityContextHolder.clearContext();

		if (logger.isDebugEnabled()) {
			logger.debug("destroy Workspace [" + this + "]");
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public String getUserLanguage() {
		return this.userLanguage;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getBrowserType() {
		return this.browserType;
	}

	public int getMenuOffset() {
		int result = 0;

		return result;
	}

//	public void setTreeMenu(boolean treeMenu) {
//		this.treeMenu = treeMenu;
//	}
//
//	public boolean isTreeMenu() {
//		return treeMenu;
//	}

	public long getClientId() {
		if ( _client_id == 0 ) {
			//request.getSession().setAttribute(CLIENT_ID, cl_id);
		}
		return _client_id;
	}

	public void setClientId( long id ) {
		_client_id = id;
	}

	public int getUserId() {
		return _user.get_id();
	}

	public User getUser() {
		return _user;
	}

	public void setUser( User user ) {
		_user = user;
	}
	
	public void loadUser() {
		_user  = Context.getInstance().getUserByClient(_client_id);
	}

	public boolean check_authorisation_read( String authorisation ) throws Exception {
		return getDataServiceProvider().check_authorisation_read( _user, authorisation );
	}

	public DataServiceInt getDataServiceProvider() {
		return _dataServiceProvider;
	}

	public void setDataServiceProvider( DataServiceInt dataServiceProvider ) {
		_dataServiceProvider = dataServiceProvider;
	}

	public int getCourseId() {
		return _courseId;
	}

	public void setCourseId(int courseId) {
		_courseId = courseId;
	}
	
}
