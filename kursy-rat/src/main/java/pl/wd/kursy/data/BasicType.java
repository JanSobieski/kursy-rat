package pl.wd.kursy.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class BasicType implements Serializable,Cloneable {
	private static final long serialVersionUID = -3087968737693385232L;

	protected int _id;
	private boolean _editable;
	private boolean _selected;
	private boolean _selected_neg;
	protected String _name;
	
	public BasicType() {
	}

	public BasicType( int id ) {
		_id = id;
	}

	public int getId() {
		return _id;
	}

	public void setId( int id ) {
		_id = id;
	}
	
	public String getName() {
		if ( _name == null ) {
			return "";
		}
		return _name;
	}

	public void setName( String name ) {
		_name = name;
	}

	@Override
	public int hashCode() {
		if ( _id == 0 ) {
			return super.hashCode();
		}
		
		return _id;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj == null ) {
			return false;
		}
		if ( obj instanceof BasicType ) {
			if ( _id == ((BasicType) obj).getId() ) {
				if ( _id == 0 ) {
					return super.equals( obj );
				}
				return true;
			}
			else
				return false;
		}
		return false;
	}

	public boolean is_selected() {
		return _selected;
	}

	public void set_selected( Object selected ) {
		if ( selected == null ) {
			_selected = false;
			return;
		}
		if ( selected instanceof Boolean ) {
			_selected = ((Boolean)selected).booleanValue();
		}
	}

	public boolean isEditable() {
		return _editable;
	}

	public void setEditable(boolean editable) {
		_editable = editable;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BasicType> List<T> copyList( List<T> list ) throws Exception {
		List<T> listOut = new ArrayList<T>(list.size());
		Class<T>[] cArg = new Class[1];
		
		if ( list.size() > 0 ) {
			cArg[0] = (Class<T>)(Class<?>)list.get(0).getClass();
			for( T item : list ) {
				T bt_item = (T)item.getClass().getDeclaredConstructor(cArg).newInstance( item );
				listOut.add(bt_item);
			}
		}

		return listOut;
	}
	
	public static <T extends BasicType> Hashtable<Integer, T> getIdMapping( List<T> list ) {
		Hashtable<Integer, T> mapping = new Hashtable<Integer, T>();
		for( T item : list ) {
			mapping.put( item.getId(), item );
		}

		return mapping;
	}
	
	
	
}
