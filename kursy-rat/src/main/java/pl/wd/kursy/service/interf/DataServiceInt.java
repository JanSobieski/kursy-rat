package pl.wd.kursy.service.interf;

import java.util.List;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.User;
import pl.wd.kursy.exception.BusinessLogicException;

public interface DataServiceInt {
	public void closeDbSession() throws Exception;

	public boolean check_authorisation_read( User user, String authorisation ) throws Exception;
	public String userLogin(String user, String pass );
	
	public void changePass( User user, String oldPass, String newPass ) throws Exception, BusinessLogicException;
	public List<User> getUsers() throws Exception;
	public void saveOrUpdate(User user) throws Exception;
	
	public List<Student> getStudents() throws Exception;
	public void saveOrUpdate(Student student) throws Exception;
	


}
