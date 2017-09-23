package pl.wd.kursy.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.wd.kursy.data.RateCardItem;
import pl.wd.kursy.data.StudentGroup;

public class RateCardDao {
	private Database _db;
	
	public RateCardDao(Database db) {
		_db = db;
	}

	public void save(List<RateCardItem> rateCards, int studentId) throws Exception {
		Session session = _db.getSession();
		Transaction tx = session.beginTransaction();
		
		for (RateCardItem rateCard : rateCards) {
			rateCard.setStudentId(studentId);
			if ( rateCard.getId() < 1 ) {
				rateCard.setId(0);
				session.save(rateCard);
			}
			else {
				session.update(rateCard);
			}
		}
		try {
			tx.commit();
		} catch (Exception err) {
			tx.rollback();
			throw err;
		}
	}
	
	public List<RateCardItem> getData( int studentId) throws Exception {
		Session session = _db.getSession();
		String hql = "from RateCardItem as rateCardItem";
		String condCl = "(rateCardItem.studentId=" + studentId + ")";
		hql += " where " + condCl;
		Query<RateCardItem> query = session.createQuery( hql );
		
		List<RateCardItem> groups = query.list();
		
		return groups;
	}
	
	public List<RateCardItem> getDataNoRKO( int studentId) throws Exception {
		Session session = _db.getSession();
		String hql = "select rateCardItem from RateCardItem as rateCardItem, Exercise as excercise";
		String condCl = "(rateCardItem.studentId=" + studentId + ")";
		condCl += " AND (rateCardItem.exerciseId=excercise.id)";
		condCl += " AND (excercise.all_skills_obligatory='FALSE')";
		hql += " where " + condCl;
		Query<RateCardItem> query = session.createQuery( hql );
		
		List<RateCardItem> groups = query.list();
		
		return groups;
	}
	
}
