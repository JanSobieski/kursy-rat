package pl.wd.kursy.web.ui.renderer;

import java.io.Serializable;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import pl.wd.kursy.data.BasicType;

public class ChoiceListModelItemRenderer implements ListitemRenderer<BasicType>, Serializable {

	private static final long serialVersionUID = 6214931193032898230L;
	
	@Override
	public void render( Listitem item, BasicType data, int index ) throws Exception {
		Listcell lc;
		lc = new Listcell(data.getName() );
		lc.setStyle("text-align:left");
		lc.setParent(item);
		if ( data.isSelected() ) {
			item.setSelected(true);
		} else {
			item.setSelected(false);
		}
	
		item.setValue(data);

		//item.setAttribute("data", info );
		
		//ComponentsCtrl.applyForward(item, "onDoubleClick=onAttachmentListItemDoubleClicked");
	}

}
