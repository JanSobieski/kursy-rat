package pl.wd.kursy.data.comp;

import java.io.Serializable;
import java.util.Comparator;

import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.misc.Util;

public class ExerciseComparator extends BaseComparator implements Comparator<Exercise>, Serializable {

	private static final long serialVersionUID = -1644276835668987035L;

	public final static int TYPE_ID = 1;
	public final static int TYPE_NAME = 2;
	
	public ExerciseComparator(boolean asc, int type) {
		super(asc, type);
	}

	  @Override
	public int compare(Exercise o1, Exercise o2) {
		int result = 0;
		switch (_type) {
			case TYPE_ID:
				result = o1.getId() - o2.getId();
				break;
				
			case TYPE_NAME:
				result = Util.getCollator().compare( o1.getName(), o2.getName() );
				break;
		}
		if ( _asc ) 
			return result;
		else
			return -1 * result;
	}

}
