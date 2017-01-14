package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.web.ui.util.BaseCtrl;
import pl.wd.kursy.web.ui.util.WebUtil;

public class LoginCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = -1726615296715627542L;

	private static final Logger logger = Logger.getLogger(LoginCtrl.class);

	protected Window loginWindow; // autowired
	protected Textbox tbPass; // aurowired
	protected Textbox tbUser; // aurowired
	protected Combobox cmbCourse; // aurowired

	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners

			setupComponents();
			initData();
			
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

		loginWindow.doModal(); // open the dialog in modal mode
	}

	public void onClick$btnLogin(Event event) throws InterruptedException {
		try {
			Executions.sendRedirect(
					"j_spring_security_check?j_username=" + tbUser.getText() + " &j_password=" + tbPass.getText());
		} catch (final Exception e) {
		}
	}

	private void setupComponents() throws Exception {
		WebUtil.addCbItemRenderer(cmbCourse);
	}
	
	private void initData() throws Exception {
		List<Course> coursesDb = getUserWorkspace().getDataServiceProvider().getCourses();
		List<Course> courses = new ArrayList<Course>();
		if ( coursesDb.size() > 1 ) {
			Course c = new Course();
			courses.add(c);
		}
		
		courses.addAll(coursesDb);

		ListModel<Course> courseModel = new ListModelArray<Course>(courses);
		cmbCourse.setModel(courseModel);

		if ( coursesDb.size() == 1 ) {
			WebUtil.selectCombo(cmbCourse, coursesDb.get(0));
		}
	}

}
