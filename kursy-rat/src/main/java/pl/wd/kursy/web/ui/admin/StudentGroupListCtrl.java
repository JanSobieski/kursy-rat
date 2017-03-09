package pl.wd.kursy.web.ui.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
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
import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.StudentGroup;
import pl.wd.kursy.web.ui.admin.model.StudentListViewModel;
import pl.wd.kursy.web.ui.admin.renderer.StudentListModelItemRenderer;
import pl.wd.kursy.web.ui.custom_controls.ExtendedTree;
import pl.wd.kursy.web.ui.interf.ChoiceDialogInt;
import pl.wd.kursy.web.ui.tree.ExtTreeNode;
import pl.wd.kursy.web.ui.tree.GroupTreeItemRenderer;
import pl.wd.kursy.web.ui.tree.GroupsTreeModel;
import pl.wd.kursy.web.ui.util.BaseCtrl;
import pl.wd.kursy.web.ui.util.WebUtil;

public class StudentGroupListCtrl extends BaseCtrl implements Serializable {
		private static final long serialVersionUID = -7846740333876278469L;

		private static final Logger logger = Logger.getLogger(StudentGroupListCtrl.class);

		protected Window 			studentGroupWindow; // autowired
		protected ExtendedTree trGroups; // aurowired
		protected Listbox 			listBoxStudents; // aurowired
		

		protected Button btnSave; // aurowired
		protected Button btnEdit; // aurowired
		
