package pl.wd.kursy.web.ui.tree;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.StudentGroup;
import pl.wd.kursy.web.UserWorkspace;


public class GroupsTreeModel {

	private DefaultTreeModel<BasicType> _treeModel;
	private ExtTreeNode<BasicType> _root;
	private UserWorkspace _workspace;
	private List<StudentGroup> _groups = new ArrayList<>();
	private int exFreeId = -1;

	public GroupsTreeModel(UserWorkspace workspace) throws Exception {
		_workspace = workspace;
		_treeModel = new DefaultTreeModel<BasicType>(getTreeRoot());
	}

	private TreeNode<BasicType> getTreeRoot() throws Exception {
		List<DefaultTreeNode<BasicType>> rootItems = new ArrayList<DefaultTreeNode<BasicType>>();
		
		_groups = _workspace.getDataServiceProvider().getStudentGroups(_workspace.getCourseId());

		for (StudentGroup group : _groups) {
			List<DefaultTreeNode<BasicType>> items = new ArrayList<DefaultTreeNode<BasicType>>();
			ExtTreeNode<BasicType> nodeCategory = new ExtTreeNode<BasicType>(group, items);
			rootItems.add(nodeCategory);
			if (group.getStudents() != null) {
				for (Student student : group.getStudents()) {
					ExtTreeNode<BasicType> nodeStudent = new ExtTreeNode<BasicType>(student);
					nodeCategory.getChildren().add(nodeStudent);
				}
			}

		}
		_root = new ExtTreeNode<BasicType>(null, rootItems);

		return _root;
	}
	
	public TreeNode<BasicType> getEmptyTreeRoot() throws Exception {
		List<DefaultTreeNode<BasicType>> rootItems = new ArrayList<DefaultTreeNode<BasicType>>();
		ExtTreeNode<BasicType> root = new ExtTreeNode<BasicType>(null, rootItems);

		return root;
	}

	public DefaultTreeModel<BasicType> getModel() {
		
		return _treeModel;
	}
	
	public DefaultTreeModel<BasicType> getEmptyModel() {
		List<DefaultTreeNode<BasicType>> rootItems = new ArrayList<DefaultTreeNode<BasicType>>();
		ExtTreeNode<BasicType> root = new ExtTreeNode<BasicType>(null, rootItems);
		
		return new DefaultTreeModel<BasicType>(root);
	}

	@SuppressWarnings("unchecked")
	public void addStudent(Treeitem treeItem, Student student) {
		ExtTreeNode<BasicType> nodeStudent = new ExtTreeNode<BasicType>(student);
		((ExtTreeNode<BasicType>) treeItem.getValue()).getChildren().add(nodeStudent);
	}	
	
	public void addGroup() {
		StudentGroup group = new StudentGroup();
		group.setCourseId(_workspace.getCourseId());
		group.setId(exFreeId);
		exFreeId--;
		List<DefaultTreeNode<BasicType>> items = new ArrayList<DefaultTreeNode<BasicType>>();
		ExtTreeNode<BasicType> node = new ExtTreeNode<BasicType>(group, items);
		_root.getChildren().add(node);
	}
	
	public void removeItem( Treeitem treeItem ) {
		
	}

	public ExtTreeNode<BasicType> getRoot() {
		return _root;
	}
	

}
