package pl.wd.kursy.web.ui.custom_controls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import pl.wd.kursy.data.BasicType;

public class ChoiceListbox extends Listbox {
	private static final long serialVersionUID = 2489666891045070661L;

	private boolean _readOnly = false;

	public void clearSelection() {
		List<Listitem> items = getItems();
		for (Listitem listItem : items) {
			listItem.setSelected(false);
		}
	}
	
	public List<BasicType> getSelItems() {
		Set<Listitem> selItems = getSelectedItems();
		List<BasicType> retList = new ArrayList<BasicType>(); 
		for (Listitem listitem : selItems) {
			retList.add((BasicType)listitem.getValue());
		}
		return retList;
	}
	
	public List<Integer> getSelectedIDs() {
		List<BasicType> selItems = getSelItems();
		List<Integer> selIDs = new ArrayList<Integer>(); 
		for (BasicType item : selItems) {
			selIDs.add( item.get_id() );
		}

		return selIDs;
	}
	
	public <T extends BasicType> void setSelectedElements( Set<T> selected ) {
		Set<Integer> hsSelIds = new HashSet<Integer>();
		for( BasicType item : selected ) {
			hsSelIds.add( item.get_id() );
		}
		setSelectedIds( hsSelIds );
	}

	public void setSelectedIds( Set<Integer> hsSelIds ) {
		if ( hsSelIds == null ) {
			return;
		}
		Set<Listitem> selItems = new HashSet<Listitem>();

		ListModelList<BasicType>  modelCL = (ListModelList<BasicType>)(ListModelList<?>)this.getModel();
		for (int i = 0; i < modelCL.size(); i++) {
			BasicType item = modelCL.get(i); 
			if ( hsSelIds.contains( item.get_id() ) ) {
				item.set_selected( true );
				if ( getItemCount() > 0 ) {
					selItems.add(getItemAtIndex(i));
				}
			}
			else {
				item.set_selected( false );
			}
		}
		if ( selItems.size() > 0 ) {
			this.setSelectedItems(selItems);
		}
		
	}
	
	public void onSelect( Event event  ) {
		if ( _readOnly ) {
			preventChangeSelection( event  );
		}
	}
	
	public void preventChangeSelection( Event event  ) {
		if (event instanceof SelectEvent) {
				SelectEvent selectEvent = (SelectEvent)event;
				if ( selectEvent.getReference() instanceof Listitem ) {
					Listitem item = (Listitem)selectEvent.getReference();
					BasicType bt = (BasicType)item.getValue();
					item.setSelected(bt.is_selected());
				}
			}
	}
	
	public void setReadOnly(boolean readOnly) {
		_readOnly = readOnly;
	}


}
