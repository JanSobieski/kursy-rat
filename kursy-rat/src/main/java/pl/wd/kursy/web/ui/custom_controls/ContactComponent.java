package pl.wd.kursy.web.ui.custom_controls;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.East;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import pl.wd.kursy.web.ui.WebConstants;

public class ContactComponent {
	public static final String LINE_PREFIX = "blLine";
	public static final String BUT = "but";

	private boolean _readOnly = false;
	private String _prefix;
	private int freeID = 1;
	private Vbox _vBox;

	public ContactComponent(Vbox vbPhoneBox, String prefixAdd) {
		_vBox = vbPhoneBox;
		_prefix = LINE_PREFIX + prefixAdd;
	}

	public Textbox addLine() {
		Borderlayout borderLayout = new Borderlayout();
		borderLayout.setHflex("1");
		borderLayout.setVflex("min");
		borderLayout.setId(_prefix + Integer.toString(freeID));
		_vBox.appendChild(borderLayout);

		Center center = new Center();
		center.setBorder("none");
		center.setVflex("min");
		Textbox tb = new Textbox();
		tb.setWidth("100%");
		tb.setInstant(true);
		setReadOnly(tb, _readOnly);
		center.appendChild(tb);
		East east = new East();
		east.setBorder("none");
		east.setVflex("min");
		Button btDel = new Button();
		btDel.setLabel("-");
		btDel.setWidth("30px");
		btDel.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) throws Exception {
				removeLine(event);
			}
		});
		btDel.setId(_prefix + BUT + Integer.toString(freeID));
		btDel.setDisabled(_readOnly);

		east.appendChild(btDel);
		borderLayout.appendChild(east);
		borderLayout.appendChild(center);
		_vBox.invalidate();
		freeID++;

		return tb;
	}

	private void removeLine(Event event) {
		int id = decodeId(event.getTarget().getId());

		List<Component> list = _vBox.getChildren();
		if (list.size() > 0) {
			Component blLine = list.get(0).getFellowIfAny(_prefix + Integer.toString(id));
			if (blLine != null) {
				_vBox.removeChild(blLine);
			}
		}
	}

	public void clear() {
		_vBox.getChildren().clear();
	}

	private int decodeId(String sId) {
		int id = 0;
		String prefix = _prefix + BUT;
		if (sId.startsWith(prefix)) {
			try {
				id = Integer.parseInt(sId.substring(prefix.length()));
			} catch (Exception e) {
			}
		}

		return id;
	}

	public void setReadOnly(Textbox tb, boolean readOnly) {
		if (readOnly) {
			tb.setReadonly(true);
			tb.setSclass(WebConstants.SCLASS_READ_ONLY_TRUE);
		} else {
			tb.setReadonly(false);
			tb.setSclass(WebConstants.SCLASS_READ_ONLY_FALSE);
		}
	}

	public void setReadOnly(boolean readOnly) {
		_readOnly = readOnly;
		List<Component> list = _vBox.getChildren();
		for (Component component : list) {
			List<Component> list2 = component.getChildren();
			for (Component component2 : list2) {
				if (component2 instanceof Center) {
					Textbox tb = (Textbox) component2.getChildren().get(0);
					setReadOnly(tb, readOnly);
				}
				if (component2 instanceof East) {
					Button btDel = (Button) component2.getChildren().get(0);
					btDel.setDisabled(readOnly);
				}
			}
		}
	}

	public List<String> getValues() {
		List<String> values = new ArrayList<String>();

		List<Component> list = _vBox.getChildren();
		for (Component component : list) {
			List<Component> list2 = component.getChildren();
			for (Component component2 : list2) {
				if (component2 instanceof Center) {
					Textbox tb = (Textbox) component2.getChildren().get(0);
					if (tb.getText().trim().length() > 0)
						values.add(tb.getText().trim());
				}
			}
		}

		return values;
	}

}
