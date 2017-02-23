package pl.wd.kursy.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
	
	public void saveExercises(List<Exercise> exercises ) throws Exception {
		Session session = _db.getSession();
		Set<Integer> ids = new HashSet<>();
		exercises.stream().forEach( (exercise) -> {
			if ( exercise.getId() > 0 ) {
				ids.add(exercise.getId());
			}
		} );
		
		Set<Integer> ids2Del = new HashSet<>();
		List<Exercise> exercisesDb = getExercises();
		exercisesDb.stream().forEach(  (exercise) -> {
			if ( !ids.contains(exercise.getId() )) {
				ids2Del.add(exercise.getId());
			}
		});
		_db.closeSession();
		
		session = _db.getSession();
		
		Transaction tx = session.beginTransaction();
		for (Exercise exercise : exercises) {
			if ( exercise.getId() < 0 ) {
				exercise.setId(0);
				session.save(exercise);
			}
			else {
				session.update(exercise);
			}
		}
		
		if ( ids2Del.size() > 0 ) {
			String hql = "delete from Exercise ";
			String condCl = "(exs_id in (:ids))";
			hql += " where " + condCl;
			Query<Exercise> query = session.createQuery(hql);
			query.setParameterList("ids", ids2Del );
			
			query.executeUpdate();
		}

		try {
			tx.commit();
		} catch (Exception err) {
			tx.rollback();
			throw err;
		}
	}


	
}
