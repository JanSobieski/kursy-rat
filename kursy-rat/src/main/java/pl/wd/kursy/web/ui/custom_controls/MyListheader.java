package pl.wd.kursy.web.ui.custom_controls;

import org.zkoss.zul.Listheader;

public class MyListheader extends Listheader {

	public MyListheader() {
	}

	@Override
	protected void updateByClient( String name, Object value ) {
		if ( "visible".equals(name) ) {
			this.setVisible((Boolean) value);
		} else {
			super.updateByClient(name, value);
		}
	}

}
