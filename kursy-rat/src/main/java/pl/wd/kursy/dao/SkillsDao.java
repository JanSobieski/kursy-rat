package pl.wd.kursy.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
		Set<Integer> ids = new HashSet<>();
		skills.stream().forEach( (skill) -> {
			if ( skill.getId() > 0 ) {
				ids.add(skill.getId());
			}
		} );
		
		Set<Integer> ids2Del = new HashSet<>();
		List<Skill> skillsDb = getSkills();
		skillsDb.stream().forEach(  (skill) -> {
			if ( !ids.contains(skill.getId() )) {
				ids2Del.add(skill.getId());
			}
		});
		_db.closeSession();

		session = _db.getSession();
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
		
		if ( ids2Del.size() > 0 ) {
			String hql = "delete from Skill ";
			String condCl = "(skl_id in (:ids))";
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
