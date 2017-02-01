package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

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

}
