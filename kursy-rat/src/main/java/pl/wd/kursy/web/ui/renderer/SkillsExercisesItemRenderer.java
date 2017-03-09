package pl.wd.kursy.web.ui.renderer;

import java.io.Serializable;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import pl.wd.kursy.data.RateCardItemStatus;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.web.ui.course.PersonRatingCardCtrl;

public class SkillsExercisesItemRenderer implements RowRenderer<Skill>, Serializable {
	private static final long serialVersionUID = -6123226270091976877L;
	
	private PersonRatingCardCtrl _ctrl;
	//private List<ProjectPersonStatus> _status_list;
	
	public SkillsExercisesItemRenderer(PersonRatingCardCtrl ctrl) {
		_ctrl = ctrl;
	}

	@Override
	public void render(Row row, Skill skill, int index) throws Exception {
		Label lb = new Label(skill.getName());
		lb.setStyle("text-align:left");
		row.appendChild(lb);
		
		if ( _ctrl.getRateCardItem() != null ) {
			if ( _ctrl.getRateCardItem().getSkills().contains(skill) ) {
				addCbStatus( row, skill );
			}
			else {
				lb = new Label("");
				row.appendChild(lb);
			}
			
		}
		
		
		row.setValue(skill);
	}
	
	private void addCbStatus( Row row, Skill skill ) throws Exception {
		final Combobox cmbStatus = new Combobox();
		cmbStatus.setReadonly(true);
		cmbStatus.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
    	public void onEvent(Event event) throws Exception {
    		int selInd = cmbStatus.getSelectedIndex();
//    		ProjectPersonStatus status = _status_list.get(selInd);
//    		((ProjectPerson)((Row)event.getTarget().getParent()).getValue()).setStatus(status);
//    		WebUtil.selectCombo(cmbStatus, status);
    	}
		});		
		//cmbStatus.setHflex("1");
		//cmbStatus.setWidth("80%");
		ListModel<RateCardItemStatus> statusModel = new ListModelArray<>(RateCardItemStatus.getList());
		cmbStatus.setModel(statusModel);
		cmbStatus.setItemRenderer(new ComboitemRenderer<RateCardItemStatus>() {
			@Override
			public void render(Comboitem item, RateCardItemStatus data, int index) throws Exception {
				item.setLabel(data.getName());
				if (data.isSelected()) {
					cmbStatus.setSelectedItem(item);
				}
				item.setValue(data);
			}
		});
		//WebUtil.selectCombo(cmbStatus, person.getStatus() );

		row.appendChild(cmbStatus);
	}
	

}
