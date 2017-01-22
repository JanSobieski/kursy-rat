package pl.wd.kursy.web.ui.menu.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wd.web.menu.util.ILabelElement;
import org.wd.web.menu.util.MenuFactoryDto;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

import pl.wd.kursy.web.UserWorkspace;
import pl.wd.kursy.web.ui.menu.domain.IMenuDomain;
import pl.wd.kursy.web.ui.menu.domain.MenuDomain;
import pl.wd.kursy.web.ui.menu.domain.MetaMenuFactory;

/**
 */
abstract public class ZkossMenuFactory implements Serializable {

	private static final long serialVersionUID = 142621423557135573L;
	private final Log loger = LogFactory.getLog(getClass());

	final private LinkedList<Component> stack;
	final private UserWorkspace workspace;

	protected ZkossMenuFactory(Component component) {
		super();
		this.workspace = UserWorkspace.getInstance();

		assert component != null : "Parent component is null!";
		assert this.workspace != null : "No UserWorkspace exists!";

		long t1 = System.nanoTime();

		this.stack = new LinkedList<Component>();
		push(component);

		createMenu(MetaMenuFactory.getRootMenuDomain().getItems());
	}

	private void createMenu(List<IMenuDomain> items) {
		if (items.isEmpty()) {
			return;
		}
		for (final IMenuDomain menuDomain : items) {
			if (menuDomain instanceof MenuDomain) {
				final MenuDomain menu = (MenuDomain) menuDomain;
				if (addSubMenuImpl(menu)) {
					createMenu(menu.getItems());
					ebeneHoch();
				}
			} else {
				addItemImpl(menuDomain);
			}
		}
	}

	private void addItemImpl(IMenuDomain itemDomain) {
		if (isAllowed(itemDomain)) {
			setAttributes(itemDomain, createItemComponent(getCurrentComponent()));
		}
	}

	abstract protected ILabelElement createItemComponent(Component parent);

	private boolean addSubMenuImpl(MenuDomain menu) {
		if (isAllowed(menu)) {
			final MenuFactoryDto dto = createMenuComponent(getCurrentComponent(), menu.isOpen());

			setAttributes(menu, dto.getNode());

			push(dto.getParent());

			return true;
		}
		return false;
	}

	abstract protected MenuFactoryDto createMenuComponent(Component parent, boolean open);

	private boolean isAllowed(IMenuDomain treecellValue) {
		return isAllowed(treecellValue.getRightName());
	}

	private void ebeneHoch() {
		poll();
	}

	private Component getCurrentComponent() {
		return peek();
	}

	private Log getLogger() {
		return this.loger;
	}

	private boolean isAllowed(String rightName) {
		if ( ( rightName == null ) || ( rightName.length() == 0 ) ) {
			return true;
		}

		
		try {
			return this.workspace.check_authorisation_read(rightName);
		} catch (Exception e) {
			//return true;
			throw new RuntimeException( e );
		}
	}

	private Component peek() {
		return this.stack.peek();
	}

	private Component poll() {
		try {
			return this.stack.poll();
		} finally {
			if (this.stack.isEmpty()) {
				throw new RuntimeException("Root no longer exists!");
			}
		}
	}

	private void push(Component e) {
		this.stack.push(e);
	}

	protected void setAttributes(IMenuDomain treecellValue, ILabelElement defaultTreecell) {
		if (treecellValue.isWithOnClickAction() == null || treecellValue.isWithOnClickAction().booleanValue()) {
			defaultTreecell.setZulNavigation(treecellValue.getZulNavigation());

			if (!StringUtils.isEmpty(treecellValue.getIconName())) {
				defaultTreecell.setImage(treecellValue.getIconName());
			}
			
			if ( treecellValue.isCreateTab() != null ) {
				defaultTreecell.setShowTab(treecellValue.isCreateTab().booleanValue());
			}
		}

		setAttributesWithoutAction(treecellValue, defaultTreecell);
	}

	private void setAttributesWithoutAction(IMenuDomain treecellValue, ILabelElement defaultTreecell) {
		assert treecellValue.getId() != null : "In mainmenu.xml file is a node who's ID is missing!";

		defaultTreecell.setId(treecellValue.getId());
		String label = treecellValue.getLabel();
		if (StringUtils.isEmpty(label)) {
			label = Labels.getLabel(treecellValue.getId());
		} else {
			label = Labels.getLabel(label);
		}
		defaultTreecell.setLabel(" " + label);
	}
}
