package pl.wd.kursy.web.ui.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.Skill;
import pl.wd.kursy.web.UserWorkspace;


public class ExerciseTreeModel {

	private DefaultTreeModel<BasicType> _treeModel;
	private UserWorkspace _workspace;
	private List<Exercise> _exercises = new ArrayList<>();

	public ExerciseTreeModel(UserWorkspace workspace) throws Exception {
		_workspace = workspace;
		_treeModel = new DefaultTreeModel<BasicType>(getTreeRoot());
	}

	private TreeNode<BasicType> getTreeRoot() throws Exception {
		LinkedList<DefaultTreeNode<BasicType>> rootItems = new LinkedList<DefaultTreeNode<BasicType>>();
		
		_exercises = _workspace.getDataServiceProvider().getExercises();
//		_skillKeyWords = _workspace.getDataServiceProvider().get_skill_key_words();
//
//		Hashtable<Integer, List<SkillKeyWord>> category_id2key_words = SkillKeyWord.get_category_id2key_words_mapping(_skillKeyWords);

		for (Exercise exercise : _exercises) {
				LinkedList<DefaultTreeNode<BasicType>> items = new LinkedList<DefaultTreeNode<BasicType>>();
				ExtTreeNode<BasicType> nodeCategory = new ExtTreeNode<BasicType>(exercise, items);
				rootItems.add(nodeCategory);
				if ( exercise.getSkills() != null ) {
					for( Skill skill : exercise.getSkills() ) {
					ExtTreeNode<BasicType> nodeSkill = new ExtTreeNode<BasicType>(skill);
					nodeCategory.getChildren().add(nodeSkill);
				}
				}
				
		}
		ExtTreeNode<BasicType> root = new ExtTreeNode<BasicType>(null, rootItems);

		return root;
	}
	
	public TreeNode<BasicType> getEmptyTreeRoot() throws Exception {
		LinkedList<DefaultTreeNode<BasicType>> rootItems = new LinkedList<DefaultTreeNode<BasicType>>();
		ExtTreeNode<BasicType> root = new ExtTreeNode<BasicType>(null, rootItems);

		return root;
	}
	

	public DefaultTreeModel<BasicType> getModel() {
		return _treeModel;
	}
	
	

}
