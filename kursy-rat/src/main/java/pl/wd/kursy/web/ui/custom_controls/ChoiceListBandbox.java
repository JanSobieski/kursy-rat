package pl.wd.kursy.web.ui.custom_controls;

import java.util.HashSet;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;

import pl.wd.kursy.data.interf.BasicTypeIntf;
import pl.wd.kursy.web.ui.interf.ChoiceDialogInt;
import pl.wd.kursy.web.ui.model.ChoicePopupListViewModel;
import pl.wd.kursy.web.ui.renderer.ChoiceGridModelItemRenderer;
import pl.wd.kursy.web.ui.util.BaseCtrl;

/**
 * 
 * @author witolddrozdzynski
 *
 */
public class ChoiceListBandbox extends Bandbox {

	private static final long serialVersionUID = 4427513769977385653L;
	
	public final static String BUTTON_CANCEL_PREFIX = "btnCancel";
	public final static String BUTTON_OK_PREFIX = "btnOk";
	
	protected Grid _grid;
	protected ChoicePopupListViewModel _model;
	protected Button _btnCancelPopup;
	protected Button _btnOkPopup;
	protected ChoiceDialogInt _dialogInt;
	protected BaseCtrl _controller;
	
	public void init( ChoicePopupListViewModel model,ChoiceDialogInt dialogInt, BaseCtrl controller ) {
		_model = model;
		_dialogInt = dialogInt;
		_controller = controller;
		Bandpopup bp = findBandpopup( this );
		_grid = findGrid( bp );
		if ( _grid != null ) {
			_grid.setRowRenderer(new ChoiceGridModelItemRenderer<BasicTypeIntf>( _controller ));
			_grid.setModel(_model);
		}
		_btnCancelPopup = findCancel( this, BUTTON_CANCEL_PREFIX );
		_btnOkPopup = findCancel( this, BUTTON_OK_PREFIX );
		this.addEventListener(Events.ON_OPEN, new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				OpenEvent openEvent = (OpenEvent)event;
				if( !openEvent.isOpen()) {
					onClick_btnCancel();
				}
			}
		});		
		if ( _btnCancelPopup != null ) {
			_btnCancelPopup.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					onClick_btnCancel();
				}
			});		
		}
		if ( _btnOkPopup != null ) {
			_btnOkPopup.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					_model.storeSelectedIds();
					setValue( _model.getSelectedItemsStr() );
					close();
					if ( _dialogInt != null ) {
						_dialogInt.onOkClose();
					}
				}
			});		
		}
	}
	
	private Bandpopup findBandpopup( Component comp ) {
		for( Component child : comp.getChildren() ) {
			if ( child instanceof Bandpopup ) {
				return (Bandpopup)child;
			}
			Bandpopup bp = findBandpopup( child );
			if ( bp != null ) {
				return bp;
			}
		}
		return null;
	}

	private Grid findGrid( Component comp ) {
		for( Component child : comp.getChildren() ) {
			if ( child instanceof Grid ) {
				return (Grid)child;
			}
			Grid grid = findGrid( child );
			if ( grid != null ) {
				return grid;
			}
		}
		return null;
	}
	
	private Button findCancel( Component comp, String prefix ) {
		for( Component child : comp.getChildren() ) {
			if ( ( child instanceof Button ) && child.getId().startsWith(prefix ) ) {
				return (Button)child;
			}
			Button button = findCancel( child, prefix );
			if ( button != null ) {
				return button;
			}
		}
		return null;
	}
	
	public Grid getGrid() {
		return _grid;
	}

	public void onClick_btnCancel() {
		close();
		_model.restoreSelectedIds();
		renewSelection();
	}
	
	public void renewSelection() {
		try {
			_grid.setModel(new ChoicePopupListViewModel());
		} catch (Exception e) {
		}
		_grid.setModel(_model);
	}
	
	public void clear() {
		setValue("");
		_model.setSelectedIds(new HashSet<Integer>()  ); 
		_model.storeSelectedIds();
		renewSelection();
	}

}
