package pl.wd.kursy.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RateCardItemStatus {
	NIE_DOTYCZY(0, "nie dotyczy"),
	K(1,"K"),
	N(2,"N");
	
	private final int _id;  
    private final String _name;
    private boolean _selected;
    
    RateCardItemStatus(int id, String name) {
        _id = id;
        _name = name;
    }

	public int getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public boolean isSelected() {
		return _selected;
	}

	public void setSelected(boolean selected) {
		_selected = selected;
	}
	
	public static List<RateCardItemStatus> getList() {
		return new ArrayList<> (Arrays.asList(NIE_DOTYCZY, K, N));
	}
}
