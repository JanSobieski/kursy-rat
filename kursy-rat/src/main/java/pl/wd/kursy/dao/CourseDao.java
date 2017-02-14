package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.data.Student;

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

	public void update( Course course ) throws Exception {
		Session session = _db.getSession(true);
		Transaction tx = session.beginTransaction();
		if ( course.getId() > 0 ) {
			session.update(course);
		}
		else {
			session.save(course);
		}

		try {
			tx.commit();
		} catch (Exception err) {
			tx.rollback();
			throw err;
		}
	}
	
	public Course getCourse( int courseId ) throws Exception {
		Session session = _db.getSession(true);
		return (Course) session.get(Course.class, courseId );
	}
	
}
