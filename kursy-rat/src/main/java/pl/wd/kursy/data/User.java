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

	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "id")
	public int get_id() {
		return _id;
	}

	public void set_id( int id ) {
		_id = id;
	}
	

}
