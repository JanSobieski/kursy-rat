package pl.wd.kursy.service;

import java.io.Serializable;

import org.apache.log4j.Logger;

import pl.wd.kursy.service.interf.DataServiceInt;

public class DataService implements DataServiceInt, Serializable {
	private static final long serialVersionUID = -1169104155427694717L;
	static Logger logger = Logger.getLogger("pl.econsulting.eis.web.service.DataService");

	//private Database _db = new Database();

	public void closeDbSession() throws Exception {
		//_db.close_session();
	}

}
