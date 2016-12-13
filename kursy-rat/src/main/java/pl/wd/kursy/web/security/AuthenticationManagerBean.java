package pl.wd.kursy.web.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;

public class AuthenticationManagerBean {
	private static AuthenticationManagerBean _authenticationManagerBean;
	private AuthenticationManager _authenticationManager;
	private SessionRegistryImpl _sessionRegistry;

	public AuthenticationManagerBean() {
		setAuthenticationManagerBean( this );
	}
	
	public AuthenticationManagerBean( SessionRegistryImpl sessionRegistry ) {
		setAuthenticationManagerBean( this );
		_sessionRegistry = sessionRegistry;
		
    for (Object principal : sessionRegistry.getAllPrincipals()) {
        for (SessionInformation session : sessionRegistry.getAllSessions(principal, false)) {
        	session.expireNow();
        }
    }		
	}
	
	public AuthenticationManager getAuthenticationManager() {
		return _authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		_authenticationManager = authenticationManager;
	}

	public static AuthenticationManagerBean getAuthenticationManagerBean() {
		return _authenticationManagerBean;
	}

	public static void setAuthenticationManagerBean( AuthenticationManagerBean authenticationManagerBean ) {
		_authenticationManagerBean = authenticationManagerBean;
	}

	public SessionRegistryImpl getSessionRegistry() {
		return _sessionRegistry;
	}

	public void setSessionRegistry( SessionRegistryImpl sessionRegistry ) {
		_sessionRegistry = sessionRegistry;
	}


}