		private GroupsTreeModel _groupsTreeModel;
		private StudentListViewModel<Student> _studentModel;
		
		
		public void doAfterCompose(Window comp) {
			try {
				super.doAfterCompose(comp); // wire variables and event listners

			    ChoiceDialogInt choiceDialogInt = new ChoiceDialogInt() {
					@Override
					public void onOkClose() {
						try {
							setupTree();
							initData();
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
			    };
			    if ( CourseChoiceCtrl.checkCourse( getUserWorkspace(), WebUtil.getTabId(studentGroupWindow),  choiceDialogInt ) ) {
					setupTree();
					initData();
			    }

				listBoxStudents.setItemRenderer(new StudentListModelItemRenderer());
				
				
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
			_groupsTreeModel = new GroupsTreeModel(getUserWorkspace());
			trGroups.setModel( _groupsTreeModel.getModel() );
			trGroups.setItemRenderer(new GroupTreeItemRenderer() );
		}

		private void initData() throws Exception {
			_studentModel = new StudentListViewModel<Student>(getUserWorkspace()); 
			_studentModel.setMultiple(true);
			_studentModel.showData(listBoxStudents, null);
		}
		
		public void onClick$btnNew( Event event ) {
			_groupsTreeModel.addGroup();
		}
		
		public void onClick$btnEdit( Event event ) {
			doEdit();
		}
		
		private void doEdit() {
//			if (_project.getProject().get_id() > 0) {
//				if (!lockRecord(Constants.TABLE_PROJECTS, _project.getProject().get_id())) {
//					return;
//				}
//				loadFromDB();
//			}
			btnSave.setDisabled(false);
			btnEdit.setDisabled(true);
		}
		
		@SuppressWarnings("unchecked")
		public void onClick$btnAdd( Event event ) {
			Treeitem selTreeItem = trGroups.getSelectedItem();
			if  ( ( selTreeItem == null ) || ( selTreeItem.getValue() == null ) || ( ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData() instanceof Student ) ) {
				return;
			}
			HashSet<Student> existingStudents = new HashSet<>();
			for (Component component : selTreeItem.getChildren() ) {
				if ( component instanceof Treechildren ) {
					for (Component childTreeItem : ((Treechildren)component).getChildren() ) {
						Student student = (Student) ((ExtTreeNode<BasicType>) ((Treeitem)childTreeItem).getValue()).getData();
						existingStudents.add(student);
					}
				}
				
			}
			Set<Listitem> selItems = listBoxStudents.getSelectedItems();
			for( Listitem selectedItem : selItems ) {
				Student student = (Student) selectedItem.getValue();
				if ( existingStudents.contains(student) ) {
					continue;
				}
				_groupsTreeModel.addStudent( selTreeItem, student );
			}
		}
		
		@SuppressWarnings("unchecked")
		public void onClick$btnDelete( Event event ) throws Exception {
			Treeitem selTreeItem = trGroups.getSelectedItem();
			if  ( ( selTreeItem == null ) || ( selTreeItem.getValue() == null ) ) {
				return;
			}
			getData(false);
			if  (  ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData() instanceof Student ) {
				Student selStudent = (Student) ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData();
				Treeitem parent = (Treeitem)selTreeItem.getParent().getParent();
				int index = 0;

				for (TreeNode<BasicType> childTreeItem : ((ExtTreeNode<BasicType>) parent.getValue()).getChildren() ) {
					Student student = (Student) ((ExtTreeNode<BasicType>)childTreeItem).getData();
					if ( selStudent.equals(student)) {
						trGroups.setModel( _groupsTreeModel.getEmptyModel() );
						((ExtTreeNode<BasicType>) parent.getValue()).getChildren().remove(index);
						//			_exerciseTreeModel.fireEvent(TreeDataEvent.STRUCTURE_CHANGED, null, 0, 0);
						
						break;
					}
					index++;
				}
			} else {
				//exercise
				int index = 0;
				StudentGroup selStudentGroup = (StudentGroup) ((ExtTreeNode<BasicType>)selTreeItem.getValue()).getData();
				
				selTreeItem.getParent().getChildren().remove(selTreeItem);

				for (TreeNode<BasicType> childTreeItem : _groupsTreeModel.getModel().getRoot().getChildren() ) {
					StudentGroup studentGroup = (StudentGroup)  ((ExtTreeNode<BasicType>)childTreeItem).getData();
					if ( selStudentGroup.equals(studentGroup)) {
						trGroups.setModel( _groupsTreeModel.getEmptyModel() );
						_groupsTreeModel.getModel().getRoot().getChildren().remove(index);
						
						break;
					}
					index++;
				}
			}
			trGroups.setModel( _groupsTreeModel.getModel() );
		}
		
		public List<StudentGroup> getData(boolean checkEmpty) {
			List<StudentGroup> data = new ArrayList<>();
			for (Treeitem treeitem : trGroups.getItems() ) {
				ExtTreeNode<BasicType> node = treeitem.getValue();
				if ( node.getData() instanceof StudentGroup) {
					StudentGroup studentGroup = (StudentGroup)node.getData();
					studentGroup.getStudents().clear();
					data.add(studentGroup);
					Treerow tr = treeitem.getTreerow();
					Treecell tc = (Treecell)tr.getChildren().get(0);
					Textbox tb = (Textbox)tc.getChildren().get(0);
					studentGroup.setName(tb.getText());
					if ( checkEmpty  && (tb.getText().trim().length() == 0 ) ) {
						Messagebox.show("Nazwa grupy nie może być pusta");
						trGroups.setSelectedItem(treeitem);
						tb.setFocus(true);
						return null;
					}
					for (Component component : treeitem.getChildren() ) {
						if ( component instanceof Treechildren ) {
							for (Component childTreeItem : ((Treechildren)component).getChildren() ) {
								Student student = (Student) ((ExtTreeNode<BasicType>) ((Treeitem)childTreeItem).getValue()).getData();
								studentGroup.getStudents().add(student);
							}
						}
						
					}
				}
			}
			
			return data;		
		}
		
		public boolean doSave() {
			List<StudentGroup> data = getData(true);
			if ( data == null ) {
				return false;
			}
			try {
				getUserWorkspace().getDataServiceProvider().saveStudentGroups(data, getUserWorkspace().getCourseId());
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
			String tabId = WebUtil.getTabId(studentGroupWindow);
			WebUtil.closeTab(tabId);
		}

		
}
