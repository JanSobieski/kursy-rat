package pl.wd.kursy.web.data;

import pl.wd.kursy.data.Student;

public class MissingSkillGridRow {
	private String _groupText;
	private String _suggestedExercise;
	private Student _student;

	public String getGroupText() {
		return _groupText;
	}

	public void setGroupText(String groupText) {
		_groupText = groupText;
	}

	public Student getStudent() {
		return _student;
	}

	public void setStudent(Student student) {
		_student = student;
	}

	public String getSuggestedExercise() {
		return _suggestedExercise;
	}

	public void setSuggestedExercise(String suggestedExercise) {
		_suggestedExercise = suggestedExercise;
	}

}
