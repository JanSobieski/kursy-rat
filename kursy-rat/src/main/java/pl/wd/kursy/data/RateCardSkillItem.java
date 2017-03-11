package pl.wd.kursy.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "rate_card_skill_items")
@GenericGenerator(name = "hibernate-increment", strategy = "increment")
public class RateCardSkillItem extends BasicType implements Serializable {
	private static final long serialVersionUID = -4761161562011411625L;

	private int _skillId;
	private int _statusId;

	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "rcs_id")
	public int getId() {
		return _id;
	}

	public void setId( int id ) {
		_id = id;
	}

	@Column(name = "skl_id")
	public int getSkillId() {
		return _skillId;
	}

	public void setSkillId(int skillId) {
		_skillId = skillId;
	}

	@Column(name = "status_id")
	public int getStatusId() {
		return _statusId;
	}

	public void setStatusId(int statusId) {
		_statusId = statusId;
	}

}
