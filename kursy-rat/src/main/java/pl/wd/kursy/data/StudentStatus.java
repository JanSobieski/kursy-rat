package pl.wd.kursy.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.wd.kursy.data.interf.BasicTypeIntf;
import pl.wd.kursy.misc.Util;

public enum StudentStatus implements BasicTypeIntf {
	PENDING(0, "w toku"), PASSED(1, "Zaliczony"), EXAM(2, "Do egzaminu");

	private final int _id;
	private final String _name;
	private boolean _selected;

	StudentStatus(int id, String name) {
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

	public static List<StudentStatus> getListAll() {
		return new ArrayList<>(Arrays.asList(PENDING, PASSED, EXAM));
	}

	public static StudentStatus getById(int id) {
		for (StudentStatus e : values()) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}

	@Override
	public void setSelected(Object selected) {
		setSelected(Util.toBoolean(selected));
	}

}
