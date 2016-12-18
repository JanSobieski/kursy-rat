package pl.wd.kursy.web.ui.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.wd.web.util.Constants;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.web.ui.WebConstants;

public class WebUtil {
	static public void selectCombo(Combobox cmb, int id2Select) {
		for (int i = 0; i < cmb.getModel().getSize(); i++) {
			BasicType item = (BasicType) cmb.getModel().getElementAt(i);
			item.set_selected(false);
			if (item.get_id() == id2Select) {
				item.set_selected(true);
				if ( cmb.getItemCount() > 0  ) {
					cmb.setSelectedIndex(i);
				}
			}
		}
	}
	
	static public void selectCombo(Combobox cmb, BasicType toSelect) {
		if (toSelect == null) {
			return;
		}
		selectCombo(cmb, toSelect.get_id());
	}

	
	static public Object  getCmbValue( final Combobox combo ) {
		if ( combo.getSelectedItem() == null ) {
			return null;
		}
		
		return combo.getSelectedItem().getValue();
	}
	
	static public void addCbItemRenderer( final Combobox combo ) {
		combo.setItemRenderer(new ComboitemRenderer<BasicType>() {
			@Override
			public void render(Comboitem item, BasicType data, int index) throws Exception {
				item.setLabel(data.getName());
				item.setValue(data);
				if (data.is_selected()) {
					combo.setSelectedItem(item);
				}
			}
		});
	}

	static public void setTbReadOnly(Textbox tb ) {
		tb.setReadonly(true);
		tb.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);
	}

	static public void setIbReadOnly(Intbox ib ) {
		ib.setReadonly(true);
		ib.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);
	}

	static public void setTbEditable(Textbox tb ) {
		tb.setReadonly(false);
		tb.setSclass(WebConstants.SCLASS_READ_ONLY_FALSE);
	}

	static public void setIbEditable(Intbox ib ) {
		ib.setReadonly(false);
		ib.setSclass(WebConstants.SCLASS_READ_ONLY_FALSE);
	}

	static public void setDbReadOnly(Datebox db ) {
		db.setDisabled(true);
		db.setReadonly(true);
		db.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);
	}

	static public void setDbEditable(Datebox db ) {
		db.setDisabled(false);
		db.setReadonly(false);
		db.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);
	}

	static public void setDateTime(Date date, Datebox db, Textbox tbHour, Textbox tbMinute ) {
		if (date == null) {
			return;
		}
		db.setValue(date);

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date.getTime());
		String hm = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		if (hm.length() < 2) {
			hm = "0" + hm;
		}
		tbHour.setText(hm);
		hm = Integer.toString(cal.get(Calendar.MINUTE));
		if (hm.length() < 2) {
			hm = "0" + hm;
		}
		tbMinute.setText(hm);
	}
	
	static public Date  getDateTime(Datebox db, Textbox tbHour, Textbox tbMinute ) {
		Date date = db.getValue();
		if ( date == null ) {
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		try {
			int hour = Integer.parseInt(tbHour.getText() );
			cal.set(Calendar.HOUR_OF_DAY, hour);
			int min = Integer.parseInt(tbMinute.getText() );
			cal.set(Calendar.MINUTE, min);
		} catch (Exception e) {
		}
		
		return cal.getTime();
	}
	
	static public void onListItemClicked(Event event, boolean selectOnlyNewRows ) throws Exception {
		Event origin = null;
		Row row = null;
		Checkbox cbSelected = null;
		Grid parentGrid = null;
		// get event target
		if (event instanceof ForwardEvent) {
			origin = Events.getRealOrigin((ForwardEvent)event);
			Component target = origin.getTarget();
			parentGrid = (Grid)target.getParent().getParent();
			row = (Row)target;
			
			Component comp = row.getChildren().get(0);
			if ( comp instanceof Checkbox ) {
				cbSelected = (Checkbox)comp;
			}
		} else {
			origin = event;
			cbSelected = (Checkbox)origin.getTarget();
			row = (Row)cbSelected.getParent();
			parentGrid = (Grid)cbSelected.getParent().getParent().getParent();
		}
		if ( ( origin != null ) && ( cbSelected != null ) ) {
			BasicType value = row.getValue();
			value.set_selected(cbSelected.isChecked());
			
			if (cbSelected.isChecked()) {
				unselectRows( parentGrid, value );
			}			
		}
	}
			
	static private void unselectRows( Grid parentGrid, BasicType selValue ) {
		for( Component row : parentGrid.getRows().getChildren() ) {
			BasicType value = ((Row)row).getValue();
			if ( ( value != null ) && (value.get_id() != selValue.get_id() ) ) {
				value.set_selected(false);
				Component comp = row.getChildren().get(0);
				if ( comp instanceof Checkbox ) {
					Checkbox cbSelected = (Checkbox)comp;
					cbSelected.setChecked(false);
				}
			}
		}
	}
	 
	static public void closeTab(String id) {
		final Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
		/* get an instance of the searched CENTER layout area */
		final Center center = bl.getCenter();

		final Tabs tabs = (Tabs) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter");
		final Tabpanels tabpanels = (Tabpanels) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter").getFellow("tabpanelsBoxIndexCenter");
		try {
			Tab checkTab = (Tab) tabs.getFellow(Constants.TAB_PREFIX + id );
			tabs.removeChild(checkTab);
			final Tabpanel tabpanel = (Tabpanel)tabpanels.getFellow(Constants.TABPANEL_PREFIX + id ); 
			tabpanels.removeChild(tabpanel);
			if ( tabs.getChildren().size() > 0 ) {
				((Tab)tabs.getChildren().get(0)).setSelected(true);
			}
		} catch (final ComponentNotFoundException ex) {
			// Ignore if can not get tab.
		}

	}
	
	static public String getTabId(Window window) {
		final Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
		final Center center = bl.getCenter();
		final Tabpanels tabpanels = (Tabpanels) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter").getFellow("tabpanelsBoxIndexCenter");
		String ret = "";
		
		for (Component comp : tabpanels.getChildren() ) {
			final Tabpanel tabpanel = (Tabpanel)comp;
			if ( (tabpanel .getChildren().size() == 1 ) && ( tabpanel .getChildren().get(0).equals(window) )) {
				ret = tabpanel .getId();
				if ( ret.startsWith(Constants.TABPANEL_PREFIX )) {
					ret = ret.substring(Constants.TABPANEL_PREFIX .length());
				}
				return ret;
			}
		}
		
		return ret;
	}
	

}
