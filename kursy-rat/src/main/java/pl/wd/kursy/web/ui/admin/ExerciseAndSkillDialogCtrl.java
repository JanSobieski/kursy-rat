package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.web.ui.admin.model.SkillListViewModel;
import pl.wd.kursy.web.ui.admin.renderer.SkillListModelItemRenderer;
import pl.wd.kursy.web.ui.custom_controls.ExtendedTree;
import pl.wd.kursy.web.ui.tree.ExerciseTreeItemRenderer;
import pl.wd.kursy.web.ui.tree.ExerciseTreeModel;
import pl.wd.kursy.web.ui.util.BaseCtrl;

public class ExerciseAndSkillDialogCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = -1726615296715627542L;

	private static final Logger logger = Logger.getLogger(ExerciseAndSkillDialogCtrl.class);

	protected Window 			skillListWindow; // autowired
	protected ExtendedTree trExercise; // aurowired
	protected Listbox 			listBoxSkills; // aurowired
	
	private ExerciseTreeModel _exerciseTreeModel;
	private SkillListViewModel<Skill> _model;

	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners

			setupComponents();
			setupTree();
			initData();
			listBoxSkills.setItemRenderer(new SkillListModelItemRenderer());
			
		} catch (final Exception e) {
			logger.error("Error", e);
			Messagebox.show(e.toString());
		} finally {
			try {
				getUserWorkspace().getDataServiceProvider().closeDbSession();
			} catch (Exception e2) {
				logger.error("Error", e2);
			}
		}
	}
	
	public void setupTree() throws Exception {
		 
		_exerciseTreeModel = new ExerciseTreeModel(getUserWorkspace());
		trExercise.setModel( _exerciseTreeModel.getModel() );
		trExercise.setItemRenderer(new ExerciseTreeItemRenderer() );
		
	}
	

	private void setupComponents() throws Exception {
	}
	
	private void initData() throws Exception {
		_model = new SkillListViewModel<Skill>(getUserWorkspace()); 
		_model.setMultiple(true);
		_model.showData(listBoxSkills, null);
	}
	
//	public void onTreeItemClicked(Event event) throws Exception {
//		Event origin = null;
//		Treeitem row = null;
//		Checkbox cbSelected = null;
//		ExtendedTree parentTree = null;
//
//		// get event target
//		if (event instanceof ForwardEvent) {
//			origin = Events.getRealOrigin((ForwardEvent)event);
//			Component target = origin.getTarget();
//			parentTree = (ExtendedTree) target.getParent().getParent();
//			row = (Treeitem)target;
//			
//			Component comp = row.getChildren().get(0);
//			if ( !(comp instanceof Checkbox) ) {
//				return;
//			}
//			cbSelected = (Checkbox)comp;
//		} else {
//			origin = event;
//			cbSelected = (Checkbox)origin.getTarget();
//			parentTree = (ExtendedTree) cbSelected.getParent().getParent().getParent();
//			//row = (Row)cbSelected.getParent();
//		}
//		if ( origin != null ) {
//			BasicType item = row.getValue();
//			item.set_selected(cbSelected.isChecked());
//			if (cbSelected.isChecked()) {
//				unselectRows( parentTree, item );
//			}			
//		}
//	}
//	
//	private void unselectRows( ExtendedTree parentTree, BasicType item ) {
////		for( Component row : parentTree.getRows().getChildren() ) {
////			BasicType rnote = ((Row)row).getValue();
////			if ( ( rnote != null ) && (item.get_id() != rnote.get_id() ) ) {
////				rnote.set_selected(false);
////				Component comp = row.getChildren().get(0);
////				if ( comp instanceof  Checkbox ) {
////					Checkbox cbSelected = (Checkbox)comp;
////					cbSelected.setChecked(false);
////				}
////			}
////		}
//	}
	

}
