package pl.wd.kursy.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.Assert;

import pl.wd.kursy.web.ui.util.WebUtil;

@Entity
@Table(name = "skills")
@GenericGenerator(name = "hibernate-increment", strategy = "increment")
public class Skill extends BasicType implements Serializable {
	private static final long serialVersionUID = 8280416657242146297L;
	
	private int _order;
	private SkillType _skillType;
	
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

	@Column(name = "sorder")
	public int getOrder() {
		return _order;
	}

	public void setOrder(int order) {
		_order = order;
	}

	@Column(name = "type_id")
	public int getTypeId() {
		return _skillType.getId();
	}

	public void setTypeId(int skillTypeId) {
		_skillType = SkillType.getById(skillTypeId);
		Assert.notNull(_skillType, "nieznany id SkillType: " + skillTypeId );
	}
	
	@javax.persistence.Transient
	public SkillType getType() {
		return _skillType;
	}

	@javax.persistence.Transient
	public void setType(SkillType skillType) {
		_skillType = skillType;
	}
	

}
