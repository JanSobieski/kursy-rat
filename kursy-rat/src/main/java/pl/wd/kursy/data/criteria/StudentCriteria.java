package pl.wd.kursy.data.criteria;

import java.util.ArrayList;
import java.util.List;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.StudentStatus;

public class StudentCriteria extends Student {
	private static final long serialVersionUID = 7028629058927654001L;
	
	private int _courseId;
	private int _groupId;
	private List<Integer> _statusIds = new ArrayList<>();

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
	
	public List<Integer> getStatusIds() {
		return _statusIds;
	}

}
