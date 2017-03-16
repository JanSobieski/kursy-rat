package pl.wd.kursy.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pl.wd.kursy.data.Exercise;

public class ExerciseDao {
	private Database _db;
	
	public ExerciseDao(Database db) {
		_db = db;
	}

	public List<Exercise> getExercises() throws Exception {
		Session session = _db.getSession();
		
		return getExercises(session);
	}

	@SuppressWarnings("unchecked")
	public List<Exercise> getExercises(Session session) throws Exception {
		Query<Exercise> query = session.createQuery("from Exercise as exercise");
		List<Exercise> courses = query.list();
		
		return courses;
	}
	
	@SuppressWarnings("unchecked")
	public List<Exercise> getExercises(boolean rko) throws Exception {
		Session session = _db.getSession();
		
		Query<Exercise> query = session.createQuery("from Exercise as exercise where (exercise.all_skills_obligatory =:rko)").setParameter("rko", rko);
		List<Exercise> courses = query.list();
		
		return courses;
	}

	public void saveExercises(List<Exercise> exercises ) throws Exception {
		Session session = _db.getFreeSession();
		Set<Integer> ids = new HashSet<>();
		exercises.stream().forEach( (exercise) -> {
			if ( exercise.getId() > 0 ) {
				ids.add(exercise.getId());
			}
		} );
		
		Set<Integer> ids2Del = new HashSet<>();
		List<Exercise> exercisesDb = getExercises(session);
		exercisesDb.stream().forEach(  (exercise) -> {
			if ( !ids.contains(exercise.getId() )) {
				ids2Del.add(exercise.getId());
			}
		});
		session.close();
		
		session = _db.getSession();
		
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

	}


	
}
