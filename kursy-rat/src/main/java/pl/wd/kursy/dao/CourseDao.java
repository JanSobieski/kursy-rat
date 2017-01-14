package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import pl.wd.kursy.data.Course;

public class CourseDao {
	private Database _db;
	
	public CourseDao(Database db) {
		_db = db;
	}
	
	public List<Course> getCourses() throws Exception {
		Session session = _db.getSession();
		Query<Course> query = session.createQuery("from Course as course");
		List<Course> courses = query.list();
		
		return courses;
	}

}
