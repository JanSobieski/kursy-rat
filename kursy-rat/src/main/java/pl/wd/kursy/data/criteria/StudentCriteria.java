package pl.wd.kursy.data.criteria;

import pl.wd.kursy.data.Student;

public class StudentCriteria extends Student {
	private static final long serialVersionUID = 7028629058927654001L;
	
	private int _courseId;
	private int _groupId;

	public int getCourseId() {
		return _courseId;
	}

	public void setCourseId(int courseId) {
		_courseId = courseId;
	}

	public int getGroupId() {
		return _groupId;
	}

	public void setGroupId(int groupId) {
		_groupId = groupId;
	}

}
