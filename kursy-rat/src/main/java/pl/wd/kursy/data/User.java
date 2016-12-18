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
	
	public User() {
	}
	
	public User(String login, String pass ) {
		_login = login;
		_pass = pass;
	}

	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "id")
	public int get_id() {
		return _id;
	}

	public void set_id( int id ) {
		_id = id;
	}

	public String get_login() {
		return _login;
	}

	public void set_login(String login) {
		_login = login;
	}

	public String get_pass() {
		return _pass;
	}

	public void set_pass(String pass) {
		_pass = pass;
	}
	

}
