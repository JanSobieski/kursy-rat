package pl.wd.kursy.data.comp;

public class BaseComparator {
	protected boolean _asc = true;
	protected int _type = 0;

	public final static int TYPE_ID = 1;
	public final static int TYPE_NAME = 2;
	public final static int TYPE_ORDER = 3;

	
	//protected DataReloadIndicator _reloadIndicator = null;

	//protected ComparatorCallbackInt _callbackInt = null;
	
	public BaseComparator(boolean asc, int type) {
		_asc = asc;
		_type = type;
	}
	
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
	
//	public void setCallbackInt( ComparatorCallbackInt callbackInt ) {
//		_callbackInt = callbackInt;
//	}
//
//	public DataReloadIndicator getReloadIndicator() {
//		return _reloadIndicator;
//	}
//
//	public void setReloadIndicator( DataReloadIndicator reloadIndicator ) {
//		_reloadIndicator = reloadIndicator;
//	}
}
