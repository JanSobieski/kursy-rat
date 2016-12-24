package pl.wd.kursy.web.ui.admin.model;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.User;
import pl.wd.kursy.web.UserWorkspace;

public class UserDialogViewModel {
	private final UserWorkspace _workspace;

	public UserDialogViewModel( UserWorkspace workspace) throws Exception {
		_workspace = workspace;
	}

	public void saveOrUpdate( User user ) throws Exception {
		_workspace.getDataServiceProvider().saveOrUpdate(user);
		_workspace.getDataServiceProvider().closeDbSession();
	}

}
