package pl.wd.kursy.web.security;

import pl.wd.kursy.misc.Util;

public class AppManagerBean {
	private static AppManagerBean _appManagerBean;

	public AppManagerBean() {
		set_appManagerBean(this);
	}

	public static AppManagerBean get_appManagerBean() {
		return _appManagerBean;
	}

	public static void set_appManagerBean( AppManagerBean appManagerBean ) {
		_appManagerBean = appManagerBean;
	}

	
	
	public String getVersion() {
		try {
			return Util.get_version();
		} catch (Exception e) {
			return "error: " + e.toString();
		}
	}
	

}
