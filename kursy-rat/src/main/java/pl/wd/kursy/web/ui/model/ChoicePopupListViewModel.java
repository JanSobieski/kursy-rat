package pl.wd.kursy.web.ui.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;

import pl.wd.kursy.data.interf.BasicTypeIntf;

public class ChoicePopupListViewModel<E> extends ListModelList<BasicTypeIntf> {
	private static final long serialVersionUID = -5258685497754334531L;
	private static final Logger logger = Logger.getLogger(ChoicePopupListViewModel.class);
	
	private Set<Integer> _selectedIds = new HashSet<Integer>();


	public ChoicePopupListViewModel() {
	}

	public String getSelectedItemsStr() {
		String ret = "";
		for( Iterator<BasicTypeIntf> iterator = iterator(); iterator.hasNext(); ) {
			BasicTypeIntf item = iterator.next();
			if ( item.isSelected() ) {
				if ( ret.length() > 0 ) {
					ret += ", ";
				}
				ret += item.getName();
			}
		}
		
		return ret;
	}
	
	public Set<Integer> getSelectedIds() {
		Set<Integer> ids = new HashSet<Integer>();
		for( Iterator<BasicTypeIntf> iterator = iterator(); iterator.hasNext(); ) {
			BasicTypeIntf item = iterator.next();
			if ( item.isSelected() ) {
				ids.add(item.getId());
			}
		}
		
		return ids;
	}
	
	public void storeSelectedIds() {
		_selectedIds = getSelectedIds();
	}
	
	public void restoreSelectedIds() {
		setSelectedIds(_selectedIds);
	}
	
	public void setSelectedIds(Set<Integer> ids ) {
		for( Iterator<BasicTypeIntf> iterator = iterator(); iterator.hasNext(); ) {
			BasicTypeIntf item = iterator.next();
			if ( ids.contains(item.getId())) {
				item.setSelected(true);
			}
			else {
				item.setSelected(false);
			}
		}
	}
}
