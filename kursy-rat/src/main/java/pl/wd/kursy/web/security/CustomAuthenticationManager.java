package pl.wd.kursy.web.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;

public class CustomAuthenticationManager implements AuthenticationManager {
	static Logger logger = Logger.getLogger("pl.econsulting.eis.web.security.CustomAuthunticationManager");

	public static final String CL_ID = "CLID:";

	public Authentication authenticate( Authentication authentication ) throws AuthenticationException {

		// if(StringUtils.isBlank((String) authentication.getPrincipal()) ||
		// StringUtils.isBlank((String) authentication.getCredentials())){
		// throw new BadCredentialsException("Invalid username/password");
		// }

		String username = authentication.getName();
		String pass = authentication.getCredentials().toString();

		GrantedAuthority[] grantedAuthorities = null;

		String cl_id = null;
		try {
			cl_id = "100";
		} catch (Exception e) {
			throw new RuntimeException("Blad: ", e );
		}

		User user_auth = new User(username, pass, true, true, true, true, makeGrantedAuthorities( cl_id ));

		return new UsernamePasswordAuthenticationToken(user_auth, authentication.getCredentials(), makeGrantedAuthorities( cl_id ));
	}

	private java.util.Collection<? extends GrantedAuthority> makeGrantedAuthorities( String client_id) {
		List<GrantedAuthorityImpl> auth = new ArrayList<GrantedAuthorityImpl>();
		auth.add(new GrantedAuthorityImpl("ROLE_URLACCESS"));
		auth.add(new GrantedAuthorityImpl( CL_ID + client_id ));

		return auth;
	}

}
