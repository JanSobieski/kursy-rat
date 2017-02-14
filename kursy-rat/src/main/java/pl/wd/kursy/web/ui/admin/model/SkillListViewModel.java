package pl.wd.kursy.web.ui.admin.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.event.ListDataEvent;

import pl.wd.kursy.data.Course;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.data.filter.StudentFilter;

public class SkillListViewModel<E> extends ListModelList<Object> {
	private static final long serialVersionUID = -1893660395425845960L;
	
	private static final Logger logger = Logger.getLogger(SkillListViewModel.class);


	private final UserWorkspace _workspace;
	private int freeId = -1;
	

	public SkillListViewModel(UserWorkspace workspace) {
		_workspace = workspace;
	}

	public List<Skill> getSkillList(StudentFilter filter) throws Exception {
		List<Skill> skills = new ArrayList<>();
		List<Skill> skillsDB = null;
		try {
			skillsDB = _workspace.getDataServiceProvider().getSkills();
		} finally {
			_workspace.getDataServiceProvider().closeDbSession();
		}
		for (Skill skill : skillsDB) {
			if (filter != null) {
				if (filter.getSid().length() > 0) {
					if (!Integer.toString(skill.getId()).contains(filter.getSid())) {
						continue;
					}
				}
				if (filter.getName().length() > 0) {
					if (!skill.getName().toLowerCase().contains(filter.getFirstName())) {
						continue;
					}
				}
			}
			skills.add(skill);
		}

		return skills;
	}
	
	public void showData(Listbox skillList, StudentFilter filter) {
		try {
			List<Skill> skills = getSkillList(filter);
			clear();
			addAll(skills);
			skillList.setModel(this);
		} catch (Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		}
	}
	
	public void newSkill() {
		Skill skill = new Skill();
		skill.setEditable(true);
		skill.setId(freeId);
		freeId--;
		add(skill);
	}

	public void repaintRow(int row) {
		fireEvent(ListDataEvent.CONTENTS_CHANGED, row, row);
	}
}
