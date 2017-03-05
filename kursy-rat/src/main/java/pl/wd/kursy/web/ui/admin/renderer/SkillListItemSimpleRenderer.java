 package pl.wd.kursy.web.ui.admin.renderer;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import pl.wd.kursy.data.Skill;

public class SkillListItemSimpleRenderer implements ListitemRenderer<Skill>, Serializable {
	private static final long serialVersionUID = -5823785333006191949L;

	private static final Logger logger = Logger.getLogger(SkillListItemSimpleRenderer.class);

	@Override
	public void render( Listitem item, Skill skill, int index ) throws Exception {
		Listcell lc;
		lc = new Listcell(skill.getName());
		lc.setStyle("text-align:left");
		lc.setParent(item);
		
		item.setValue(skill);
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onSkillListItemDoubleClicked");
	}

}

