package pl.wd.kursy.web.ui.admin.model;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import pl.wd.kursy.data.User;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.data.filter.UserWebFilter;

public class UserListViewModel<E> extends ListModelList<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3582041428390546097L;
	private final UserWorkspace _workspace;
	private User selected;

	public UserListViewModel(UserWorkspace workspace) {
		_workspace = workspace;
	}

	public List<User> getUserList(UserWebFilter filter) throws Exception {
		List<User> users = new ArrayList<User>();
		List<User> usersDB = null;
		try {
			usersDB = _workspace.getDataServiceProvider().getUsers();
		} finally {
			_workspace.getDataServiceProvider().closeDbSession();
		}
		for (User userDB : usersDB) {
			User user = new User(userDB);
			if (filter != null) {
				if (filter.getId().length() > 0) {
					if (!Integer.toString(user.getId()).contains(filter.getId())) {
						continue;
					}
				}
				if (filter.getName().length() > 0) {
					if (!user.getLogin().toLowerCase().contains(filter.getName())) {
						continue;
					}
				}
				// if ( filter.getPersonName().length() > 0 ) {
				// if (
				// !user.getPersonName().toLowerCase().contains(filter.getPersonName())
				// ) {
				// continue;
				// }
				// }
			}
			users.add(user);
		}

		return users;
	}

	public void setSelectedUser(User selected) {
		this.selected = selected;
	}

	public User getSelectedUser() {
		return selected;
	}

	public void showData(Listbox userList, UserWebFilter filter) {
		try {
			List<User> users = getUserList(filter);
			clear();
			addAll(users);
			userList.setModel(this);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}