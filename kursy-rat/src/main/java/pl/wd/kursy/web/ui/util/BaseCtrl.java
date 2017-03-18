package pl.wd.kursy.web.ui.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.security.access.annotation.Secured;
import org.wd.web.util.CommonBaseCtrl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Row;

import pl.wd.kursy.data.interf.BasicTypeIntf;
import pl.wd.kursy.web.UserWorkspace;

/**
 * Base controller for creating the controllers of the zul files with the spring
 * framework.
 * 
 */
abstract public class BaseCtrl extends CommonBaseCtrl implements Serializable {

	private static final long serialVersionUID = -1171206258809472640L;
	
	public static final String READ_ONLY = "readOnly";
	public static final String SKIP_LOADING = "skipLoading";

	protected transient UserWorkspace _userWorkspace;
	protected boolean _readOnly = true;

	public BaseCtrl() {
		super();
	}
	
	protected void isAllowed(Method mtd) {
		final Annotation[] annotations = mtd.getAnnotations();
		for (final Annotation annotation : annotations) {
			if (annotation instanceof Secured) {
				final Secured secured = (Secured) annotation;
				for (final String rightName : secured.value()) {
					if (!_userWorkspace.isAllowed(rightName)) {
						throw new SecurityException("Call of this method is not allowed! Missing right: \n\n"
								+ "needed RightName: " + rightName + "\n\n" + "Method: " + mtd);
					}
				}
				return;
			}
		}
	}

	protected boolean lockRecord(String table, int id) {
		return lockRecord(table, id, null );
	}

	protected boolean lockRecord(String table, int id, String message ) {
		String msg = "Dane zablokowane przez użytkownika: \r\n%s" +  "\r\nDostęp tylko do odczytu.";
		if ( message != null ) {
			msg = message;
		}
		try {
//			Request request = ConnectionUtil.lock_record_request(getUserWorkspace().getClientId(), table, id);
//			ConnectorInt connector = Context.get_instance().getConnectorFactory().create_connector();
//			Transport response = connector.sendRequest(request);
//			User lck_usr = ConnectionUtil.get_lock_response(response);
//			if (lck_usr != null) {
////				String msg = "Dane zablokowane przez użytkownika: \r\n" + lck_usr.getFirst_last_name()
////						+ "\r\nDostęp tylko do odczytu.";
//				Messagebox.show(String.format(msg, lck_usr.getFirst_last_name()));
//				return false;
//			}

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		_readOnly = false;

		return true;
	}

	protected void unlockRecord(String table, int id) {
		try {
//			Request request = ConnectionUtil.unlock_record_request(getUserWorkspace().getClientId(), table, id);
//			ConnectorInt connector = Context.get_instance().getConnectorFactory().create_connector();
//			connector.sendRequest(request);
		} catch (Exception e) {
			// TODO: handle exception
		}
		_readOnly = true;
	}

	final public UserWorkspace getUserWorkspace() {
		return _userWorkspace;
	}

	public void setUserWorkspace(UserWorkspace userWorkspace) {
		_userWorkspace = userWorkspace;
	}

	public void onChoiceItemClicked(Event event) throws Exception {
		Event origin;
		// get event target
		if (event instanceof ForwardEvent) {
			origin = Events.getRealOrigin((ForwardEvent) event);
			Component target = origin.getTarget();
			Row row = (Row) target;
			BasicTypeIntf item = row.getValue();
			Hlayout hl = (Hlayout) row.getChildren().get(0);
			Checkbox cbSelected = (Checkbox) hl.getChildren().get(0);
			item.setSelected(cbSelected.isChecked());
		}
		if (event instanceof CheckEvent) {
			
			
			Checkbox cbSelected = (Checkbox) event.getTarget();
			Row row = (Row) cbSelected.getParent().getParent();
			BasicTypeIntf item = row.getValue();
			item.setSelected(cbSelected.isChecked());
		}
	}

	public boolean isReadOnly() {
		return _readOnly;
	}
}
