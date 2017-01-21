package pl.wd.kursy.web.ui.admin.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.User;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.data.filter.StudentFilter;

public class StudentListViewModel<E> extends ListModelList<Object> {
	private static final long serialVersionUID = 3582041428390546097L;
	
	private static final Logger logger = Logger.getLogger(StudentListViewModel.class);

	private final UserWorkspace _workspace;
	private User selected;

	public StudentListViewModel(UserWorkspace workspace) {
		_workspace = workspace;
	}

	public List<Student> getStudentList(StudentFilter filter) throws Exception {
		List<Student> students = new ArrayList<>();
		List<Student> studentsDB = null;
		try {
			studentsDB = _workspace.getDataServiceProvider().getStudents(_workspace.getCourseId());
		} finally {
			_workspace.getDataServiceProvider().closeDbSession();
		}
		for (Student student : studentsDB) {
			if (filter != null) {
				if (filter.getSid().length() > 0) {
					if (!Integer.toString(student.getId()).contains(filter.getSid())) {
						continue;
					}
				}
				if (filter.getFirstName().length() > 0) {
					if (!student.getFirstName().toLowerCase().contains(filter.getFirstName())) {
						continue;
					}
				}
				if (filter.getLastName().length() > 0) {
					if (!student.getLastName().toLowerCase().contains(filter.getLastName())) {
						continue;
					}
				}
			}
			students.add(student);
		}

		return students;
	}

	public void setSelectedUser(User selected) {
		this.selected = selected;
	}

	public User getSelectedUser() {
		return selected;
	}

	public void showData(Listbox studentList, StudentFilter filter) {
		try {
			List<Student> students = getStudentList(filter);
			clear();
			addAll(students);
			studentList.setModel(this);
		} catch (Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}

	}

}