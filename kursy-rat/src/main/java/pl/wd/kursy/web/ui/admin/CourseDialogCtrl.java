package pl.wd.kursy.web.ui.admin; 

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.wd.web.util.MessageUtils;
import org.wd.web.util.MultiLineMessageBox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.data.Student;
import pl.wd.kursy.exception.BusinessLogicException;
import pl.wd.kursy.web.ui.WebConstants;
import pl.wd.kursy.web.ui.admin.model.CourseDialogViewModel;
import pl.wd.kursy.web.ui.admin.model.StudentDialogViewModel;
import pl.wd.kursy.web.ui.util.BaseCtrl;

public class CourseDialogCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = -7287101962808717615L;

	private transient static final Logger logger = Logger.getLogger(CourseDialogCtrl.class);

	private CourseDialogViewModel _model;
	
	final static String COURSE = "course";

	private transient String _oldVar_Name;

	// overhanded vars per params
	private transient Listbox _listBoxCourse; // overhanded
	private transient Course _course; // overhanded

	protected Window courseDialogWindow; // autowired

	// panel account details
	protected Textbox tbName; // autowired

	// Button controller for the CRUD buttons
	protected Button btnNew; // autowired
	protected Button btnEdit; // autowired
	protected Button btnDelete; // autowired
	protected Button btnSave; // autowired
	protected Button btnCancel;// autowired
	protected Button btnClose; // autowired

	public CourseDialogCtrl() {
		super();
	}
	
	public void doAfterCompose(Window comp) {
		try {
	    super.doAfterCompose(comp); //wire variables and event listners

			try {
				_model = new CourseDialogViewModel(_userWorkspace);

				@SuppressWarnings("unchecked")
				Map<String, Object> args = (Map<String, Object>) Executions.getCurrent().getArg();

				if (args.containsKey(COURSE)) {
					setCourse((Course) args.get(COURSE));
				} else {
					setCourse(new Course());
				}

				if ( args.containsKey(CourseListCtrl.LIST_BOX_COURSES) ) {
					_listBoxCourse = (Listbox) args.get(CourseListCtrl.LIST_BOX_COURSES);
				} else {
					_listBoxCourse = null;
				}
			} catch (Throwable e) {
				logger.error("Error", e);
			} finally {
				getUserWorkspace().getDataServiceProvider().closeDbSession();
			}

			doShowDialog(getCourse());
		} catch (Exception e) {
			logger.error(e);
			Messagebox.show(e.toString());
		}
	}
	

	/**
	 * Before binding the data and calling the dialog window we check, if the
	 * zul-file is called with a parameter for a selected user object in a Map.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreate$userDialogWindow( Event event ) throws Exception {
	}

	/**
	 * Opens the Dialog window modal.
	 * 
	 * It checks if the dialog opens with a new or existing object and set the
	 * readOnly mode accordingly.
	 * 
	 * @param user
	 * @throws InterruptedException
	 */
	public void doShowDialog( Course course ) throws InterruptedException {
		if ( course == null ) {
			/** !!! DO NOT BREAK THE TIERS !!! */
		}

		// set Readonly mode accordingly if the object is new or not.

		if ( course.getId() == 0 ) {
			doEdit();
		} else {
			// btnCtrl.setInitEdit();
			doReadOnly();
		}

		try {
			// fill the components with the data
			doWriteBeanToComponents(course);
			doStoreInitValues();
			tbName.setFocus(true);

			courseDialogWindow.doModal(); // open the dialog in modal mode
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}
	}

	/**
	 * Writes the bean data to the components.<br>
	 */
	public void doWriteBeanToComponents( Course course ) {
		tbName.setValue(course.getName());
	}
	
	public void doWriteComponentsToBean(Course course) throws Exception {
		course.setName(tbName.getValue());
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++ Components events +++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * If we close the dialog window. <br>
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClose$userDialogWindow( Event event ) throws Exception {
		doClose(event);
	}

	/**
	 * when the "save" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnSave( Event event ) throws InterruptedException {
		try {
			doSave();
			doClose(event);
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	/**
	 * when the "edit" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnEdit( Event event ) {
		// logger.debug(event.toString());
		doEdit();
	}

	/**
	 * when the "new" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnNew( Event event ) {
		_course = new Course();
		doClear();
		doEdit(); // edit mode
	}

	/**
	 * when the "cancel" button is clicked. <br>
	 * 
	 * @param event
	 */
	public void onClick$btnCancel( Event event ) {
		doCancel();
	}

	/**
	 * when the "close" button is clicked. <br>
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnClose( Event event ) throws InterruptedException {
		try {
			doClose(event);
		} catch (final Exception e) {
			logger.error("Error", e);
		}
	}


	/**
	 * Closes the dialog window. <br>
	 * <br>
	 * Before closing we check if there are unsaved changes in <br>
	 * the components and ask the user if saving the modifications. <br>
	 * 
	 */
	private void doClose( Event event ) throws Exception {
		if ( isDataChanged() ) {

			// Show a confirm box
			String msg = Labels.getLabel("message_Data_Modified_Save_Data_YesNo");
			String title = Labels.getLabel("message.Information");

			MultiLineMessageBox.doSetTemplate();
			if ( MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION, true,
					new EventListener() {
						@Override
						public void onEvent( Event evt ) {
							switch( ((Integer) evt.getData()).intValue() ) {
								case MultiLineMessageBox.YES:
									try {
										if ( doSave() ) {
											closeWindow();
										}
									} catch (final Exception e) {
										throw new RuntimeException(e);
									}
									break;
								case MultiLineMessageBox.NO:
									closeWindow();
									break; //
							}
						}
					}

			) == MultiLineMessageBox.YES ) {
			}
		}
		else {
			closeWindow();
		}
	}
	
	private void closeWindow() {
		//unlockRecord(Constants.TABLE_USERS, _user.get_id());
		courseDialogWindow.onClose();
	}

	/**
	 * Cancel the actual operation. <br>
	 * <br>
	 * Resets to the original status.<br>
	 * 
	 */
	private void doCancel() {
		if ( !_readOnly ) {
			//unlockRecord(Constants.TABLE_USERS, _user.get_id());
		}
		doResetInitValues();
		doReadOnly();
	}
	
	public boolean doSave() throws Exception {
		boolean newUser = false;

		// fill the object with the components data
		doWriteComponentsToBean(_course);

		/* if a language is selected get the object from the listbox */

		// save it to database
		try {
			if ( _course.getName().length() == 0 ) {
				MessageUtils.showErrorMessage("Prosze podac nazwę");
				return false;
			}
			
			_model.saveOrUpdate(_course);
			doReadOnly();
			doStoreInitValues();
			
			ListModelList<Course> lml = (ListModelList<Course>)(ListModelList<?>) _listBoxCourse.getListModel();

			// Check if the object is new or updated
			// -1 means that the obj is not in the list, so it's new.
			if (lml.indexOf(_course) == -1) {
				lml.add(_course);
			} else {
				lml.set(lml.indexOf(_course), _course);
			}
			return true;
		} catch (BusinessLogicException e) {
			MessageUtils.showErrorMessage(e.get_description());
		} catch (Exception e) {
			logger.error("Error", e);
			MessageUtils.showErrorMessage(e.toString());
		}
		finally {
			getUserWorkspace().getDataServiceProvider().closeDbSession();
		}
		return false;
	}
	

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++++ helpers ++++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Stores the init values in mem vars. <br>
	 */
	private void doStoreInitValues() {
		_oldVar_Name = tbName.getValue();

	}

	/**
	 * Resets the init values from mem vars. <br>
	 */
	private void doResetInitValues() {
		tbName.setValue(_oldVar_Name);

	}

	/**
	 * Checks, if data are changed since the last call of <br>
	 * doStoreInitData() . <br>
	 * 
	 * @return true, if data are changed, otherwise false
	 */
	private boolean isDataChanged() {
		boolean changed = false;

		if ( _oldVar_Name != tbName.getValue() ) {
			return true;
		}

		return changed;
	}

	/**
	 * Sets the Validation by setting the accordingly constraints to the fields.
	 */
	private void doSetValidation() {
		/*
		 * setValidationOn(true);
		 * 
		 * usrLoginname.setConstraint("NO EMPTY");
		 * usrLastname.setConstraint("NO EMPTY");
		 */
	}

	/**
	 * Set the components for edit mode. <br>
	 */
	private void doEdit() {
//		if ( !lockRecord(Constants.TABLE_USERS, _user.get_id()) ) {
//			return;
//		}


		tbName.setReadonly(false);
		tbName.setSclass(WebConstants.SCLASS_READ_ONLY_FALSE);
		
		btnSave.setDisabled(false);
		btnSave.invalidate();
		btnEdit.setDisabled(true);
		btnEdit.invalidate();


		tbName.focus();

		// remember the old vars
		doStoreInitValues();
	}

	/**
	 * Set the components to ReadOnly. <br>
	 */
	public void doReadOnly() {
		if ( !_readOnly ) {
			//unlockRecord(Constants.TABLE_USERS, _user.get_id());
		}
		btnSave.setDisabled(true);
		btnSave.invalidate();
		
		tbName.setReadonly(true);
		tbName.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);

		_readOnly = true;
	}

	/**
	 * Clears the components values. <br>
	 */
	public void doClear() {
		tbName.setValue("");
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public Course getCourse() {
		return _course;
	}

	public void setCourse( Course course ) {
		_course = course;
	}

}

