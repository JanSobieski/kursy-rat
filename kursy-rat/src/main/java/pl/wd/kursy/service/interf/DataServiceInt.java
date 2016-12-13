package pl.wd.kursy.service.interf;

public interface DataServiceInt {
	public void closeDbSession() throws Exception;

	public boolean check_authorisation_read( int user_id, String authorisation ) throws Exception;

}
