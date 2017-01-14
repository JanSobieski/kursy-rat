package pl.wd.kursy.dao;


import org.apache.log4j.Logger;
import org.hibernate.Session;

import pl.wd.kursy.web.security.AppManagerBean;

public class Database {
	static Logger logger = Logger.getLogger("pl.wd.kursy.dao.Database");

	private Session _session;

	public Session getSession() {
		return getSession(false);
	}

	public Session getSession( boolean force_new_session ) {
		if ( force_new_session ) {
			closeSession();
		}
		if ( _session == null ) {
			_session = AppManagerBean.getAppManagerBean().getSessionFactory().openSession();
			//_session = HibernateUtil.getSessionFactory().openSession();
		} else {
		}

		return _session;
	}

	public void closeSession() {
		if ( (_session != null) && (_session.isOpen()) ) {
			_session.close();
			_session = null;
		}
	}
}
