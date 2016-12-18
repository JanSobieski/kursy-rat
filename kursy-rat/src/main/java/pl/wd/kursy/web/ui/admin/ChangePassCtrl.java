package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.wd.web.util.MessageUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.constants.SystemConstants;
import pl.wd.kursy.exception.BusinessLogicException;
import pl.wd.kursy.misc.Util;
import pl.wd.kursy.web.ui.util.BaseCtrl;

public class ChangePassCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = 7489214566126516255L;

	private static final Logger logger = Logger.getLogger(ChangePassCtrl.class);

	protected Window changePassWindow; // autowired
	protected Textbox tbOldPass; // aurowired
	protected Textbox tbNewPass; // aurowired
	protected Textbox tbNewPass2; // aurowired

	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners

			doShowDialog();
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		} finally {
			try {
				getUserWorkspace().getDataServiceProvider().closeDbSession();
			} catch (Exception e2) {
				logger.error("Error", e2);
			}
		}
	}

	public void doShowDialog() throws InterruptedException {
		tbOldPass.setFocus(true);

		changePassWindow.doModal(); // open the dialog in modal mode
	}

	public void onClick$btnClose(Event event) throws InterruptedException {
		try {
			closeWindow();
		} catch (final Exception e) {
		}
	}

	private void closeWindow() {
		changePassWindow.onClose();

	}

	public void onClick$btnSave(Event event) throws InterruptedException {
		if (tbNewPass.getText().trim().length() == 0) {
			Messagebox.show("Nowe hasło nie może być puste!");
			return;
		}
		if (!tbNewPass.getText().equals(tbNewPass2.getText())) {
			Messagebox.show("Pierwsza i druga wersja nowego hasła są niezgodne!");
			return;
		}
		if (tbOldPass.getText().equals(tbNewPass.getText())) {
			Messagebox.show("Stare i nowe hasło identyczne!");
			return;
		}

		try {
			getUserWorkspace().getDataServiceProvider().changePass(getUserWorkspace().getUser(),
					Util.encode_password(tbOldPass.getText()), Util.encode_password(tbNewPass.getText()));
		} catch (BusinessLogicException ble) {
			if (SystemConstants.RESPONSE_WRONG_PASS.equals(ble.getMessage())) {
				MessageUtils.showErrorMessage("Stare hasło jest niepoprawne.");
				return;
			} else {
				MessageUtils.showErrorMessage("Zmiana hasła nie powiodła się.");
				return;
			}
		} catch (Exception e) {
			MessageUtils.showErrorMessage("Zmiana hasła nie powiodła się: " + e.getMessage());
			return;
		}

		closeWindow();
	}
}
