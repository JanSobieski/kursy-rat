package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.data.Exercise;

public class ExerciseDao {
	private Database _db;
	
	public ExerciseDao(Database db) {
		_db = db;
	}
	
	public List<Exercise> getExercises() throws Exception {
		Session session = _db.getSession();
		Query<Exercise> query = session.createQuery("from Exercise as exercise");
		List<Exercise> courses = query.list();
		
		return courses;
	}

	
}
