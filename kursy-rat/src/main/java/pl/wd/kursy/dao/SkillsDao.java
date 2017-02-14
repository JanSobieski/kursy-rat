package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.Skill;

public class SkillsDao {
	private Database _db;
	
	public SkillsDao(Database db) {
		_db = db;
	}
	
	public List<Skill> getSkills() throws Exception {
		Session session = _db.getSession();
		Query<Skill> query = session.createQuery("from Skill as skill");
		List<Skill> skills = query.list();
		
		return skills;
	}
	
	public void saveSkills(List<Skill> skills ) throws Exception {
		Session session = _db.getSession();
		Transaction tx = session.beginTransaction();
		for (Skill skill : skills) {
			if ( skill.getId() < 0 ) {
				skill.setId(0);
				session.save(skill);
			}
			else {
				session.update(skill);
			}
		}

		try {
			tx.commit();
		} catch (Exception err) {
			tx.rollback();
			throw err;
		}
	}
		

}
