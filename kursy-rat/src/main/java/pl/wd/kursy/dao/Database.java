package pl.wd.kursy.dao;


import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.web.security.AppManagerBean;

public class Database implements Serializable {
	private static final long serialVersionUID = 7019739230521208928L;

	static Logger logger = Logger.getLogger("pl.wd.kursy.dao.Database");

	private Session _session;

	public Session getSession() {
		return getSession(false);
	}

	public Session getFreeSession() {
		return AppManagerBean.getAppManagerBean().getSessionFactory().openSession();
	}
	
	public Session getSession( boolean force_new_session ) {
		if ( force_new_session ) {
			closeSession();
		}
		if ( _session == null ) {
			_session = AppManagerBean.getAppManagerBean().getSessionFactory().openSession();
			//_session = HibernateUtil.getSessionFactory().openSession();
		} else {
		}

		return _session;
	}

	public void closeSession() {
		if ( (_session != null) && (_session.isOpen()) ) {
			_session.close();
			_session = null;
		}
	}
	
	public void saveExercisesAndSkills(List<Exercise> exercises, List<Skill> skills) throws Exception {
		Session session = getSession(true);

		Transaction tx = session.beginTransaction();

		ExerciseDao exerciseDao = new ExerciseDao(this);
		exerciseDao.saveExercises(exercises);

		SkillsDao skillDao = new SkillsDao(this);
		skillDao.saveSkills(skills);

		try {
			tx.commit();
		} catch (Exception err) {
			tx.rollback();
			throw err;
		}

	}

}
