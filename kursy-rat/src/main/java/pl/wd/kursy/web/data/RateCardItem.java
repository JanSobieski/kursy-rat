package pl.wd.kursy.web.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Skill;

public class RateCardItem extends BasicType implements Serializable {
	private static final long serialVersionUID = 6260019166689800965L;

	private int _exerciseId;
	private int _studentId;
	private Set<Skill> _skills = new HashSet<>();

	public int getId() {
		return _id;
	}

	public void setId( int id ) {
		_id = id;
	}

	public Set<Skill> getSkills() {
		return _skills;
	}

	public void setSkills( Set<Skill> skills ) {
		_skills = skills;
	}

	public int getExerciseId() {
		return _exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		_exerciseId = exerciseId;
	}

	public int getStudentId() {
		return _studentId;
	}

	public void setStudentId(int studentId) {
		_studentId = studentId;
	}
}
