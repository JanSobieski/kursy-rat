package pl.wd.kursy.web.security;

import java.util.Hashtable;
import java.util.Random;

import pl.wd.kursy.data.User;

public class Context {
	public final static int MAX_RETRY_COUNT = 100;

	private static Context _instance = new Context();

	private Hashtable<Long, User> _client2user;

	private Context() {
		_client2user = new Hashtable<>();
	}
	
	public static Context getInstance() {
		return _instance;
	}
	
	public synchronized long registerUser( User user ) {
		int retry_count = 0;
		User existing_user = null;
		long client_id = System.currentTimeMillis();
		do {
			client_id = System.currentTimeMillis();
			existing_user = _client2user.get(Long.valueOf(client_id));
			if ( existing_user != null ) {
				Random rnd = new Random();
				try {
					Thread.sleep((long) (rnd.nextDouble() * 500));
				} catch (Exception e) {
				}
				if ( retry_count > MAX_RETRY_COUNT ) {
					throw new RuntimeException( "Max retry count exceeded while obtaining client id" );
				}
				retry_count++;
			}
		} while( existing_user != null );
		_client2user.put( client_id, user );
		
		return client_id;
	}


}
