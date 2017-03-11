package pl.wd.kursy.web.ui.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;

import pl.wd.kursy.data.Skill;
import pl.wd.kursy.data.StudentGroup;
import pl.wd.kursy.web.UserWorkspace;

public class SkillExerciseGridModel<E> extends ListModelList<Skill> {
	private static final long serialVersionUID = -8598402836760946033L;

	private static final Logger logger = Logger.getLogger(SkillExerciseGridModel.class);

	private final UserWorkspace _workspace;

	public SkillExerciseGridModel(UserWorkspace workspace) throws Exception {
		_workspace = workspace;
	}

	public void fireEventExt(int type, int index0, int index1) {
		super.fireEvent(type, index0, index1);
	}


}
