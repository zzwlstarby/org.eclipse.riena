/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchWindow;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.internal.navigation.ui.swt.CoolbarUtils;
import org.eclipse.riena.internal.navigation.ui.swt.IAdvisorHelper;
import org.eclipse.riena.internal.navigation.ui.swt.RestoreFocusOnEscListener;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.ApplicationNodeListener;
import org.eclipse.riena.navigation.listener.ModuleGroupNodeListener;
import org.eclipse.riena.navigation.listener.ModuleNodeListener;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubApplicationNodeListener;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.navigation.ui.swt.component.TitleComposite;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.EmbeddedBorderRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellBorderRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.riena.navigation.ui.swt.statusline.IStatuslineContentFactoryExtension;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.swt.DefaultStatuslineContentFactory;
import org.eclipse.riena.ui.swt.IStatusLineContentFactory;
import org.eclipse.riena.ui.swt.InfoFlyout;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineSpacer;
import org.eclipse.riena.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.TestingSupport;
import org.eclipse.riena.ui.swt.utils.WidgetIdentificationSupport;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

public class ApplicationViewAdvisor extends WorkbenchWindowAdvisor {

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ApplicationViewAdvisor.class);
	/**
	 * System property defining the initial width of the application window.
	 */
	private static final String PROPERTY_RIENA_APPLICATION_WIDTH = "riena.application.width"; //$NON-NLS-1$
	/**
	 * System property defining the initial height of the application window.
	 */
	private static final String PROPERTY_RIENA_APPLICATION_HEIGHT = "riena.application.height"; //$NON-NLS-1$
	/**
	 * System property defining the minimum width of the application window.
	 */
	private static final String PROPERTY_RIENA_APPLICATION_MINIMUM_WIDTH = "riena.application.minimum.width"; //$NON-NLS-1$
	/**
	 * System property defining the minimum height of the application window.
	 */
	private static final String PROPERTY_RIENA_APPLICATION_MINIMUM_HEIGHT = "riena.application.minimum.height"; //$NON-NLS-1$

	private static final int DEFAULT_COOLBAR_TOP_MARGIN = 2;
	public static final String SHELL_RIDGET_PROPERTY = "applicationWindow"; //$NON-NLS-1$

	enum BtnState {
		NONE, HOVER, HOVER_SELECTED;
	}

	private final ApplicationController controller;
	private final AbstractViewBindingDelegate binding;
	private final IAdvisorHelper advisorHelper;

	private TitleComposite titleComposite;

	// content factory for delegation of content creation from the statusline
	private IStatusLineContentFactory statuslineContentFactory;

	/**
	 * The application window size minimum.
	 */
	private Point applicationSizeMinimum;

	/**
	 * @noreference This constructor is not intended to be referenced by
	 *              clients.
	 */
	public ApplicationViewAdvisor(IWorkbenchWindowConfigurer configurer, ApplicationController pController,
			IAdvisorHelper helper) {
		super(configurer);
		controller = pController;
		binding = createBinding();
		advisorHelper = helper;
		initializeListener();
	}

	public void addUIControl(Composite control, String propertyName) {
		binding.addUIControl(control, propertyName);
	}

	@InjectExtension()
	public void bindStatuslineContentFactory(IStatuslineContentFactoryExtension[] statuslineContentFactoryExtensions) {
		if (statuslineContentFactoryExtensions.length > 0) {
			this.statuslineContentFactory = statuslineContentFactoryExtensions[0].createFactory();
		}
	}

	@Override
	public final ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return advisorHelper.createActionBarAdvisor(configurer);
	}

	@Override
	public void createWindowContents(final Shell shell) {
		initShell(shell);

		// create and layouts the composite of switcher, menu, tool bar etc.
		shell.setLayout(new FormLayout());
		Composite grabCorner = createGrabCorner(shell);
		createStatusLine(shell, grabCorner);
		titleComposite = createTitleComposite(shell);
		Composite menuBarComposite = createMenuBarComposite(shell, titleComposite);
		Composite coolBarComposite = createCoolBarComposite(shell, menuBarComposite);
		Composite mainComposite = createMainComposite(shell, coolBarComposite);
		createInfoFlyout(mainComposite);

		RestoreFocusOnEscListener focusListener = new RestoreFocusOnEscListener(shell);
		focusListener.addControl(RestoreFocusOnEscListener.findCoolBar(menuBarComposite));
		focusListener.addControl(RestoreFocusOnEscListener.findCoolBar(coolBarComposite));

	}

	private void createInfoFlyout(Composite mainComposite) {
		InfoFlyout flyout = new InfoFlyout(mainComposite);
		binding.addUIControl(flyout, "infoFlyout"); //$NON-NLS-1$
	}

	@Override
	public void dispose() {
		super.dispose();
		if (titleComposite != null) {
			titleComposite.dispose();
			titleComposite = null;
		}

	}

	@Override
	public void preWindowOpen() {
		configureWindow();
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		doInitialBinding();
		if (titleComposite != null) {
			// Redraw so that the active tab is displayed correct
			titleComposite.setRedraw(false);
			titleComposite.setRedraw(true);
		}
	}

	/**
	 * Creates a delegate for the binding of view and controller.
	 * 
	 * @return delegate for binding
	 */
	protected AbstractViewBindingDelegate createBinding() {
		return new InjectSwtViewBindingDelegate();
	}

	// helping methods
	//////////////////

	private void initializeListener() {
		NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new MyApplicationNodeListener());
		navigationTreeObserver.addListener(new MySubApplicationNodeListener());
		navigationTreeObserver.addListener(new MyModuleGroupNodeListener());
		navigationTreeObserver.addListener(new MyModuleNodeListener());
		navigationTreeObserver.addListener(new MySubModuleNodeListener());
		navigationTreeObserver.addListenerTo(controller.getNavigationNode());
	}

	/**
	 * Configures the window of the application.
	 */
	private void configureWindow() {

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		String label = controller.getNavigationNode().getLabel();
		if (label != null) {
			configurer.setTitle(label);
		}
		initApplicationSize(configurer);
		if (LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.SHELL_HIDE_OS_BORDER)
				&& !TestingSupport.isTestingEnabled()) { // some testing UI tools might not work with windows w/o real decorations(menu, border, etc){
			// don't show the shell border (with the minimize, maximize and
			// close buttons) of the operation system
			configurer.setShellStyle(SWT.NO_TRIM | SWT.DOUBLE_BUFFERED);
		}

	}

	/**
	 * Reads the two properties for the initial width and the initial height of
	 * the application.
	 * 
	 * @param configurer
	 */
	private void initApplicationSize(IWorkbenchWindowConfigurer configurer) {

		int width = Integer.getInteger(PROPERTY_RIENA_APPLICATION_WIDTH, getApplicationSizeMinimum().x);
		if (width < getApplicationSizeMinimum().x) {
			width = getApplicationSizeMinimum().x;
			LOGGER.log(LogService.LOG_WARNING,
					"The initial width of the application is less than the minimum width which is " //$NON-NLS-1$
							+ getApplicationSizeMinimum().x);
		}

		int height = Integer.getInteger(PROPERTY_RIENA_APPLICATION_HEIGHT, getApplicationSizeMinimum().y);
		if (height < getApplicationSizeMinimum().y) {
			height = getApplicationSizeMinimum().y;
			LOGGER.log(LogService.LOG_WARNING,
					"The initial height of the application is less than the minimum height which is " //$NON-NLS-1$
							+ getApplicationSizeMinimum().y);
		}

		configurer.setInitialSize(new Point(width, height));
	}

	private void initApplicationSizeMinimum() {

		int widthMinimum = Integer.getInteger(PROPERTY_RIENA_APPLICATION_MINIMUM_WIDTH,
				getApplicationDefaultSizeMinimum().x);
		int heightMinimum = Integer.getInteger(PROPERTY_RIENA_APPLICATION_MINIMUM_HEIGHT,
				getApplicationDefaultSizeMinimum().y);
		applicationSizeMinimum = new Point(widthMinimum, heightMinimum);
	}

	private Point getApplicationSizeMinimum() {
		if (applicationSizeMinimum == null) {
			initApplicationSizeMinimum();
		}

		return applicationSizeMinimum;
	}

	private Point getApplicationDefaultSizeMinimum() {
		return (Point) LnfManager.getLnf().getSetting(LnfKeyConstants.APPLICATION_MIN_SIZE);
	}

	private void doInitialBinding() {
		binding.injectAndBind(controller);
		controller.afterBind();
		controller.getNavigationNode().activate();
	}

	private void createStatusLine(Composite shell, Composite grabCorner) {
		IStatusLineContentFactory statusLineFactory = getStatuslineContentFactory();
		if (statusLineFactory == null) {
			statusLineFactory = new DefaultStatuslineContentFactory();
		}
		Statusline statusLine = new Statusline(shell, SWT.None, StatuslineSpacer.class, statusLineFactory);
		FormData fd = new FormData();
		fd.height = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.STATUSLINE_HEIGHT);
		Rectangle navigationBounds = TitlelessStackPresentation.calcNavigationBounds(shell);
		fd.left = new FormAttachment(0, navigationBounds.x);
		if (grabCorner != null) {
			fd.right = new FormAttachment(grabCorner, 0);
		} else {
			int padding = getShellPadding();
			fd.right = new FormAttachment(100, -padding);
		}
		fd.bottom = new FormAttachment(100, -5);
		statusLine.setLayoutData(fd);
		addUIControl(statusLine, "statusline"); //$NON-NLS-1$

		new LnFUpdater().updateUIControls(statusLine, true);
	}

	/**
	 * Initializes the given shell.
	 * 
	 * @param shell
	 *            shell to initialize
	 */
	private void initShell(final Shell shell) {
		shell.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND));
		shell.addPaintListener(new ShellPaintListener());

		String iconName = controller.getNavigationNode().getIcon();
		shell.setImage(ImageStore.getInstance().getImage(iconName));
		shell.setMinimumSize(getApplicationSizeMinimum());

		// prepare shell for binding
		addUIControl(shell, SHELL_RIDGET_PROPERTY);

		if (getShellRenderer() != null) {
			getShellRenderer().setShell(shell);
		}
		// TODO check if this is the main window. maybe support more then one workbench window.
		WidgetIdentificationSupport.setIdentification(shell);
	}

	/**
	 * Returns the menu manager of the main menu (menu bar).
	 * 
	 * @return menu manager
	 */
	private MenuManager getMenuManager() {
		return ((WorkbenchWindow) getWindowConfigurer().getWindow()).getMenuManager();
	}

	/**
	 * Create the composite that contains the:
	 * <ul>
	 * <li>shell title and title buttons</li>
	 * <li>the logo</li>
	 * <li>the sub application switcher</li>
	 * </ul>
	 * 
	 * @param parentShell
	 *            the parent shell (non null)
	 * @return the title composite (never null)
	 */
	private TitleComposite createTitleComposite(final Shell parentShell) {
		ApplicationNode node = (ApplicationNode) controller.getNavigationNode();
		return new TitleComposite(parentShell, node);
	}

	/**
	 * Creates and positions the corner to grab.
	 * 
	 * @param shell
	 */
	private GrabCorner createGrabCorner(final Shell shell) {

		if (GrabCorner.isResizeable() && LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.SHELL_HIDE_OS_BORDER)) {
			return new GrabCorner(shell, SWT.DOUBLE_BUFFERED);
		}

		return null;

	}

	/**
	 * Creates and positions the composite for the menu bar.
	 * 
	 * @param parent
	 *            parent of composite
	 * @param previous
	 *            previous composite in the layout
	 * @return composite
	 */
	private Composite createMenuBarComposite(Composite parent, Composite previous) {
		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		int padding = getShellPadding();

		// menu bar
		MenuCoolBarComposite composite = new MenuCoolBarComposite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		createMenuBar(composite);

		FormData formData = new FormData();
		formData.top = new FormAttachment(previous, getMenuBarTopMargin());
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		composite.setLayoutData(formData);

		return composite;
	}

	/**
	 * Creates a cool bar with menus.
	 * 
	 * @param parent
	 * @return cool bar with menus
	 */
	private void createMenuBar(MenuCoolBarComposite parent) {

		IContributionItem[] contribItems = getMenuManager().getItems();
		for (int i = 0; i < contribItems.length; i++) {
			if (contribItems[i] instanceof MenuManager) {
				MenuManager topMenuManager = (MenuManager) contribItems[i];
				parent.createAndAddMenu(topMenuManager);
			}
		}

	}

	/**
	 * Creates and positions the composite for the cool bar.
	 * 
	 * @param parent
	 *            parent of composite
	 * @param previous
	 *            previous composite in the layout
	 * @return composite
	 */
	private Composite createCoolBarComposite(Composite parent, Composite previous) {
		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		int padding = getCoolBarSeparatorPadding();

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData formData = new FormData();
		formData.top = new FormAttachment(previous);
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		separator.setLayoutData(formData);

		padding = getShellPadding();
		Composite result = new Composite(parent, SWT.NONE);
		result.setLayout(new FillLayout());
		formData = new FormData();
		formData.top = new FormAttachment(previous, getToolBarTopMargin());
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		result.setLayoutData(formData);

		Control control = getWindowConfigurer().createCoolBarControl(result);
		if (control instanceof CoolBar) {
			CoolBar coolbar = (CoolBar) control;
			CoolbarUtils.initCoolBar(coolbar, getToolbarFont());
		}
		return result;
	}

	private static Font getToolbarFont() {
		return LnfManager.getLnf().getFont(LnfKeyConstants.TOOLBAR_FONT);
	}

	private int getCoolBarSeparatorPadding() {

		ModuleGroupRenderer mgRenderer = (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.MODULE_GROUP_RENDERER);
		if (mgRenderer == null) {
			mgRenderer = new ModuleGroupRenderer();
		}
		int padding = mgRenderer.getModuleGroupPadding();

		EmbeddedBorderRenderer borderRenderer = (EmbeddedBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.SUB_MODULE_VIEW_BORDER_RENDERER);
		if (borderRenderer == null) {
			borderRenderer = new EmbeddedBorderRenderer();
		}
		padding += borderRenderer.getBorderWidth();

		padding += getShellPadding();

		return padding;
	}

	/**
	 * Creates the main composite.
	 * 
	 * @param parent
	 *            parent of composite
	 * @param previous
	 *            previous composite in the layout
	 * @return composite
	 */
	private Composite createMainComposite(Composite parent, Composite previous) {

		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		int padding = getShellPadding();

		Composite composite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		composite.setLayout(new FillLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(previous, LnfManager.getLnf().getIntegerSetting(
				LnfKeyConstants.TOOLBAR_WORK_AREA_VERTICAL_GAP), 0);
		formData.bottom = new FormAttachment(100, -padding);
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		composite.setLayoutData(formData);
		getWindowConfigurer().createPageComposite(composite);
		return composite;

	}

	/**
	 * Returns the padding between shell border and content.
	 * 
	 * @return padding
	 */
	private int getShellPadding() {

		ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		return borderRenderer.getCompleteBorderWidth();

	}

	/**
	 * Returns the renderer of the shell.
	 * 
	 * @return renderer
	 */
	private ShellRenderer getShellRenderer() {
		ShellRenderer shellRenderer = (ShellRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.TITLELESS_SHELL_RENDERER);
		return shellRenderer;
	}

	/**
	 * Returns the margin above the menu bar.
	 * 
	 * @return top margin
	 */
	private int getMenuBarTopMargin() {
		RienaDefaultLnf lnf = LnfManager.getLnf();
		return lnf.getIntegerSetting(LnfKeyConstants.MENUBAR_TOP_MARGIN, DEFAULT_COOLBAR_TOP_MARGIN);
	}

	/**
	 * Returns the margin above the tool bar.
	 * 
	 * @return top margin
	 */
	private int getToolBarTopMargin() {
		RienaDefaultLnf lnf = LnfManager.getLnf();
		return lnf.getIntegerSetting(LnfKeyConstants.TOOLBAR_TOP_MARGIN, DEFAULT_COOLBAR_TOP_MARGIN);
	}

	// helping classes
	//////////////////

	private class MyApplicationNodeListener extends ApplicationNodeListener {

		@Override
		public void filterAdded(IApplicationNode source, IUIFilter filter) {
			show();
		}

		@Override
		public void filterRemoved(IApplicationNode source, IUIFilter filter) {
			show();
		}

		private void show() {
			if (controller == null || controller.getNavigationNode() == null
					|| controller.getNavigationNode().isDisposed()) {
				return;
			}
			try {
				IViewPart vp = getNavigationViewPart();
				if (vp == null) {
					NavigationViewPart navi = (NavigationViewPart) getActivePage().showView(NavigationViewPart.ID);
					navi.updateNavigationSize();
				}
			} catch (PartInitException e) {
				throw new UIViewFailure(e.getMessage(), e);
			}
		}

		private IWorkbenchPage getActivePage() {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		}

		/**
		 * Returns the view part of the navigation.
		 * 
		 * @return view part of the navigation or
		 */
		private IViewPart getNavigationViewPart() {
			IViewReference[] references = getActivePage().getViewReferences();
			for (IViewReference viewReference : references) {
				if (viewReference.getId().equals(NavigationViewPart.ID)) {
					return viewReference.getView(true);
				}
			}
			return null;
		}

	}

	private class MySubApplicationNodeListener extends SubApplicationNodeListener {

		/**
		 * {@inheritDoc}
		 * <p>
		 * Shows the specified perspective (sub-application).
		 */
		@Override
		public void activated(ISubApplicationNode source) {
			if (source != null) {
				showPerspective(source);
				if (titleComposite != null) {
					// Redraw so that the active tab is displayed correct
					titleComposite.setRedraw(false);
					titleComposite.setRedraw(true);
				}
				prepare(source);
			}
			super.activated(source);
		}

		private void showPerspective(ISubApplicationNode source) {
			try {
				PlatformUI.getWorkbench().showPerspective(SwtViewProvider.getInstance().getSwtViewId(source).getId(),
						PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			} catch (WorkbenchException e) {
				throw new UIViewFailure(e.getMessage(), e);
			}
		}

		@Override
		public void disposed(ISubApplicationNode source) {
			SwtViewProvider viewProvider = SwtViewProvider.getInstance();
			String id = viewProvider.getSwtViewId(source).getId();
			IWorkbench workbench = PlatformUI.getWorkbench();
			IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
			IPerspectiveDescriptor perspDesc = registry.findPerspectiveWithId(id);
			workbench.getActiveWorkbenchWindow().getActivePage().closePerspective(perspDesc, false, false);
			viewProvider.unregisterSwtViewId(source);
		}
	}

	/**
	 * This listener of a module group ensures the preparation of nodes (if
	 * necessary).
	 */
	private class MyModuleGroupNodeListener extends ModuleGroupNodeListener {

		/**
		 * {@inheritDoc}
		 * <p>
		 * After activation of a module group prepare - if necessary - every
		 * child (sub module) node.
		 */
		@Override
		public void activated(IModuleGroupNode source) {
			prepare(source);
			super.activated(source);
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * After the parent of a module group changed prepare - if necessary -
		 * every child node.
		 */
		@Override
		public void parentChanged(IModuleGroupNode source) {
			super.parentChanged(source);
			prepare(source);
		}

	}

	/**
	 * This listener of a module ensures the preparation of nodes (if
	 * necessary).
	 */
	private class MyModuleNodeListener extends ModuleNodeListener {

		/**
		 * {@inheritDoc}
		 * <p>
		 * After activation of a module prepare - if necessary - every child
		 * (sub module) node.
		 */
		@Override
		public void activated(IModuleNode source) {
			prepare(source);
			super.activated(source);
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * After the parent of a module changed prepare - if necessary - every
		 * child node.
		 */
		@Override
		public void parentChanged(IModuleNode source) {
			super.parentChanged(source);
			prepare(source);
		}

	}

	/**
	 * This listener of a sub module ensures the preparation of nodes (if
	 * necessary).
	 */
	private class MySubModuleNodeListener extends SubModuleNodeListener {

		/**
		 * {@inheritDoc}
		 * <p>
		 * After activation of a sub module prepare - if necessary - every child
		 * node.
		 */
		@Override
		public void activated(ISubModuleNode source) {
			prepare(source);
			super.activated(source);
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * After the parent of a sub module changed prepare - if necessary -
		 * every child node.
		 */
		@Override
		public void parentChanged(ISubModuleNode source) {
			super.parentChanged(source);
			prepare(source);
		}

	}

	/**
	 * Prepares every sub-module whose definition requires preparation.
	 * 
	 * @param node
	 *            navigation node
	 */
	private void prepare(INavigationNode<?> node) {

		if ((node == null) || (node.getParent() == null)) {
			return;
		}

		if (node instanceof ISubModuleNode) {
			ISubModuleNode subModuleNode = (ISubModuleNode) node;
			IWorkareaDefinition definition = WorkareaManager.getInstance().getDefinition(subModuleNode);
			if ((definition != null) && definition.isRequiredPreparation() && subModuleNode.isCreated()) {
				subModuleNode.prepare();
			}
		}

		/*
		 * The number of children can change while iterating. Only observe the
		 * node children !before! the iteration begins. Any child added while
		 * iterating will be handled automatically if preparation is required.
		 * Just ensure that there will be no concurrent modification of the
		 * children list while iterating over it. Conclusion is a copy..
		 */
		List<INavigationNode<?>> children = new ArrayList<INavigationNode<?>>(node.getChildren());
		for (INavigationNode<?> child : children) {
			prepare(child);
		}
	}

	/**
	 * This listener paints the shell (the border of the shell).
	 */
	private static class ShellPaintListener implements PaintListener {

		public void paintControl(PaintEvent e) {
			onPaint(e);
		}

		/**
		 * Paints the border of the (titleless) shell.
		 * 
		 * @param e
		 *            event
		 */
		private void onPaint(PaintEvent e) {
			if (e.getSource() instanceof Control) {
				Control shell = (Control) e.getSource();

				Rectangle shellBounds = shell.getBounds();
				Rectangle bounds = new Rectangle(0, 0, shellBounds.width, shellBounds.height);

				ILnfRenderer borderRenderer = LnfManager.getLnf().getRenderer(
						LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
				borderRenderer.setBounds(bounds);
				borderRenderer.paint(e.gc, null);
			}
		}
	}

	public IStatusLineContentFactory getStatuslineContentFactory() {
		return statuslineContentFactory;
	}

}