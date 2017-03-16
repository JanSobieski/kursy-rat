package pl.wd.kursy.web.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.data.comp.SkillComparator;
import pl.wd.kursy.web.UserWorkspace;

public class SkillExerciseGridModel<E> extends ListModelList<Skill> {
	private static final long serialVersionUID = -8598402836760946033L;

	private static final Logger logger = Logger.getLogger(SkillExerciseGridModel.class);

	private final UserWorkspace _workspace;
	private boolean _rko;
	private Hashtable<Integer,Exercise> _id2ex;
	private Hashtable<Integer,Skill> _id2skill;
	private List<Exercise> _exercises; 
	private List<Skill> _allSkills = new ArrayList<>(); 

	public SkillExerciseGridModel(UserWorkspace workspace) throws Exception {
		_workspace = workspace;
	}

	public void fireEventExt(int type, int index0, int index1) {
		super.fireEvent(type, index0, index1);
	}
	
	public void initData(boolean rko) throws Exception {
		_rko = rko;
		Set<Skill> skills = new HashSet<>();
		_exercises = _workspace.getDataServiceProvider().getExercises(rko);
		_exercises.stream().forEach((exercise) -> {
			skills.addAll(exercise.getSkills());
		});
		_allSkills .addAll(skills);
		Collections.sort(_allSkills, new SkillComparator(true, SkillComparator.TYPE_NAME));

		_id2ex = BasicType.getIdMapping(_exercises);
		
		_id2skill = BasicType.getIdMapping(_allSkills);
		
		
	}

	public Hashtable<Integer, Exercise> getId2ex() {
		return _id2ex;
	}

	public List<Skill> getSkills() {
		return _allSkills;
	}

	public Hashtable<Integer, Skill> getId2skill() {
		return _id2skill;
	}



}
