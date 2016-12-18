package pl.wd.kursy.data;

import java.io.Serializable;

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

	public int get_id() {
		return _id;
	}

	public void set_id( int id ) {
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
			if ( _id == ((BasicType) obj).get_id() ) {
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
	
}
