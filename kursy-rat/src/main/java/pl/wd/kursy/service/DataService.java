package pl.wd.kursy.service;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;

import pl.wd.kursy.dao.Database;
import pl.wd.kursy.dao.UserDao;
import pl.wd.kursy.data.User;
import pl.wd.kursy.service.interf.DataServiceInt;
import pl.wd.kursy.web.security.AppManagerBean;

public class DataService implements DataServiceInt, Serializable {
	private static final long serialVersionUID = -1169104155427694717L;
	static Logger logger = Logger.getLogger("pl.wd.kursy.service.DataService");

	private Database _db = new Database();
	
	public DataService() {
	}

	public void closeDbSession() throws Exception {
		_db.closeSession();
	}

	@Override
	public boolean check_authorisation_read( int user_id, String authorisation ) throws Exception {
		return true;
	}
	
	@Override
	public String userLogin(String login, String pass ) {
		String mainAdmin = AppManagerBean.getAppManagerBean().getMainAdmin();
		String mainAdminPass = AppManagerBean.getAppManagerBean().getMainAdminPass();

		User user = null;
		if ( ( mainAdmin != null ) && (mainAdmin.length() > 0 ) && ( mainAdminPass != null ) ) {
			if ( mainAdmin.equals(login) &&  mainAdminPass.equals(pass) ) {
				user = new User(login, pass);
			}
		}
		
		if ( user == null ) {
			UserDao userDao = new UserDao(_db);
			User user_db = null;
			try {
				user_db = userDao.getUserByLogin( login );
				_db.closeSession();
			} catch (Exception e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			if ( user_db == null ) {
				throw new BadCredentialsException("Autoryzacja odrzucona przez serwer.\r\nNiepoprawne hasło lub użytkownik.");
			}
			if ( (user_db.getPass() != null) && user_db.getPass().equals(pass) ) {
				user  = user_db;
			}
			else {
				throw new BadCredentialsException("Autoryzacja odrzucona przez serwer.\r\nNiepoprawne hasło lub użytkownik.");
			}
		}
		
		long clId = pl.wd.kursy.web.security.Context.getInstance().registerUser(user);
		String ret = Long.toString(clId) ;
		return ret;
	}
	

}
