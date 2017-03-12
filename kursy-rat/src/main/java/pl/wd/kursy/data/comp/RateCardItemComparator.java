package pl.wd.kursy.data.comp;

import java.io.Serializable;
import java.util.Comparator;

import pl.wd.kursy.data.RateCardItem;

public class RateCardItemComparator extends BaseComparator implements Comparator<RateCardItem>, Serializable {
	private static final long serialVersionUID = 5323567803296277254L;

	public final static int TYPE_DATE_CREATED = 3;

	public RateCardItemComparator(boolean asc, int type) {
		super(asc, type);
	}

	@Override
	public int compare(RateCardItem o1, RateCardItem o2) {
		int result = 0;
		switch (_type) {
		case TYPE_DATE_CREATED:
			result = (int)(o1.getDate_created().getTime() - o2.getDate_created().getTime());
			break;
		}
		if (_asc)
			return result;
		else
			return -1 * result;
	}

}
