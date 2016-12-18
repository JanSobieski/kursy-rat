package pl.wd.kursy.web.ui.renderer;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.web.ui.util.BaseCtrl;

public class ChoiceGridModelItemRenderer<T> implements RowRenderer<BasicType>, Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ChoiceGridModelItemRenderer.class);
	private BaseCtrl _controller;

	public ChoiceGridModelItemRenderer(BaseCtrl controller ) {
		_controller = controller;
	}

	@Override
	public void render( Row row, BasicType data, int index ) throws Exception {
		Hlayout hl = new Hlayout();
		hl.setStyle("text-align:left");
		row.appendChild(hl);
		Checkbox cb = new Checkbox();
		cb.addEventListener(Events.ON_CHECK, new EventListener<Event>() {
      public void onEvent( Event event ) throws Exception {
      	_controller.onChoiceItemClicked(event);
      }
    });
		
		cb.setChecked(data.is_selected());
		hl.appendChild(cb);
		Label lb = new Label(" " + data.getName());
		lb.setStyle("text-align:left");
		hl.appendChild(lb);

		row.setValue(data);

		ComponentsCtrl.applyForward(row, "onClick=onChoiceItemClicked");
	}
}
