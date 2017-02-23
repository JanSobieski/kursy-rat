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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import pl.wd.kursy.web.ui.util.WebUtil;

@Entity
@Table(name = "exercises")
@GenericGenerator(name = "hibernate-increment", strategy = "increment")
public class Exercise extends BasicType implements Serializable {
	private static final long serialVersionUID = 6168979257328569867L;

	private Set<Skill> _skills = new HashSet<>();

	private boolean _all_skills_obligatory;
	private String _description;

	public Exercise() {
	}
	
	public Exercise( Exercise exercise ) {
		super( exercise.getId() );
	}

	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "exs_id")
	public int getId() {
		return _id;
	}

	public void setId( int id ) {
		_id = id;
	}

	public String getName() {
		return WebUtil.normValue(_name);
	}

	public void setName(String name) {
		_name = name;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	@JoinTable(name = "exercise_skills", joinColumns = { @JoinColumn(name = "exs_id", nullable=false)}, inverseJoinColumns = @JoinColumn(name = "skl_id", nullable=false))
	//@BatchSize(size = 300)
	public Set<Skill> getSkills() {
		return _skills;
	}

	public void setSkills( Set<Skill> skills ) {
		_skills = skills;
	}

	public boolean getAll_skills_obligatory() {
		return _all_skills_obligatory;
	}

	public void setAll_skills_obligatory(boolean all_skills_obligatory) {
		_all_skills_obligatory = all_skills_obligatory;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}
	
}
