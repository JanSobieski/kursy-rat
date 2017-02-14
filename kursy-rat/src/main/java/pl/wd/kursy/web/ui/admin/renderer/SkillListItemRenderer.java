 package pl.wd.kursy.web.ui.admin.renderer;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import pl.wd.kursy.data.Skill;
import pl.wd.kursy.web.ui.data.InfoSkill;

public class SkillListItemRenderer implements ListitemRenderer<Skill>, Serializable {

	private static final long serialVersionUID = 506615676284375556L;
	
	private static final Logger logger = Logger.getLogger(SkillListItemRenderer.class);

	@Override
	public void render( Listitem item, Skill skill, int index ) throws Exception {
		Listcell lc;
		lc = new Listcell(Integer.toString(skill.getId()));
		lc.setStyle("text-align:right");
		lc.setParent(item);
		
		if ( skill.isEditable() ) {
			lc = new Listcell();
			lc.setParent(item);
			
			Textbox tb = new Textbox();
			tb.setHflex("1");
			tb.setParent(lc);
			tb.setText(skill.getName());

			lc = new Listcell();
			Div div = new Div();
			div.setParent(lc);
			Button btnOk = new Button ();
			btnOk.setImage("/resources/img/icons/ok.png");
			btnOk.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			      public void onEvent( Event event ) throws Exception {
			  		skill.setEditable(false);
			  		InfoSkill infoSkill  = new InfoSkill();
			  		infoSkill.setRow(index);
			  		infoSkill.setText(tb.getText());
					Events.echoEvent("onDoEditOk", item.getParent(), infoSkill  );
			      }
			    });
			div.getChildren().add(btnOk);
			Button btnCancel = new Button ();
			btnCancel.setImage("/resources/img/icons/cross-small.png");
			btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			      public void onEvent( Event event ) throws Exception {
			  		skill.setEditable(false);
					Events.echoEvent("onDoEditCancel", item.getParent(), Integer.valueOf( index ) );
			      }
			    });
			div.getChildren().add(btnCancel);
		}
		else {
			lc = new Listcell(skill.getName());
			lc.setStyle("text-align:left");
			lc.setParent(item);

			lc = new Listcell();
			Button btnEdit = new Button ();
			btnEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			      public void onEvent( Event event ) throws Exception {
			  		skill.setEditable(true);
					Events.echoEvent("onDoEdit", item.getParent(), Integer.valueOf( index ) );
			      }
			    });
			btnEdit.setImage("/resources/img/icons/edit_16.png");
			btnEdit.setParent(lc);
		}
		lc.setParent(item);

		item.setValue(skill);
		
		ComponentsCtrl.applyForward(item, "onDoubleClick=onSkillListItemDoubleClicked");
		
	}

}

