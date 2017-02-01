package pl.wd.kursy.web.ui.tree;

import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import pl.wd.kursy.data.BasicType;
import pl.wd.kursy.data.Exercise;
import pl.wd.kursy.data.Skill;

public class ExerciseTreeItemRenderer implements TreeitemRenderer<ExtTreeNode<BasicType>> {

	@Override
	public void render(Treeitem item, ExtTreeNode<BasicType> data, int index) throws Exception {
		if (data.getData() instanceof Exercise) {
			item.setLabel(data.getData().getName());
			item.setOpen(data.isOpen());

			item.setValue(data);
			if (data.getData().is_selected()) {
				item.setSelected(true);
			} else {
				item.setSelected(false);
			}

			item.setStyle("height: 34px");
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
			tb.setHflex("1");
			tb.setRows(1);
			// tb.setVflex("min");
			// tb.setStyle("height: 30px");

			Treecell tc = new Treecell();
			tc.setStyle("height: 45px");
			tc.appendChild(tb);
			tr.appendChild(tc);
		}

		if (data.getData() instanceof Skill) {
			item.setLabel(data.getData().getName());
			item.setOpen(data.isOpen());

			item.setValue(data);
			if (data.getData().is_selected()) {
				item.setSelected(true);
			} else {
				item.setSelected(false);
			}
		}
		
		//ComponentsCtrl.applyForward(item, "onClick=onTreeItemClicked");
		

	}

}


