package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.wd.web.util.MultiLineMessageBox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.web.ui.util.BaseCtrl;

public class CourseChoiceCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = 7489214566126516255L;

	private static final Logger logger = Logger.getLogger(CourseChoiceCtrl.class);

	protected Window courseChoiceWindow; // autowired
	protected Combobox cmbCourse; // aurowired

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

		courseChoiceWindow.doModal(); // open the dialog in modal mode
	}

	public void onClick$btnClose(Event event) throws InterruptedException {
		try {
			closeWindow();
		} catch (final Exception e) {
		}
	}

	private void closeWindow() {
		courseChoiceWindow.onClose();

	}

	public void onClick$btnSave(Event event) throws InterruptedException {
//		if (tbNewPass.getText().trim().length() == 0) {
//			Messagebox.show("Nowe hasło nie może być puste!");
//			return;
//		}

		closeWindow();
	}
	
	public static void showDialog() throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//map.put("student", student);

		// call the zul-file with the parameters packed in a map
		try {
			Executions.createComponents("/secure/admin/courseChoicePanel.zul", null, map);
		} catch (final Exception e) {
			logger.error("onOpenWindow:: error opening window / " + e.getMessage());

			// Show a error box
			String msg = e.getMessage();
			String title = Labels.getLabel("messag.Error");

			MultiLineMessageBox.doSetTemplate();
			MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "ERROR", true);
		}
	}
	
}
