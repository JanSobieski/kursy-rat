package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
	
	public User getUser( int id ) throws Exception {
		Session session = _db.getSession();
		User user = (User) session.get(User.class, id);

		return user;
	}

	public void updateUser( User user ) throws Exception {
		Session session = _db.getSession(true);
		Transaction tx = session.beginTransaction();
		if ( user.getId() > 0 ) {
			session.update(user);
		}
		else {
			session.save(user);
		}

		try {
			tx.commit();
		} catch (Exception err) {
			tx.rollback();
			throw err;
		}
	}
	
	public List<User> getUsers() throws Exception {
		Session session = _db.getSession();
		Query<User> query = session.createQuery("from User as users");
		List<User> users = query.list();
		return users;
	}
	
	public List<User> getUsers(User user) throws Exception {
		Session session = _db.getSession();
		String cond = "";
		String hql = "from User as users";
		
		if ( user != null ) {
			if ( user.getLogin().length() > 0 ) {
				cond += "(lower(users.login) = '" + user.getLogin().toLowerCase() + "')";
			}
		}

		if ( cond.length() > 0 ) {
			hql = hql + " where " + cond;
		}

		Query query = session.createQuery(hql);

		List<User> users = query.list();
		return users;
	}
	


}
