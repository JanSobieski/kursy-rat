package pl.wd.kursy.data.comp;

import java.io.Serializable;
import java.util.Comparator;

import pl.wd.kursy.data.Skill;
import pl.wd.kursy.misc.Util;

public class SkillComparator extends BaseComparator implements Comparator<Skill>, Serializable {
	private static final long serialVersionUID = -3918930561188312038L;

	public SkillComparator(boolean asc, int type) {
		super(asc, type);
	}

	@Override
	public int compare(Skill o1, Skill o2) {
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
