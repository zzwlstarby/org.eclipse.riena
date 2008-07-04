package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.tests.FTActionListener;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree.DefaultObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.DefaultObservableTreeNode;
import org.eclipse.riena.ui.ridgets.tree.IObservableTreeModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Tests for the {@link TreeRidget}.
 */
public class TreeRidgetTest extends AbstractSWTRidgetTest {

	private static final String ROOT_NODE_USER_OBJECT = "TestRootNode";
	private static final String ROOT_CHILD1_NODE_USER_OBJECT = "TestRootChild1Node";
	private static final String ROOT_CHILD2_NODE_USER_OBJECT = "TestRootChild2Node";
	private static final String ROOT_CHILD1_CHILD1_NODE_USER_OBJECT = "TestRootChild1Child1Node";
	private static final String ROOT_CHILD1_CHILD2_NODE_USER_OBJECT = "TestRootChild1Child2Node";
	private static final String ROOT_CHILD1_CHILD2_CHILD_NODE_USER_OBJECT = "TestRootChild1Child2ChildNode";

	private DefaultObservableTreeNode rootNode;
	private DefaultObservableTreeNode rootChild1Node;
	private DefaultObservableTreeNode rootChild2Node;
	private DefaultObservableTreeNode rootChild1Child1Node;
	private DefaultObservableTreeNode rootChild1Child2Node;
	private DefaultObservableTreeNode rootChild1Child2ChildNode;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bindToModel();
	}

	private void bindToModel() {
		getRidget().bindToModel(initializeTreeModel());
	}

	@Override
	protected Control createUIControl(Composite parent) {
		return new Tree(parent, SWT.MULTI);
	}

	@Override
	protected IRidget createRidget() {
		return new TreeRidget();
	}

	@Override
	protected Tree getUIControl() {
		return (Tree) super.getUIControl();
	}

	@Override
	protected TreeRidget getRidget() {
		return (TreeRidget) super.getRidget();
	}

	// testing methods
	// ////////////////

	public void testGetUIControl() throws Exception {
		Tree control = getUIControl();
		assertEquals(control, getRidget().getUIControl());
	}

	public void testUpdateTreeFromModel() throws Exception {
		ITreeRidget ridget = getRidget();
		Tree control = getUIControl();

		ridget.expandTree();

		assertEquals(1, control.getItemCount());
		TreeItem treeRoot = control.getItems()[0];
		assertEquals(ROOT_NODE_USER_OBJECT, treeRoot.getText());
		assertEquals(2, treeRoot.getItemCount());

		TreeItem treeRootChild1 = treeRoot.getItem(0);
		assertEquals(ROOT_CHILD1_NODE_USER_OBJECT, treeRootChild1.getText());
		TreeItem treeRootChild2 = treeRoot.getItem(1);
		assertEquals(ROOT_CHILD2_NODE_USER_OBJECT, treeRootChild2.getText());
		assertEquals(2, treeRootChild1.getItemCount());
		assertEquals(0, treeRootChild2.getItemCount());

		TreeItem treeRootChild1Child1 = treeRootChild1.getItem(0);
		assertEquals(ROOT_CHILD1_CHILD1_NODE_USER_OBJECT, treeRootChild1Child1.getText());
		TreeItem treeRootChild1Child2 = treeRootChild1.getItem(1);
		assertEquals(ROOT_CHILD1_CHILD2_NODE_USER_OBJECT, treeRootChild1Child2.getText());
		assertEquals(0, treeRootChild1Child1.getItemCount());
		assertEquals(1, treeRootChild1Child2.getItemCount());

		TreeItem treeRootChild1Child2Child = treeRootChild1Child2.getItem(0);
		assertEquals(ROOT_CHILD1_CHILD2_CHILD_NODE_USER_OBJECT, treeRootChild1Child2Child.getText());
		assertEquals(0, treeRootChild1Child2Child.getItemCount());
	}

	public void testUpdateOnModelChanges() throws Exception {
		Tree control = getUIControl();

		assertEquals(3, getItemCount(control));

		rootChild2Node.removeFromParent();

		assertEquals(2, getItemCount(control));

		rootNode.addChild(createNode("TestNewNode1"));
		rootNode.addChild(createNode("TestNewNode2"));

		assertEquals(4, getItemCount(control));
	}

	public void testExpandAndCollapseTree() throws Exception {
		TreeRidget ridget = getRidget();
		Tree control = getUIControl();

		assertEquals(3, getItemCount(control));

		ridget.expandTree();

		assertEquals(6, getItemCount(control));

		ridget.collapseTree();

		assertEquals(1, getItemCount(control));

		// check that the tree was collapsed recursively...

		ridget.expand(rootNode);

		assertEquals(3, getItemCount(control));

		ridget.expandTree();

		assertEquals(6, getItemCount(control));

		// ...to check that it collapses to its default state when a new model
		// is updated...
		bindToModel();
		ridget.updateFromModel();

		assertEquals(3, getItemCount(control));
	}

	public void testExpandAndCollapseTreeWhenUnbound() throws Exception {
		TreeRidget ridget = getRidget();
		Tree control = getUIControl();

		ridget.setUIControl(null);
		ridget.expandTree();
		ridget.setUIControl(control);

		assertEquals(3, getItemCount(control));

		ridget.setUIControl(null);
		ridget.collapseTree();
		ridget.setUIControl(control);

		assertEquals(3, getItemCount(control));
	}

	public void testExpandAndCollapseSingleNodes() throws Exception {
		TreeRidget ridget = getRidget();
		Tree control = getUIControl();

		ridget.expandTree();

		assertEquals(6, getItemCount(control));

		ridget.collapse(rootChild1Child2Node);

		assertEquals(5, getItemCount(control));

		ridget.collapse(rootChild1Node);

		assertEquals(3, getItemCount(control));

		ridget.expand(rootChild1Node);

		assertEquals(5, getItemCount(control));

		ridget.expand(rootChild1Child2Node);

		assertEquals(6, getItemCount(control));
	}

	public void testExpandAndCollapseSingleNodesWhenUnbound() throws Exception {
		TreeRidget ridget = getRidget();
		Tree control = getUIControl();

		ridget.setUIControl(null);
		ridget.expand(rootChild1Child2Node);
		ridget.setUIControl(control);

		assertEquals(3, getItemCount(control));

		ridget.setUIControl(null);
		ridget.collapse(rootNode);
		ridget.setUIControl(control);

		assertEquals(3, getItemCount(control));
	}

	public void testSelectionType() throws Exception {
		TreeRidget ridget = getRidget();
		Tree control = getUIControl();

		assertEquals(ITreeRidget.SelectionType.SINGLE, ridget.getSelectionType());

		ridget.setSelectionType(ITreeRidget.SelectionType.MULTI);

		assertEquals(ITreeRidget.SelectionType.MULTI, ridget.getSelectionType());

		try {
			ridget.setSelectionType(ITreeRidget.SelectionType.NONE);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException expected) {
			// ok, expected
		}

		assertEquals(ITreeRidget.SelectionType.MULTI, ridget.getSelectionType());

		ridget.setUIControl(null); // unbind
		ridget.setSelectionType(ITreeRidget.SelectionType.SINGLE);

		assertEquals(ITreeRidget.SelectionType.SINGLE, ridget.getSelectionType());

		ridget.setUIControl(control); // rebind

		assertEquals(ITreeRidget.SelectionType.SINGLE, ridget.getSelectionType());
	}

	public void testAddDoubleClickListener() {
		ITreeRidget ridget = getRidget();
		Tree control = getUIControl();

		try {
			ridget.addDoubleClickListener(null);
			fail();
		} catch (RuntimeException npe) {
			// expected
		}

		FTActionListener listener1 = new FTActionListener();
		ridget.addDoubleClickListener(listener1);

		FTActionListener listener2 = new FTActionListener();
		ridget.addDoubleClickListener(listener2);
		ridget.addDoubleClickListener(listener2);

		Event doubleClick = new Event();
		doubleClick.widget = control;
		doubleClick.type = SWT.MouseDoubleClick;
		control.notifyListeners(SWT.MouseDoubleClick, doubleClick);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeDoubleClickListener(listener1);

		control.notifyListeners(SWT.MouseDoubleClick, doubleClick);

		assertEquals(1, listener1.getCount());
	}

	// helping methods
	// ////////////////

	private IObservableTreeModel initializeTreeModel() {
		rootNode = createNode(ROOT_NODE_USER_OBJECT);
		rootChild1Node = createNode(ROOT_CHILD1_NODE_USER_OBJECT);
		rootChild2Node = createNode(ROOT_CHILD2_NODE_USER_OBJECT);
		rootChild1Child1Node = createNode(ROOT_CHILD1_CHILD1_NODE_USER_OBJECT);
		rootChild1Child2Node = createNode(ROOT_CHILD1_CHILD2_NODE_USER_OBJECT);
		rootChild1Child2ChildNode = createNode(ROOT_CHILD1_CHILD2_CHILD_NODE_USER_OBJECT);

		rootNode.addChild(rootChild1Node);
		rootNode.addChild(rootChild2Node);
		rootChild1Node.addChild(rootChild1Child1Node);
		rootChild1Node.addChild(rootChild1Child2Node);
		rootChild1Child2Node.addChild(rootChild1Child2ChildNode);

		return createTreeModel(rootNode);
	}

	private DefaultObservableTreeNode createNode(String userObject) {
		return new DefaultObservableTreeNode(userObject);
	}

	private DefaultObservableTreeModel createTreeModel(DefaultObservableTreeNode rootNode) {
		return new DefaultObservableTreeModel(rootNode);
	}

	/**
	 * Returns the number of items in the tree starting from the root
	 * (included).
	 */
	private int getItemCount(Tree control) {
		int count = 0;
		for (TreeItem root : control.getItems()) {
			count += getItemCount(root);
		}
		return count;
	}

	/** Returns the number of items in subtree starting from item (included). */
	private int getItemCount(TreeItem item) {
		int count = 0;
		if (item != null) { // && item.getData() != null) {
			count++;
			if (item.getExpanded()) {
				for (TreeItem child : item.getItems()) {
					count += getItemCount(child);
				}
			}
		}
		return count;
	}

}
