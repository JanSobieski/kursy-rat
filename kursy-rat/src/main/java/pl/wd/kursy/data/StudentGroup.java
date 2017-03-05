package pl.wd.kursy.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import pl.wd.kursy.web.ui.util.WebUtil;

@Entity
@Table(name = "student_groups")
@GenericGenerator(name = "hibernate-increment", strategy = "increment")
public class StudentGroup extends BasicType implements Serializable {
	private static final long serialVersionUID = -3884754733831392535L;
	
	private int _courseId;
	
	private Set<Student> _students = new HashSet<>();


	public StudentGroup() {
	}
	
	@Id
	@GeneratedValue(generator = "hibernate-increment")
	@Column(name = "sgp_id")
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
	
	@Column(name = "crs_id")
	public int getCourseId() {
		return _courseId;
	}

	public void setCourseId(int courseId) {
		_courseId = courseId;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	@JoinTable(name = "student_groups_students", joinColumns = { @JoinColumn(name = "sgp_id", nullable=false)}, inverseJoinColumns = @JoinColumn(name = "stu_id", nullable=false))
	public Set<Student> getStudents() {
		return _students;
	}

	public void setStudents(Set<Student> students) {
		_students = students;
	}
	

}
