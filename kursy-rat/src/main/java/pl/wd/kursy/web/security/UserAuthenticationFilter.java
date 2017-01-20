package pl.wd.kursy.web.security;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.TextEscapeUtils;

import pl.wd.kursy.misc.Util;

public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	static Logger logger = Logger.getLogger("pl.econsulting.eis.web.security.UserAuthenticationFilter");  

	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "j_username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "j_password";
	public static final String SPRING_SECURITY_FORM_COURSE_ID = "courseId";
	public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";
	public static final String CLIENT_ID = "CL_ID";

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

	public UserAuthenticationFilter() {
		super("/j_spring_security_check");
	}

	@Override
	public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException,
			IOException, ServletException {

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}
		try {
			password = Util.encode_password(new String(password));
		} catch (Exception err ) {
			logger.error( "Blad:", err );
		}
		username = username.trim();
		String courseId = obtainCourseId(request);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( username, password );

		// Place the last username attempted into HttpSession for views
		HttpSession session = request.getSession(false);
		if (session != null || getAllowSessionCreation()) {
			request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, TextEscapeUtils.escapeEntities(username));
		}

//		setDetails(request, authToken);
		Authentication newAuth = this.getAuthenticationManager().authenticate(authToken);
		java.util.Collection<? extends GrantedAuthority> auth = (java.util.Collection<? extends GrantedAuthority>)newAuth.getAuthorities();
		for( Iterator iterator = auth.iterator(); iterator.hasNext(); ) {
			GrantedAuthority grantedAuthority = (GrantedAuthority) iterator.next();
			if ( grantedAuthority.getAuthority().startsWith( CustomAuthenticationManager.CL_ID) ) {
				String cl_id = grantedAuthority.getAuthority().substring( CustomAuthenticationManager.CL_ID.length() );
//				long client_id = Long.parseLong(cl_id);
				if (session != null || getAllowSessionCreation()) {
//					LoginBean login_bean = (LoginBean)request.getSession().getAttribute(LoginBean.LOGIN_BEAN_ATTR );
//					if ( login_bean != null ) {
//						login_bean.setClientId( Long.parseLong(cl_id) );
//						login_bean.load_clients_user();
//					}
//					else {
						request.getSession().setAttribute(CLIENT_ID, cl_id);
						request.getSession().setAttribute(SPRING_SECURITY_FORM_COURSE_ID, courseId);
						
//					}
				}
				break;
			}
		}

		return newAuth;
	}
	
	/**
	 * Enables subclasses to override the composition of the password, such as
	 * by including additional values and a separator.
	 * <p>
	 * This might be used for example if a postcode/zipcode was required in
	 * addition to the password. A delimiter such as a pipe (|) should be used
	 * to separate the password and extended value(s). The
	 * <code>AuthenticationDao</code> will need to generate the expected
	 * password in a corresponding manner.
	 * </p>
	 *
	 * @param request
	 *            so that request attributes can be retrieved
	 *
	 * @return the password that will be presented in the
	 *         <code>Authentication</code> request token to the
	 *         <code>AuthenticationManager</code>
	 */
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	/**
	 * Enables subclasses to override the composition of the username, such as
	 * by including additional values and a separator.
	 *
	 * @param request
	 *            so that request attributes can be retrieved
	 *
	 * @return the username that will be presented in the
	 *         <code>Authentication</code> request token to the
	 *         <code>AuthenticationManager</code>
	 */
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}

	protected String obtainCourseId(HttpServletRequest request) {
		return request.getParameter(SPRING_SECURITY_FORM_COURSE_ID);
	}
	
	/**
	 * Provided so that subclasses may configure what is put into the
	 * authentication request's details property.
	 *
	 * @param request
	 *            that an authentication request is being created for
	 * @param authRequest
	 *            the authentication request object that should have its details
	 *            set
	 */

	public final String getUsernameParameter() {
		return usernameParameter;
	}

	public final String getPasswordParameter() {
		return passwordParameter;
	}


}
