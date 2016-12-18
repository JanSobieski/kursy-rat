package pl.wd.kursy.web.data.comp;

import java.io.Serializable;
import java.util.Comparator;

import pl.wd.kursy.data.User;

public class UserWebComparator extends BaseComparator implements Comparator<User>, Serializable {
	private static final long serialVersionUID = 4607476394712496534L;
	
	public final static int TYPE_LOGIN = 1;
	public final static int TYPE_PERSON = 2;

  public UserWebComparator(boolean asc, int type) {
      _asc = asc;
      _type = type;
  }
  
	@Override
	public int compare( User o1, User o2 ) {
		int result = 0;
		switch (_type) {
			case TYPE_LOGIN:
				result = o1.getLogin().compareTo(o2.getLogin() );
				if ( _asc ) 
					return result;
				else
					return -1 * result;
			
		}
		return 0;
	}


}
