package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.wd.web.util.Constants;
import org.wd.web.util.MessageUtils;
import org.wd.web.util.MultiLineMessageBox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.User;
import pl.wd.kursy.exception.BusinessLogicException;
import pl.wd.kursy.misc.Util;
import pl.wd.kursy.web.ui.WebConstants;
import pl.wd.kursy.web.ui.admin.model.UserDialogViewModel;
import pl.wd.kursy.web.ui.util.BaseCtrl;

public class UserDialogCtrl extends BaseCtrl implements Serializable {

	private static final long serialVersionUID = -5769173296358420487L;
	private transient static final Logger logger = Logger.getLogger(UserDialogCtrl.class);
	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding component
	 * with the same 'id' in the zul-file are getting autowired by our 'extends
	 * GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	private UserDialogViewModel _model;

	private transient String oldVar_usrLogin;
	private transient boolean oldVar_usrActive;
	private transient String oldVar_usrPerson;

	// overhanded vars per params
	private transient Listbox _listBoxUser; // overhanded
	private transient User _user; // overhanded

	protected Window userDialogWindow; // autowired

	// panel account details
	protected Textbox usrLogin; // autowired
	protected Textbox usrDescr; // autowired
	protected Textbox usrPassword; // autowired
	protected Checkbox usrAdmin; // autowired

	// Button controller for the CRUD buttons
	protected Button btnNew; // autowired
	protected Button btnEdit; // autowired
	protected Button btnDelete; // autowired
	protected Button btnSave; // autowired
	protected Button btnCancel;// autowired
	protected Button btnClose; // autowired

	public UserDialogCtrl() {
		super();
	}
	
	public void doAfterCompose(Window comp) {
		try {
	    super.doAfterCompose(comp); //wire variables and event listners

			try {
				_model = new UserDialogViewModel(_userWorkspace);

				// get the params map that are overhanded by creation.
				//Map<String, Object> args = getCreationArgsMap(event);
		    @SuppressWarnings("unchecked")
				Map<String, Object> args = (Map<String, Object>)Executions.getCurrent().getArg();

				if ( args.containsKey("user") ) {
					setUser((User) args.get("user"));
				} else {
					setUser(new User());
				}

				// we get the listBox Object for the users list. So we have access
				// to it and can synchronize the shown data when we do insert, edit or
				// delete users here.
				if ( args.containsKey("listBoxUsers") ) {
					_listBoxUser = (Listbox) args.get("listBoxUsers");
				} else {
					_listBoxUser = null;
				}
			} catch (Throwable e) {
				logger.error("Error", e);
			} finally {
				getUserWorkspace().getDataServiceProvider().closeDbSession();
			}

			doShowDialog(getUser());
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
	public void doShowDialog( User user ) throws InterruptedException {
		if ( user == null ) {
			/** !!! DO NOT BREAK THE TIERS !!! */
			// We don't create a new DomainObject() in the frontend.
			// We GET it from the backend.
			// user = getUserService().getNewUser();
		}

		// set Readonly mode accordingly if the object is new or not.

		if ( user.getId() == 0 ) {
			doEdit();
		} else {
			// btnCtrl.setInitEdit();
			doReadOnly();
		}

		try {
			// fill the components with the data
			doWriteBeanToComponents(user);
			doStoreInitValues();
			usrLogin.setFocus(true);

			userDialogWindow.doModal(); // open the dialog in modal mode
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}
	}

	/**
	 * Writes the bean data to the components.<br>
	 * 
	 * @param anUser
	 */
	public void doWriteBeanToComponents( User user ) {
		usrLogin.setValue(user.getLogin());
		usrDescr.setValue(user.getDescription());
		usrAdmin.setChecked(user.isAdmin());
	}
	
	public void doWriteComponentsToBean(User user) throws Exception {
		user.setLogin(usrLogin.getValue());
		user.setDescription(usrDescr.getValue());
		user.setAdmin( usrAdmin.isChecked() );
		if ( usrPassword.getValue().length() > 0 ) {
			try {
				user.setPass(Util.encode_password(usrPassword.getValue()));
			} catch (Exception e) {
				logger.error("Error", e);
			}
		}
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
		_user = new User();
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
		userDialogWindow.onClose();
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
		doWriteComponentsToBean(_user);

		/* if a language is selected get the object from the listbox */

		// save it to database
		try {
			if ( _user.getLogin().length() == 0 ) {
				MessageUtils.showErrorMessage("Prosze podac login");
				return false;
			}
			if ( _user.getId() == 0 ) {
				if ( _user.getPass().length() == 0 ) {
					MessageUtils.showErrorMessage("Prosze podac haslo");
					return false;
				}
			}
			
			_model.saveOrUpdate(_user);
			doReadOnly();
			doStoreInitValues();
			
			ListModelList<User> lml = (ListModelList<User>)(ListModelList<?>) _listBoxUser.getListModel();

			// Check if the object is new or updated
			// -1 means that the obj is not in the list, so it's new.
			if (lml.indexOf(_user) == -1) {
				lml.add(_user);
			} else {
				lml.set(lml.indexOf(_user), _user);
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
		oldVar_usrLogin = usrLogin.getValue();
		oldVar_usrPerson = usrDescr.getValue();
		oldVar_usrActive = usrAdmin.isChecked();

	}

	/**
	 * Resets the init values from mem vars. <br>
	 */
	private void doResetInitValues() {
		usrLogin.setValue(oldVar_usrLogin);
		usrDescr.setValue(oldVar_usrPerson);
		usrAdmin.setChecked(oldVar_usrActive);

	}

	/**
	 * Checks, if data are changed since the last call of <br>
	 * doStoreInitData() . <br>
	 * 
	 * @return true, if data are changed, otherwise false
	 */
	private boolean isDataChanged() {
		boolean changed = false;

		if ( oldVar_usrLogin != usrLogin.getValue() ) {
			return true;
		}
		if ( oldVar_usrActive != usrAdmin.isChecked() ) {
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


		usrLogin.setReadonly(false);
		usrLogin.setSclass(WebConstants.SCLASS_READ_ONLY_FALSE);
		usrDescr.setReadonly(false);
		usrDescr.setSclass(WebConstants.SCLASS_READ_ONLY_FALSE);
		usrPassword.setReadonly(false);
		usrPassword.setSclass(WebConstants.SCLASS_READ_ONLY_FALSE);
		usrAdmin.setDisabled(false);
		usrAdmin.setSclass(WebConstants.SCLASS_READ_ONLY_FALSE);
		
		btnSave.setDisabled(false);
		btnSave.invalidate();
		btnEdit.setDisabled(true);
		btnEdit.invalidate();


		usrLogin.focus();

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
		
		usrLogin.setReadonly(true);
		usrLogin.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);
		usrDescr.setReadonly(true);
		usrDescr.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);
		usrPassword.setReadonly(true);
		usrPassword.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);
		usrAdmin.setDisabled(true);
		usrAdmin.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);

		_readOnly = true;
	}

	/**
	 * Clears the components values. <br>
	 */
	public void doClear() {
		usrLogin.setValue("");
		usrDescr.setValue("");
		usrAdmin.setChecked(false);
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	// ++++++++++++++++++ getter / setter +++++++++++++++++++//
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

	public User getUser() {
		return _user;
	}

	public void setUser( User user ) {
		_user = user;
	}

}
