package pl.wd.kursy.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
@GenericGenerator(name = "hibernate-increment", strategy = "increment")
public class User extends BasicType implements Serializable {
	private static final long serialVersionUID = 7910961986707942681L;
	
	private String _login;
	private String _pass;
	private String _description;
	private boolean _admin;
	
	public User() {
	}
	
	public User(String login, String pass ) {
		_login = login;
		_pass = pass;
	}
	
	public User( User user ) {
		super( user.get_id() );
		
		_login = user.getLogin();
		_admin = user.isAdmin();
	}

	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "usr_id")
	public int getId() {
		return _id;
	}

	public void setId( int id ) {
		_id = id;
	}

	public String getLogin() {
		return _login;
	}

	public void setLogin(String login) {
		_login = login;
	}

	@Column(name = "password", nullable = false)
	public String getPass() {
		return _pass;
	}

	public void setPass(String pass) {
		_pass = pass;
	}

	public boolean isAdmin() {
		return _admin;
	}

	public void setAdmin(boolean admin) {
		_admin = admin;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}
	

}
