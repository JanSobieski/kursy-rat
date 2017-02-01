 package pl.wd.kursy.web.ui.admin.renderer;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import pl.wd.kursy.data.Skill;

public class SkillListModelItemRenderer implements ListitemRenderer<Skill>, Serializable {

	private static final long serialVersionUID = 506615676284375556L;
	
	private static final Logger logger = Logger.getLogger(SkillListModelItemRenderer.class);

	@Override
	public void render( Listitem item, Skill skill, int index ) throws Exception {
		Listcell lc;
		lc = new Listcell(Integer.toString(skill.get_id()));
		lc.setStyle("text-align:right");
		lc.setParent(item);
		lc = new Listcell(skill.getName());
		lc.setStyle("text-align:left");
		lc.setParent(item);

		item.setAttribute("data", skill);
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onSkillListItemDoubleClicked");
	}

}

