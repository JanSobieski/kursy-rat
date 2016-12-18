package pl.wd.kursy.web.data.comp;

import pl.wd.kursy.web.data.comp.helper.DataReloadIndicator;
import pl.wd.kursy.web.ui.interf.ComparatorCallbackInt;

public class BaseComparator {
	protected boolean _asc = true;
	protected int _type = 0;
	protected DataReloadIndicator _reloadIndicator = null;

	protected ComparatorCallbackInt _callbackInt = null;
	
	int compareNull( Object o1, Object o2 ) {
		if ( o1 == null ) {
			if ( o2 == null ) {
				return 0;
			}
			if ( _asc ) {
				return -1;
			}
			else {
				return 1;
			}
		}
		
		if ( _asc ) {
			return 1;
		}
		else {
			return -1;
		}
	}

	public void setCallbackInt( ComparatorCallbackInt callbackInt ) {
		_callbackInt = callbackInt;
	}

	public DataReloadIndicator getReloadIndicator() {
		return _reloadIndicator;
	}

	public void setReloadIndicator( DataReloadIndicator reloadIndicator ) {
		_reloadIndicator = reloadIndicator;
	}
}
