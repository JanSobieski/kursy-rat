package pl.wd.kursy.web.ui.report;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import pl.wd.kursy.web.ui.admin.CourseChoiceCtrl;
import pl.wd.kursy.web.ui.interf.ChoiceDialogInt;
import pl.wd.kursy.web.ui.model.MissingSkillsGridModel;
import pl.wd.kursy.web.ui.renderer.MissingSkillItemRenderer;
import pl.wd.kursy.web.ui.util.BaseCtrl;
import pl.wd.kursy.web.ui.util.WebUtil;

public class MissingSkillsReportCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = 6491004979053960584L;

	private static final Logger logger = Logger.getLogger(MissingSkillsReportCtrl.class);
	
	protected Window 			missingSkillsWindow; // autowired
	protected Grid 				gridMissingSkills; // autowired
	
	private MissingSkillsGridModel _model;
	

	/**
	 * default constructor.<br>
	 */
	public MissingSkillsReportCtrl() {
		super();
	}

	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners

			gridMissingSkills.setRowRenderer(new MissingSkillItemRenderer());
			
		    ChoiceDialogInt choiceDialogInt = new ChoiceDialogInt() {
				@Override
				public void onOkClose() {
					try {
//						setupComponents();
						initData();
						
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
		    };
		    if ( CourseChoiceCtrl.checkCourse( getUserWorkspace(), WebUtil.getTabId(missingSkillsWindow),  choiceDialogInt ) ) {
//				setupComponents();
				initData();
		    }
			
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
	
	private void initData() throws Exception {
		_model = new MissingSkillsGridModel(getUserWorkspace()); 
		
		gridMissingSkills.setModel(_model);

	}
	

}
