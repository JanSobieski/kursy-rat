package pl.wd.kursy.web.security;

import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import pl.wd.kursy.misc.Util;
import pl.wd.kursy.service.interf.DataServiceInt;

public class AppManagerBean {
	private static AppManagerBean _appManagerBean;
	
	private String _mainAdmin;
	private String _mainAdminPass;
	private DataServiceInt _dataServiceProvider;
	private SessionFactoryImpl _sessionFactory;

	public AppManagerBean() {
		setAppManagerBean(this);
	}

	public static AppManagerBean getAppManagerBean() {
		return _appManagerBean;
	}

	public static void setAppManagerBean( AppManagerBean appManagerBean ) {
		_appManagerBean = appManagerBean;
	}
	
	public DataServiceInt getDataServiceProvider() {
		return _dataServiceProvider;
	}

	public void setDataServiceProvider(DataServiceInt dataServiceProvider) {
		_dataServiceProvider = dataServiceProvider;
	}

	public void setMainAdmin(String ma) {
		_mainAdmin = ma;
	}

	public String getMainAdmin() {
		return _mainAdmin;
	}
	
	public void setMainAdminPass(String map) {
		_mainAdminPass = map;
	}
	
	public String getMainAdminPass() {
		return _mainAdminPass;
	}

	public String getVersion() {
		try {
			return Util.get_version();
		} catch (Exception e) {
			return "error: " + e.toString();
		}
	}

	public SessionFactoryImpl getSessionFactory() {
		return _sessionFactory;
	}

	public void setSessionFactory(SessionFactoryImpl sessionFactory) {
		_sessionFactory = sessionFactory;
	}
	

}
