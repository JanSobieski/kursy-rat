package pl.wd.kursy.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.wd.kursy.data.interf.BasicTypeIntf;
import pl.wd.kursy.misc.Util;

public enum SkillType implements BasicTypeIntf {
	SECONDARY(0, "drugorzędna"), MAIN(1, "główna");

	private final int _id;
	private final String _name;
	private boolean _selected;

	SkillType(int id, String name) {
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

	public static List<SkillType> getListAll() {
		return new ArrayList<>(Arrays.asList(SECONDARY, MAIN));
	}

	public static SkillType getById(int id) {
		for (SkillType e : values()) {
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
