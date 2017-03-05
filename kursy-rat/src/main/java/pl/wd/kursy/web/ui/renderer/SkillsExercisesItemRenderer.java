package pl.wd.kursy.web.ui.renderer;

import java.io.Serializable;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import pl.wd.kursy.data.Skill;

public class SkillsExercisesItemRenderer implements RowRenderer<Skill>, Serializable {
	private static final long serialVersionUID = -6123226270091976877L;

	@Override
	public void render(Row row, Skill skill, int index) throws Exception {
		Label lb = new Label(skill.getName());
		lb.setStyle("text-align:left");
		row.appendChild(lb);
		
		
		row.setValue(skill);
	}

}
