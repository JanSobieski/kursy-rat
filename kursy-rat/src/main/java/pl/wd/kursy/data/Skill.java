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
@Table(name = "skills")
@GenericGenerator(name = "hibernate-increment", strategy = "increment")
public class Skill extends BasicType implements Serializable {
	private static final long serialVersionUID = 8280416657242146297L;
	
	public Skill() {
	}
	
	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "skl_id")
	public int getId() {
		return _id;
	}

	public void setId( int id ) {
		_id = id;
	}
	
	public String getName() {
		return WebUtil.normValue(_name);
	}

	public void setName(String name) {
		_name = name;
	}
	

}
