package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.User;

public class StudentDao {
	private Database _db;
	
	public StudentDao(Database db) {
		_db = db;
	}
	
	public List<Student> getStudents( int courseId) throws Exception {
		Session session = _db.getSession();
		String hql = "from Student as student";
		String condCl = "(student.courseId=" + courseId + ")";
		hql += " where " + condCl;
		Query<Student> query = session.createQuery( hql );
		
		List<Student> students = query.list();
		
		return students;
	}
	
	public void updateStudent( Student student ) throws Exception {
		Session session = _db.getSession(true);
		Transaction tx = session.beginTransaction();
		if ( student.getId() > 0 ) {
			session.update(student);
		}
		else {
			session.save(student);
		}

		try {
			tx.commit();
		} catch (Exception err) {
			tx.rollback();
			throw err;
		}
	}


}
