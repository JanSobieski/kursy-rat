package pl.wd.kursy.data.wrapper;

import java.util.Collections;
import java.util.List;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.StudentGroup;
import pl.wd.kursy.data.comp.StudentGroupComparator;

public class StudentWrapper {
	private Student _student;
	private String _groups; 
	
	public StudentWrapper(Student student) {
		_student = student; 
	}

	public Student getStudent() {
		return _student;
	}

	public void setStudent(Student student) {
		_student = student;
	}

	public String getGroups() {
		return _groups;
	}

	public void setGroups(String groups) {
		_groups = groups;
	}
	
	public void setGroups(List<StudentGroup> groups) {
		Collections.sort(groups, new StudentGroupComparator(true, StudentGroupComparator.TYPE_NAME));
		_groups = "";
		groups.stream().forEach( (group) -> {
			if ( _groups.length() > 0 ) {
				_groups += ",  ";
			}
			_groups += group.getName();
		} );
	}

}
