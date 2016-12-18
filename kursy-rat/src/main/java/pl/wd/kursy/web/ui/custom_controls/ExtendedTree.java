package pl.wd.kursy.web.ui.custom_controls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.web.ui.tree.ExtTreeNode;

public class ExtendedTree extends Tree {
	private static final long serialVersionUID = 1772248916206369674L;

	private boolean _readOnly = false;

	public ExtendedTree() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<BasicType> getSelectedItemsExt() {
		List<BasicType> selItems = new ArrayList<BasicType> ();
		Collection<Treeitem> items = getItems() ;
		for (Treeitem treeitem : items) {
			if ( treeitem.isSelected() && ( treeitem.getValue() != null ) ) {
				selItems.add( (BasicType)((ExtTreeNode<BasicType>)treeitem.getValue()).getData() );
			}
		}
		
	return selItems;
	}
	
	public void clearSelection() {
		Collection<Treeitem> items = getItems() ;
		for (Treeitem treeitem : items) {
			treeitem.setSelected(false);
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
				if ( selectEvent.getReference() instanceof Treeitem ) {
					Treeitem item = (Treeitem)selectEvent.getReference();
					ExtTreeNode val = (ExtTreeNode)item.getValue();
					BasicType bt = (BasicType)val.getData();
					item.setSelected(bt.is_selected());
				}
			}
	}
	
	public void setReadOnly(boolean readOnly) {
		_readOnly = readOnly;
	}
		
	
}
