package pl.wd.kursy.web.ui.tree;

import java.util.LinkedList;

import org.zkoss.zul.DefaultTreeNode;

public class ExtTreeNode<T> extends DefaultTreeNode<T> {
	private static final long serialVersionUID = 6519027740714668005L;
	// Node Control the default open
	private boolean open = true;

	public ExtTreeNode( T data, LinkedList<DefaultTreeNode<T>> children, boolean open ) {
		super(data, children);
		this.setOpen(open);
	}

	public ExtTreeNode( T data, LinkedList<DefaultTreeNode<T>> children ) {
		super(data, children);
	}

	public ExtTreeNode( T data ) {
		super(data);
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen( boolean open ) {
		this.open = open;
	}
}