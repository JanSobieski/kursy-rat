package pl.wd.kursy.web.ui.tree;

import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Student;
import pl.wd.kursy.data.StudentGroup;

public class GroupTreeItemRenderer implements TreeitemRenderer<ExtTreeNode<BasicType>> {

	@Override
	public void render(Treeitem item, ExtTreeNode<BasicType> data, int index) throws Exception {
		if (data.getData() instanceof StudentGroup) {
			item.setLabel(data.getData().getName());
			item.setOpen(data.isOpen());

			item.setValue(data);
			if (data.getData().isSelected()) {
				item.setSelected(true);
			} else {
				item.setSelected(false);
			}

			//item.setStyle("height: 34px");
			Treerow tr;
			if (item.getTreerow() == null) {
				tr = new Treerow();
				tr.setParent(item);
			} else {
				tr = item.getTreerow();
				tr.getChildren().clear();
			}

			Textbox tb = new Textbox();
			tb.setText(data.getData().getName());
			tb.setStyle("width: 90%");
			//tb.setHflex("true");
			tb.setRows(1);
			// tb.setVflex("min");
			// tb.setStyle("height: 30px");

			Treecell tc = new Treecell();
			tc.setStyle("height: 45px");
			//tc.setVflex("1");
			tc.appendChild(tb);
			tr.appendChild(tc);
		}

		if (data.getData() instanceof Student) {
			item.setLabel(((Student)data.getData()).getLastFirstName());
			item.setOpen(data.isOpen());

			item.setValue(data);
			if (data.getData().isSelected()) {
				item.setSelected(true);
			} else {
				item.setSelected(false);
			}
		}
		
		//ComponentsCtrl.applyForward(item, "onClick=onTreeItemClicked");
	}

}


