package pl.wd.kursy.data;

import java.io.Serializable;

public class BasicType implements Serializable,Cloneable {
	private static final long serialVersionUID = -3087968737693385232L;

	protected int _id;
	
	public BasicType() {
	}

	public BasicType( int id ) {
		_id = id;
	}

}
