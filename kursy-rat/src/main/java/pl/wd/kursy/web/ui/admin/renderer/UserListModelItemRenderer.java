package pl.wd.kursy.web.ui.admin.renderer;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import pl.wd.kursy.data.User;

public class UserListModelItemRenderer implements ListitemRenderer<User>, Serializable {
	private static final long serialVersionUID = 8136613342324757987L;
	private static final Logger logger = Logger.getLogger(UserListModelItemRenderer.class);

	@Override
	public void render( Listitem item, User data, int index ) throws Exception {
		final User user = (User) data;
		
		Listcell lc;
		lc = new Listcell(Integer.toString(user.getId()));
		lc.setStyle("text-align:right");
		lc.setParent(item);
		lc = new Listcell(user.getLogin());
		lc.setStyle("text-align:left");
		lc.setParent(item);
		lc = new Listcell(user.getDescription());
		lc.setStyle("text-align:left");
		lc.setParent(item);

		lc = new Listcell();
		Checkbox cb = new Checkbox();
		cb.setChecked(user.isAdmin());
		cb.setDisabled(true);
		lc.appendChild(cb);
		lc.setParent(item);
		
		item.setAttribute("data", data);
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onUserListItemDoubleClicked");
	}

}


