package pl.wd.kursy.service;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.zkoss.util.resource.Labels;

import pl.wd.kursy.dao.CourseDao;
import pl.wd.kursy.dao.Database;
import pl.wd.kursy.dao.StudentDao;
import pl.wd.kursy.dao.UserDao;
import pl.wd.kursy.data.Course;
import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.User;
import pl.wd.kursy.data.constants.Rights;
import pl.wd.kursy.data.constants.SystemConstants;
import pl.wd.kursy.exception.BusinessLogicException;
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
	public boolean check_authorisation_read( User user, String authorisation ) throws Exception {
		
		if ( Rights.AUTH_COMMON_USER.equals(authorisation) ) {
			if ( user.get_id() == 0 ) {
				return false;
			}
		}
		if ( Rights.SHOW_MENU_ADMINISTRATION.equals(authorisation) ) {
			return user.isAdmin();
		}
		
		
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
				user.setAdmin(true);
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
	
	@Override
	public void changePass( User user, String oldPass, String newPass ) throws Exception, BusinessLogicException {
		UserDao userDao = new UserDao(_db);
		try {
			User userDb = userDao.getUser(user.get_id());
			if ( userDb == null ) {
				throw new BusinessLogicException(SystemConstants.RESPONSE_CHANGE_PASS_FAILED, "");
			}
			if ( !userDb.getPass().equals(oldPass) ) {
				throw new BusinessLogicException(SystemConstants.RESPONSE_WRONG_PASS, "");
			}
			userDb.setPass(newPass);
			userDao.updateUser(userDb);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		finally {
			_db.closeSession();
		}
	}
	
	@Override
	public List<User> getUsers(User user) throws Exception {
		UserDao userDao = new UserDao(_db);
		List<User> users = userDao.getUsers(user);
		//Collections.sort(users, User.get_last_name_comparator());

		return users;
	}
	
	@Override
	public List<User> getUsers() throws Exception {
		UserDao userDao = new UserDao(_db);
		List<User> users = userDao.getUsers();
		//Collections.sort(users, User.get_last_name_comparator());

		return users;
	}
	
	@Override
	public void saveOrUpdate(User user) throws Exception {
		UserDao userDao = new UserDao(_db);
		User userDB = userDao.getUser(user.get_id());
		
		List<User> users = userDao .getUsers(user);
		for (User user2 : users) {
			if (user2.get_id() != user.get_id()) {
				String msg = Labels.getLabel("message_userExistsLogin");
				throw new BusinessLogicException(msg + user2.getLogin());
			}
		}
		if (user.get_id() > 0) {
			userDB = userDao.getUser(user.get_id());
		} else {
		}
		if ((user.getPass() != null) && (user.getPass().length() > 0)) {
			userDB.setPass(user.getPass());
		}
		userDB.setLogin(user.getLogin());
		userDB.setDescription(user.getDescription());
		userDB.setAdmin(user.isAdmin());
		userDao.updateUser(userDB);
		
		if (user.get_id() == 0) {
			user.set_id(userDB.get_id());
		}
	}
	
	@Override
	public List<Student> getStudents(int courseId) throws Exception {
		StudentDao studentDao = new StudentDao(_db);
		//Collections.sort(users, User.get_last_name_comparator());
		
		return studentDao.getStudents(courseId);
	}
	
	@Override
	public void saveOrUpdate(Student student) throws Exception {
		StudentDao studentDao = new StudentDao(_db);
		
		studentDao.updateStudent(student);
	}

	@Override
	public List<Course> getCourses() throws Exception {
		CourseDao courseDao = new CourseDao(_db);
		
		return courseDao.getCourses();
	}

	@Override
	public void saveOrUpdate(Course course) throws Exception {
		CourseDao courseDao = new CourseDao(_db);
		courseDao.update(course);
	}
	
	public Course getCourse( int courseId ) throws Exception {
		CourseDao courseDao = new CourseDao(_db);
		
		return courseDao.getCourse( courseId );
	}
	
}
