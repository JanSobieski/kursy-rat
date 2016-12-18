package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Session;

import pl.wd.kursy.data.User;

public class UserDao {
	
	private Database _db;
	
	public UserDao(Database db) {
		_db = db;
	}
	
	public User getUserByLogin( String login ) throws Exception {
		Session session = _db.getSession();
		List<User> users = session.createQuery("from User as users where (users.login =:login)").setParameter("login", login).list();
		if ( users.isEmpty() ) {
			return null;
		}
		return users.get(0);
//		if ( !user_db.getActive() ) {
//			return null;
//		}
	}

}
