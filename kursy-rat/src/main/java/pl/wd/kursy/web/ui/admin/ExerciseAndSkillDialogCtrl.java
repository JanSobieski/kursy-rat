package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.web.ui.admin.model.SkillListViewModel;
import pl.wd.kursy.web.ui.admin.renderer.SkillListItemRenderer;
import pl.wd.kursy.web.ui.custom_controls.ExtendedTree;
import pl.wd.kursy.web.ui.data.InfoSkill;
import pl.wd.kursy.web.ui.tree.ExerciseTreeItemRenderer;
import pl.wd.kursy.web.ui.tree.ExerciseTreeModel;
import pl.wd.kursy.web.ui.tree.ExtTreeNode;
import pl.wd.kursy.web.ui.util.BaseCtrl;
import pl.wd.kursy.web.ui.util.WebUtil;

public class ExerciseAndSkillDialogCtrl extends BaseCtrl implements Serializable {
	private static final long serialVersionUID = -1726615296715627542L;

	private static final Logger logger = Logger.getLogger(ExerciseAndSkillDialogCtrl.class);

	protected Window 			skillListWindow; // autowired
	protected ExtendedTree trExercise; // aurowired
	protected Listbox 			listBoxSkills; // aurowired

	protected Button btnSave; // aurowired
	protected Button btnEdit; // aurowired
	protected Checkbox cbObligatory; // aurowired
	protected Textbox tbDescription; // aurowired
	
	private ExerciseTreeModel _exerciseTreeModel;
	private SkillListViewModel<Skill> _skillModel;
	private Exercise _prevSelectedExercise; 

