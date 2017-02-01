package pl.wd.kursy.web.ui.admin.model;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.web.UserWorkspace;

public class CourseDialogViewModel {
	private final UserWorkspace _workspace;

	public CourseDialogViewModel( UserWorkspace workspace) throws Exception {
		_workspace = workspace;
	}

	public void saveOrUpdate( Course course ) throws Exception {
		_workspace.getDataServiceProvider().saveOrUpdate(course);
		_workspace.getDataServiceProvider().closeDbSession();
	}
	
}
