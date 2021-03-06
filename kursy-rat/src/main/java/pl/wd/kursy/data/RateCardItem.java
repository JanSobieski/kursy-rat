package pl.wd.kursy.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "rate_card_items")
@GenericGenerator(name = "hibernate-increment", strategy = "increment")
public class RateCardItem extends BasicType implements Serializable {
	private static final long serialVersionUID = 6260019166689800965L;

	private int _exerciseId;
	private int _studentId;
	private java.util.Date _date_created; // timestamp
	private Set<RateCardSkillItem> _skills = new HashSet<>();

	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "rci_id")
	public int getId() {
		return _id;
	}

	public void setId( int id ) {
		_id = id;
	}
	
    @OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true )
    @JoinColumn(name = "rci_id", referencedColumnName = "rci_id", nullable=false, updatable=true, insertable=true )
	public Set<RateCardSkillItem> getSkills() {
		return _skills;
	}

	public void setSkills( Set<RateCardSkillItem> skills ) {
		_skills = skills;
	}

	@Column(name = "exs_id")
	public int getExerciseId() {
		return _exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		_exerciseId = exerciseId;
	}
	
	@Column(name = "stu_id")
	public int getStudentId() {
		return _studentId;
	}

	public void setStudentId(int studentId) {
		_studentId = studentId;
	}
	
	@Column(columnDefinition="timestamp without time zone")
	public java.util.Date getDate_created() {
		return _date_created;
	}

	public void setDate_created( java.util.Date date_created ) {
		_date_created = date_created;
	}

	public void addSkill( Skill skill ) {
		RateCardSkillItem ski = new RateCardSkillItem();
		ski.setSkillId(skill.getId());
		_skills.add(ski);
	}
	
	public RateCardSkillItem getSkill(Skill skill) {
		return _skills.stream().filter( sk -> (sk.getSkillId() == skill.getId()) ).findFirst().orElse(null);
	}
	
	public boolean containsSkill( Skill skill ) {
		return _skills.stream().filter( sk -> (sk.getSkillId() == skill.getId()) ).findFirst().isPresent();
	}
	
}