	public void doAfterCompose(Window comp) {
		try {
			super.doAfterCompose(comp); // wire variables and event listners

			setupComponents();
			setupTree();
			initData();
			listBoxSkills.setItemRenderer(new SkillListItemRenderer());
			
			
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
		_skillModel = new SkillListViewModel<Skill>(getUserWorkspace()); 
		_skillModel.setMultiple(true);
		_skillModel.showData(listBoxSkills, null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$addBtn( Event event ) {
		Treeitem selTreeItem = trExercise.getSelectedItem();
		if  ( ( selTreeItem == null ) || ( selTreeItem.getValue() == null ) || ( ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData() instanceof Skill ) ) {
			return;
		}
		HashSet<Skill> existingSkills = new HashSet<>();
		for (Component component : selTreeItem.getChildren() ) {
			if ( component instanceof Treechildren ) {
				for (Component childTreeItem : ((Treechildren)component).getChildren() ) {
					Skill skill = (Skill) ((ExtTreeNode<BasicType>) ((Treeitem)childTreeItem).getValue()).getData();
					existingSkills.add(skill);
				}
			}
			
		}
		Set<Listitem> selItems = listBoxSkills.getSelectedItems();
		for( Listitem selectedItem : selItems ) {
			Skill skill = (Skill) selectedItem.getValue();
			if ( existingSkills.contains(skill) ) {
				continue;
			}
			_exerciseTreeModel.addSkill( selTreeItem, skill );
		}
	}
	
	public void onClick$btnNew( Event event ) {
		_exerciseTreeModel.addExercise();
	}
	
	public void onClick$btnEdit( Event event ) {
		doEdit();
	}
	
	private void doEdit() {
//		if (_project.getProject().get_id() > 0) {
//			if (!lockRecord(Constants.TABLE_PROJECTS, _project.getProject().get_id())) {
//				return;
//			}
//			loadFromDB();
//		}
		btnSave.setDisabled(false);
		btnEdit.setDisabled(true);
	}
	
	public void onClick$btnSave( Event event ) {
		try {
			if ( doSave() ) {
				closeWindow();
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}
	
	private void closeWindow() {
		String tabId = WebUtil.getTabId(skillListWindow);
		WebUtil.closeTab(tabId);
	}
	
	public List<Skill> getSkills(boolean checkEmpty) {
		List<Skill> skills = new ArrayList<>();
		
		for (Listitem listitem : listBoxSkills.getItems() ) {
			Skill skill = listitem.getValue();
			if ( skill.isEditable() ) {
				Textbox tb = (Textbox)listitem.getChildren().get(1).getChildren().get(0);
				if ( checkEmpty && ( tb.getText().trim().length() == 0 ) ) {
					Messagebox.show("Nazwa umiejętności nie może być pusta");
					return null;
				}
				skill.setName(tb.getText());
			}
			skills.add(skill);
		}
		
		return skills;
	}
	
	public List<Exercise> getData(boolean checkEmpty) {
		List<Exercise> data = new ArrayList<>();
		for (Treeitem treeitem : trExercise.getItems() ) {
			ExtTreeNode<BasicType> node = treeitem.getValue();
			if ( node.getData() instanceof Exercise) {
				Exercise ex = (Exercise)node.getData();
				ex.getSkills().clear();
				data.add(ex);
				Treerow tr = treeitem.getTreerow();
				Treecell tc = (Treecell)tr.getChildren().get(0);
				Textbox tb = (Textbox)tc.getChildren().get(0);
				ex.setName(tb.getText());
				if ( checkEmpty  && (tb.getText().trim().length() == 0 ) ) {
					Messagebox.show("Nazwa ćwiczenia nie może być pusta");
					trExercise.setSelectedItem(treeitem);
					tb.setFocus(true);
					return null;
				}
				for (Component component : treeitem.getChildren() ) {
					if ( component instanceof Treechildren ) {
						for (Component childTreeItem : ((Treechildren)component).getChildren() ) {
							Skill skill = (Skill) ((ExtTreeNode<BasicType>) ((Treeitem)childTreeItem).getValue()).getData();
							ex.getSkills().add(skill);
						}
					}
					
				}
			}
		}
		
		return data;		
	}
	
	public boolean doSave() {
		List<Skill> skills = getSkills(true);
		if ( skills == null ) {
			return false;
		}
		List<Exercise> data = getData(true);
		if ( data == null ) {
			return false;
		}
		try {
			getUserWorkspace().getDataServiceProvider().saveExercisesAndSkills(data, skills);
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
		return true;
	}
	
	public void onClick$btnNewSkill( Event event ) throws Exception {
		_skillModel.newSkill();
	}
	
	public void onDoEdit$listBoxSkills( Event event ) throws Exception {
		Event origin = Events.getRealOrigin((ForwardEvent)event);
		if ( origin.getData() instanceof Integer ) {
			_skillModel.repaintRow( (Integer)origin.getData() );
		}
	}
	
	public void onDoEditCancel$listBoxSkills( Event event ) throws Exception {
		Event origin = Events.getRealOrigin((ForwardEvent)event);
		if ( origin.getData() instanceof Integer ) {
			_skillModel.repaintRow( (Integer)origin.getData() );
		}
	}
	
	public void onDoEditOk$listBoxSkills( Event event ) throws Exception {
		Event origin = Events.getRealOrigin((ForwardEvent)event);
		if ( origin.getData() instanceof InfoSkill ) {
			InfoSkill infoSkill = (InfoSkill)origin.getData();
			int row = infoSkill.getRow();
			_skillModel.repaintRow( row );
			Skill skill = (Skill)_skillModel.get(row);
			skill.setName(infoSkill.getText());
		}
	}
	
	public void onSelect$trExercise( Event event ) throws Exception {
		Treeitem selTreeItem = trExercise.getSelectedItem();
		if  ( ( selTreeItem == null ) || ( selTreeItem.getValue() == null ) || ( ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData() instanceof Skill ) ) {
			return;
		}
		Exercise exercise  = (Exercise)((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData();
		boolean obl = cbObligatory.isChecked();
		String descr = tbDescription.getText();
		if (_prevSelectedExercise != null ) {
			_prevSelectedExercise.setDescription(descr);
			_prevSelectedExercise.setAll_skills_obligatory(obl);
		}
		
		cbObligatory.setChecked(exercise.getAll_skills_obligatory());
		tbDescription.setText(exercise.getDescription());
		
		_prevSelectedExercise = exercise;
	}

	@SuppressWarnings("unchecked")
	public void onClick$btnDelete( Event event ) throws Exception {
		Treeitem selTreeItem = trExercise.getSelectedItem();
		if  ( ( selTreeItem == null ) || ( selTreeItem.getValue() == null ) ) {
			return;
		}
		getData(false);
		if  (  ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData() instanceof Skill ) {
			Skill selSkill = (Skill) ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData();
			Treeitem parent = (Treeitem)selTreeItem.getParent().getParent();
			int index = 0;

			for (TreeNode<BasicType> childTreeItem : ((ExtTreeNode<BasicType>) parent.getValue()).getChildren() ) {
				Skill skill = (Skill) ((ExtTreeNode<BasicType>)childTreeItem).getData();
				if ( selSkill.equals(skill)) {
					trExercise.setModel( _exerciseTreeModel.getEmptyModel() );
					((ExtTreeNode<BasicType>) parent.getValue()).getChildren().remove(index);
					//			_exerciseTreeModel.fireEvent(TreeDataEvent.STRUCTURE_CHANGED, null, 0, 0);
					
					break;
				}
				index++;
			}
		} else {
			//exercise
			int index = 0;
			Exercise selExercise = (Exercise) ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData();
			
			selTreeItem.getParent().getChildren().remove(selTreeItem);

			for (TreeNode<BasicType> childTreeItem : _exerciseTreeModel.getModel().getRoot().getChildren() ) {
				Exercise exercise = (Exercise)  ((ExtTreeNode<BasicType>)childTreeItem).getData();
				if ( selExercise.equals(exercise)) {
					trExercise.setModel( _exerciseTreeModel.getEmptyModel() );
					_exerciseTreeModel.getModel().getRoot().getChildren().remove(index);
					
					break;
				}
				index++;
			}
		}
		trExercise.setModel( _exerciseTreeModel.getModel() );
		
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$btnDeleteSkill( Event event ) throws Exception {
		Set<Skill> skillsToRemove = new HashSet<>();
		Set<Listitem> selItems = listBoxSkills.getSelectedItems();
		for( Listitem selectedItem : selItems ) {
			Skill skill = (Skill) selectedItem.getValue();
			skillsToRemove.add(skill);
			
			_skillModel.remove(skill);
		}
	}
}

// tree
//  			fireEvent(TreeDataEvent.STRUCTURE_CHANGED, null, 0, 0);
