package pl.wd.kursy.web.ui.menu.tree;

import org.wd.web.menu.util.ILabelElement;
import org.wd.web.menu.util.MenuFactoryDto;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import pl.wd.kursy.web.ui.menu.util.ZkossMenuFactory;

/**
 */
public class ZkossTreeMenuFactory extends ZkossMenuFactory {

	private static final long serialVersionUID = -1601202637698812546L;

	public static void addMainMenu(Component component) {
		new ZkossTreeMenuFactory(component);
	}

	/**
	 * @param component
	 */
	private ZkossTreeMenuFactory(Component component) {
		super(component);
	}

	@Override
	protected MenuFactoryDto createMenuComponent(Component parent, boolean open) {
		final Treeitem treeitem = new Treeitem();
		parent.appendChild(treeitem);

		treeitem.setOpen(open);

		final ILabelElement item = insertTreeCell(treeitem);

		final Treechildren treechildren = new Treechildren();
		treeitem.appendChild(treechildren);

		return new MenuFactoryDto(treechildren, item);
	}

	@Override
	protected ILabelElement createItemComponent(Component parent) {
		final Treeitem treeitem = new Treeitem();
		parent.appendChild(treeitem);

		final ILabelElement item = insertTreeCell(treeitem);

		return item;
	}

	private ILabelElement insertTreeCell(Component parent) {
		final Treerow treerow = new Treerow();
		parent.appendChild(treerow);

		final DefaultTreecell treecell = new DefaultTreecell();
		treerow.appendChild(treecell);

		return treecell;
	}

}
