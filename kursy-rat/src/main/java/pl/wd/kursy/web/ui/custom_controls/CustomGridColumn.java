package pl.wd.kursy.web.ui.custom_controls;

import org.zkoss.zul.Column;

import pl.wd.kursy.web.ui.interf.GridColumnCallbackInt;

public class CustomGridColumn extends Column {
	private static final long serialVersionUID = 1394413119443168751L;
	
	private boolean _accesible = true;
	private GridColumnCallbackInt _callbackInt;

	@Override
	protected void updateByClient( String paramName, Object value ) {
		String id = getId();
		if ( "visible".equals(paramName) ) {
			this.setVisible((Boolean) value);
			if ( _callbackInt != null ) {
				_callbackInt.updateByClient(paramName, value, id);
			}
		} else {
			super.updateByClient(paramName, value);
		}
	}

	public boolean isAccesible() {
		return _accesible;
	}

	public void setAccesible( boolean accesible ) {
		_accesible = accesible;
	}

	public void setCallbackInt( GridColumnCallbackInt callbackInt ) {
		_callbackInt = callbackInt;
	}

}
