 package pl.wd.kursy.web.ui.admin.renderer;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import pl.wd.kursy.data.Skill;
import pl.wd.kursy.data.SkillType;
import pl.wd.kursy.data.StudentStatus;
import pl.wd.kursy.web.ui.data.InfoSkill;
import pl.wd.kursy.web.ui.util.WebUtil;

public class SkillListItemRenderer implements ListitemRenderer<Skill>, Serializable {

	private static final long serialVersionUID = 506615676284375556L;
	
	private static final Logger logger = Logger.getLogger(SkillListItemRenderer.class);

	@Override
	public void render( Listitem item, Skill skill, int index ) throws Exception {
		Listcell lc;
		
		if ( skill.isEditable() ) {
			lc = new Listcell();
			lc.setParent(item);
			final Intbox ib = new Intbox();
			ib.setConstraint(new Constraint() {
				@Override
				public void validate(Component comp, Object value) throws WrongValueException {
					if ( !skill.isNewItem() && skill.isEditable() ) {
						int order = 0;
						try {
							order = Integer.parseInt(value.toString());
						} catch (Exception nfe) {
							throw new WrongValueException(comp, "Proszę wprowadzić liczbę");
						}
						if ( order < 1 ) {
							throw new WrongValueException(comp, "Proszę wprowadzić liczbę większą od zera");
						}
					}
					else {
						skill.setNewItem(false);
					}
				}
			});
			ib.setHflex("1");
			ib.setText(Integer.toString(skill.getOrder()));
			ib.setParent(lc);

			lc = new Listcell();
			lc.setParent(item);
			Textbox tb = new Textbox();
			tb.setHflex("1");
			tb.setText(skill.getName());
			tb.setParent(lc);
			
			lc = new Listcell();
			Combobox cmbType = addCbType(lc, skill);
			lc.setParent(item);
			

			lc = new Listcell();
			Div div = new Div();
			div.setParent(lc);
			Button btnOk = new Button ();
			btnOk.setImage("/resources/img/icons/ok.png");
			btnOk.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					InfoSkill infoSkill = new InfoSkill();
					try {
						int i = Integer.parseInt(ib.getText());
						skill.setOrder(i);
						Events.echoEvent("onDoEditOk", item.getParent(), infoSkill);
					} catch (Exception e) {
						Messagebox.show(e.toString());
						return;
					}
					skill.setEditable(false);
					infoSkill.setRow(index);
					infoSkill.setText(tb.getText());
					skill.setType((SkillType)WebUtil.getCmbValue(cmbType));
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
			lc = new Listcell(Integer.toString(skill.getOrder()));
			lc.setStyle("text-align:right");
			lc.setParent(item);

			lc = new Listcell(skill.getName());
			lc.setStyle("text-align:left");
			lc.setParent(item);

			lc = new Listcell(skill.getType().getName());
			String style = "text-align:left";
			if ( SkillType.MAIN.equals(skill.getType())) {
				style = "; font-weight:bold; ";
			}
			lc.setStyle(style);

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
	
	private Combobox addCbType( Listcell lc, Skill skill ) throws Exception {
		final Combobox cmbType = new Combobox();
		cmbType.setReadonly(true);
		cmbType.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
    	public void onEvent(Event event) throws Exception {
    		int selInd = cmbType.getSelectedIndex();
//    		ProjectPersonStatus status = _status_list.get(selInd);
//    		((ProjectPerson)((Row)event.getTarget().getParent()).getValue()).setStatus(status);
//    		WebUtil.selectCombo(cmbStatus, status);
    	}
		});		
		cmbType.setWidth("80%");
		List<SkillType> statList = SkillType.getListAll();
		
		
		ListModel<SkillType> typeModel = new ListModelArray<>(statList);
		cmbType.setModel(typeModel);
		cmbType.setItemRenderer(new ComboitemRenderer<SkillType>() {
			@Override
			public void render(Comboitem item, SkillType data, int index) throws Exception {
				item.setLabel(data.getName());
				if (data.isSelected()) {
					cmbType.setSelectedItem(item);
				}
				item.setValue(data);
			}
		});
		WebUtil.selectCombo(cmbType, skill.getType() );

		cmbType.setParent(lc);
		
		return cmbType;
	}
	

}

