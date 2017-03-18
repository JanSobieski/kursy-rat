package pl.wd.kursy.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.StudentGroup;
import pl.wd.kursy.data.criteria.StudentCriteria;

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
	
	public List<Student> getStudents(StudentCriteria crit) throws Exception {
		Set<Integer> ids = new HashSet<>();
		Session session = _db.getSession();
		String hql = "from Student as student";
		String condCl = "(student.courseId=" + crit.getCourseId() + ")";
		if ( crit.getGroupId() > 0 ) {
			StudentGroup group = session.get(StudentGroup.class, Integer.valueOf(crit.getGroupId()));
			group.getStudents().stream().forEach(  (student) -> {
				ids.add(student.getId());
			});
			if ( !ids.isEmpty() ) {
				condCl += " AND (student.id in (:ids))";
			}
		}
		if ( !crit.getStatusIds().isEmpty() ) {
			condCl += " AND (student.statusId in (:statusIds))";
			
		}
		hql += " where " + condCl;
		Query<Student> query = session.createQuery( hql );
		if ( !ids.isEmpty() ) {
			query.setParameterList("ids", ids );
		}
		if ( !crit.getStatusIds().isEmpty() ) {
			query.setParameterList("statusIds", crit.getStatusIds() );
		}
		
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
