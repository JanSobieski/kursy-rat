package pl.wd.kursy.web.ui.admin.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.User;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.data.filter.StudentFilter;

public class CourseListViewModel<E> extends ListModelList<Object> {
	private static final long serialVersionUID = 3582041428390546097L;
	
	private static final Logger logger = Logger.getLogger(CourseListViewModel.class);

	private final UserWorkspace _workspace;
	private User selected;

	public CourseListViewModel(UserWorkspace workspace) {
		_workspace = workspace;
	}

	public List<Course> getStudentList(StudentFilter filter) throws Exception {
		List<Course> students = new ArrayList<>();
		List<Course> coursesDB = null;
		try {
			coursesDB = _workspace.getDataServiceProvider().getCourses();
		} finally {
			_workspace.getDataServiceProvider().closeDbSession();
		}
		for (Course course : coursesDB) {
			if (filter != null) {
				if (filter.getSid().length() > 0) {
					if (!Integer.toString(course.getId()).contains(filter.getSid())) {
						continue;
					}
				}
				if (filter.getName().length() > 0) {
					if (!course.getName().toLowerCase().contains(filter.getFirstName())) {
						continue;
					}
				}
			}
			students.add(course);
		}

		return students;
	}

	public void setSelectedUser(User selected) {
		this.selected = selected;
	}

	public User getSelectedUser() {
		return selected;
	}

	public void showData(Listbox courseList, StudentFilter filter) {
		try {
			List<Course> courses = getStudentList(filter);
			clear();
			addAll(courses);
			courseList.setModel(this);
		} catch (Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}

	}

}