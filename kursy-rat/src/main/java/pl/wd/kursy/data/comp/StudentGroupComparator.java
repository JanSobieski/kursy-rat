package pl.wd.kursy.data.comp;

import java.io.Serializable;
import java.util.Comparator;

import pl.wd.kursy.data.StudentGroup;
import pl.wd.kursy.misc.Util;

public class StudentGroupComparator extends BaseComparator implements Comparator<StudentGroup>, Serializable {
	private static final long serialVersionUID = 274443044477260250L;

	public StudentGroupComparator(boolean asc, int type) {
		super(asc, type);
	}

	@Override
	public int compare(StudentGroup o1, StudentGroup o2) {
		int result = 0;
		switch (_type) {
		case TYPE_ID:
			result = o1.getId() - o2.getId();
			break;

		case TYPE_NAME:
			result = Util.getCollator().compare(o1.getName(), o2.getName());
			break;
		}
		if (_asc)
			return result;
		else
			return -1 * result;
	}

}
