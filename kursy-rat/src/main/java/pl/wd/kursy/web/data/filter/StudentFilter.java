package pl.wd.kursy.web.data.filter;

import pl.wd.kursy.data.Student;

public class StudentFilter extends Student {
	private static final long serialVersionUID = -4664206380086078281L;

	private String _sId;

	public String getSid() {
		return _sId;
	}

	public void setSid(String sId) {
		_sId = sId;
	}

}
