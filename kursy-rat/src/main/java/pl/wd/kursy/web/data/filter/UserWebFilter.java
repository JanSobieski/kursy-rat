package pl.wd.kursy.web.data.filter;


public class UserWebFilter {
	private String _id;
	private String _name;
	private String _personName;
	
	public String getName() {
		if ( _name == null ) {
			_name = "";
		}
    return _name.toLowerCase();
	}

	public void setName(String name) {
		_name = name;
	}

	public String getPersonName() {
		if ( _personName == null ) {
			_personName = "";
		}
    return _personName.toLowerCase();
	}

	public void setPersonName(String name) {
    _personName = name;
	}

	public String getId() {
		return _id;
	}

	public void setId( String id ) {
		_id = id;
	}
}
