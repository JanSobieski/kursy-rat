package pl.wd.kursy.web.ui.renderer;

import java.io.Serializable;

import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import pl.wd.kursy.data.Exercise;

public class ExerciseListItemRenderer implements ListitemRenderer<Exercise>, Serializable {
	private static final long serialVersionUID = -2951051558613728089L;

	@Override
	public void render(Listitem item, Exercise data, int index) throws Exception {
		Listcell lc = new Listcell(data.getName() );
		lc.setStyle("text-align:left");
		lc.setParent(item);
		
		item.setValue(data);
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onExerciseListItemDoubleClicked");
	}

}
