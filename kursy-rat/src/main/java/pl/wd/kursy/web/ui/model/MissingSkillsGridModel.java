package pl.wd.kursy.web.ui.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.zkoss.zul.ListModelList;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.RateCardItem;
import pl.wd.kursy.data.RateCardItemStatus;
import pl.wd.kursy.data.RateCardSkillItem;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.data.Student;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.data.MissingSkillGridRow;

public class MissingSkillsGridModel extends ListModelList<MissingSkillGridRow> {
	private static final long serialVersionUID = 6739520686673638280L;
	
	private final UserWorkspace _workspace;

	public MissingSkillsGridModel(UserWorkspace workspace) throws Exception {
		_workspace = workspace;
		List<Exercise> exercises = _workspace.getDataServiceProvider().getExercises();
		List<Skill> mainSkills = _workspace.getDataServiceProvider().getMainSkills();
		Hashtable<Integer, Skill> htId2Skill = BasicType.getIdMapping(mainSkills);
		Hashtable<Integer, HashSet<Integer>> studentId2Excercise = new Hashtable<>(); 
		
		Hashtable<HashSet<Skill>,List<Student>> missingSkills2StudentList = new Hashtable<HashSet<Skill>,List<Student>>(); 
		
		List<Student> students = _workspace.getDataServiceProvider().getStudents(_workspace.getCourseId());
		for (Student student : students) {
			HashSet<Integer> excerciseIds = new HashSet<> (); 
			studentId2Excercise.put(student.getId(), excerciseIds);
			HashSet<Skill> notPassed = new HashSet<>(); 
			HashSet<Skill> passed = new HashSet<>(); 
			List<RateCardItem> rateCards = _workspace.getDataServiceProvider().getRateCardNotRKO(student.getId());
			for (RateCardItem rateCardItem : rateCards) {
				excerciseIds.add(rateCardItem.getExerciseId());
				
				for (RateCardSkillItem skillItem : rateCardItem.getSkills() ) {
					Skill skill = htId2Skill.get(skillItem.getSkillId());
					if ( skill != null ) {
						if ( RateCardItemStatus.K.equals(skillItem.getStatus())) {
							passed.add(skill);
						}
						else {
						}
						
					}
				}
			}
			
			for (Skill skill : mainSkills ) {
				if ( !passed.contains(skill)) {
					notPassed.add(skill);
				}
			}
			
			List<Student> studentList = missingSkills2StudentList.get(notPassed);
			if ( studentList == null ) {
				studentList = new ArrayList<Student>();
				missingSkills2StudentList.put(notPassed, studentList);
			}
			studentList.add(student);
		}
		HashSet<Integer> excerciseIds;
		int grpNr = 1;
		Enumeration<HashSet<Skill>> keys = missingSkills2StudentList.keys();
		while( keys.hasMoreElements() ) {
			HashSet<Skill> skills = keys.nextElement();
			List<Student> groupStudents = missingSkills2StudentList.get(skills);
			excerciseIds = null;
			
			if ( groupStudents.size() > 0 ) {
				excerciseIds = studentId2Excercise.get(groupStudents.get(0));
			}
			if ( excerciseIds == null ) {
				excerciseIds = new HashSet<>();
			}			
			
			String skillTxt = "";
			for (Skill skill  : skills  ) {
				if ( skillTxt.length() > 0 ) {
					skillTxt += ", ";
				}
				skillTxt += skill.getOrder() + "." + skill.getName();
			}
			String grpTxt = "Grupa " + grpNr + ": " + skillTxt;
					
			MissingSkillGridRow msRow = new MissingSkillGridRow();
			msRow.setGroupText( grpTxt );
			add(msRow);
			grpNr++;
			
			int foundSkills = 0;
			Hashtable<Integer,List<Exercise>> count2ExcerciseList = new Hashtable<>(); 
			for (Exercise exercise : exercises) {
				foundSkills = 0;
				if ( !excerciseIds.contains(exercise.getId()) && !exercise.getAll_skills_obligatory() ) {
					
					// zliczanie ilosci zaliczonych umiejetnosci dla cwiczenia
					for (Skill skill : exercise.getSkills() ) {
						if ( skills.contains(skill) ) {
							foundSkills++;
						}						
					}
					List<Exercise> list = count2ExcerciseList.get(foundSkills);
					if ( list == null ) {
						list = new ArrayList<>();
						count2ExcerciseList.put(foundSkills, list);
					}
					list.add(exercise);
				}
			}
			String exercisesTxt = "";
			for (int i = skills.size(); i > 0; i--) {
				List<Exercise> list = count2ExcerciseList.get(i);
				if ( list != null ) {
					for (Exercise exercise : list) {
						if ( exercisesTxt.length() > 0 ) {
							exercisesTxt  += ", ";
						}
						exercisesTxt  += exercise.getName();
						if ( i < skills.size() ) {
							exercisesTxt  += " (" + Integer.toString(i - skills.size()) + ")";
						}
					}
				}
				
			}
			
			
			for (Student student2 : groupStudents) {
				msRow = new MissingSkillGridRow();
				msRow.setStudent(student2);
				msRow.setSuggestedExercise( exercisesTxt);
				add(msRow);
				
			}
			
		}
		
	}
	

}
