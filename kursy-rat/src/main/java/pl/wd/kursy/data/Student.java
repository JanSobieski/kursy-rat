package pl.wd.kursy.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import pl.wd.kursy.web.ui.util.WebUtil;

@Entity
@Table(name = "students")
@GenericGenerator(name = "hibernate-increment", strategy = "increment")
public class Student extends BasicType implements Serializable {
	private static final long serialVersionUID = -7522672168943359853L;

	private String _firstName;
	private String _lastName;
	
	public Student() {
	}
	
	public Student( Student student ) {
		super( student.get_id() );
	}

	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "stu_id")
	public int getId() {
		return _id;
	}

	public void setId( int id ) {
		_id = id;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return WebUtil.normValue(_firstName);
	}

	public void setFirstName(String firstName) {
		_firstName = firstName;
	}

	@Column(name = "last_name")
	public String getLastName() {
		return WebUtil.normValue(_lastName);
	}

	public void setLastName(String lastName) {
		_lastName = lastName;
	}


	

}
