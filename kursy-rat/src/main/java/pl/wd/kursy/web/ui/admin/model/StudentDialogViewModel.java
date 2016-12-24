package pl.wd.kursy.web.ui.admin.model;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.web.UserWorkspace;

public class StudentDialogViewModel {
	private final UserWorkspace _workspace;

	public StudentDialogViewModel( UserWorkspace workspace) throws Exception {
		_workspace = workspace;
	}

	public void saveOrUpdate( Student student ) throws Exception {
		_workspace.getDataServiceProvider().saveOrUpdate(student);
		_workspace.getDataServiceProvider().closeDbSession();
	}
	
}
