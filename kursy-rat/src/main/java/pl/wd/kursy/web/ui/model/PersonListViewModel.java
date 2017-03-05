package pl.wd.kursy.web.ui.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;

import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.StudentGroup;
import pl.wd.kursy.data.criteria.StudentCriteria;
import pl.wd.kursy.data.wrapper.StudentWrapper;
import pl.wd.kursy.web.UserWorkspace;

public class PersonListViewModel<E> extends ListModelList<Object> {
	private static final long serialVersionUID = -366498153440587643L;

	private static final Logger logger = Logger.getLogger(PersonListViewModel.class);

	private final UserWorkspace _workspace;

	public PersonListViewModel(UserWorkspace workspace) throws Exception {
		_workspace = workspace;

	}
	
	public void loadData(StudentCriteria filter) throws Exception {
		List<Student> personsDB = _workspace.getDataServiceProvider().getStudents(filter);
		//Collections.sort(persons, new PersonListComparator(true, PersonListComparator.TYPE_LAST_NAME));
		List<StudentGroup> groups = _workspace.getDataServiceProvider().getStudentGroups(_workspace.getCourseId());
		Hashtable<Student, List<StudentGroup>> student2groups = new Hashtable<>();
		for (StudentGroup group : groups) {
			for (Student student : group.getStudents()) {
				List<StudentGroup> grps = student2groups.get(student);
				if ( grps == null ) {
					grps = new ArrayList<StudentGroup>(); 
					student2groups.put(student,grps);
				}
				grps.add(group);
			}
		}
		
		List<StudentWrapper> personsW = new ArrayList<>();
		clear();

		for (Student student : personsDB) {
			StudentWrapper sw = new StudentWrapper(student);
			List<StudentGroup> grps = student2groups.get(student);
			if ( grps != null ) {
				sw.setGroups(grps);
			}
			personsW.add(sw);
		}
		addAll(personsW);
		
		
	}
	

}
