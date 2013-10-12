package org.nanfeng.mcs.create.wizard.wizardpage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.mcs.create.wizard.wizardpage.component.ProjectSelectionDialog;
import org.nanfeng.mcs.js.Closure;
import org.nanfeng.mcs.js.ModuleDefinition;
import org.nanfeng.mcs.js.Type;
import org.nanfeng.mcs.js.impl.ModuleDefinitionImpl;
import org.nanfeng.mcs.util.Messages;
import org.nanfeng.mcs.util.ModuleUtil;
import org.nanfeng.mcs.util.StringUtil;
import org.nanfeng.mcs.util.gui.WorkbenchUtil;

/**
 * A wizard page to collect end-user's input for setting module
 * definition.
 */
public class ModuleDefinitionWizard extends WizardPage {

    private final String PREFIX = "com.zuohao.iptv.portal";

    private Composite container;

    private Closure closure;
    private IPath path;
    private String initModulePath;
    private ModuleDefinition definition;

    private Text source;
    private Button browser;
    private Text moduleId;
    private Combo majorVer;
    private Combo minorVer;
    private Combo type;

    /**
     * @param clos the <code>Closure</code> will be used for building
     *            JavaScript code
     */
    public ModuleDefinitionWizard(Closure clos) {
        super("Module Definition wizard");
        setTitle("MCS module");
        setDescription("Enter module basic information");
        this.closure = clos;
        this.definition = new ModuleDefinitionImpl();
    }

    /**
     * This method should be called once the end-user clicked the
     * "New Module" button from wherever it is.
     * 
     * @param path initialized IPath of source folder
     * @param modulePath initialized prefix of moduleId
     */
    public void setInitInfoFromActionPoint(IPath path, String modulePath) {
        this.path = path;
        this.initModulePath = modulePath;
    }

    @Override
    public void createControl(Composite parent) {
        createMainContainer(parent);
        setContainerLayout(container);

        createLabel("Source folder:");
        Composite horizontal = createSourceContainer(container);
        setSourceContainerLayout(horizontal);
        createSourceFolderInput(horizontal);
        addSourceFolderChangedListener();
        setSourceFolderInputLayout();
        createSourceFolderButton(horizontal);
        setSourceFolderButtonLayout();
        addBrowserEventHandler(horizontal.getShell());

        createLabel("Module ID:");
        createModuleIdInput();
        setModuleIdInputLayout();
        createModuleIdInputChangeListener();

        createLabel("Version:");
        Composite version = createVersionContainer();
        setVersionLayout(version);
        createVersionInput(version);

        createLabel("Type:");
        type = createType();

        setFocusOnModuleId();

        // Required to avoid an error in the system
        setControl(container);
        setPageComplete(false);

    }

    private Combo createVer(Composite parent, String t) {
        Combo ver = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        if ("minor".equals(t)) {
            ver.add("0");
        }
        ver.add("1");
        ver.add("2");
        ver.add("3");
        if ("minor".equals(t)) {
            ver.add("4");
            ver.add("5");
            ver.add("6");
        }
        ver.select(0);
        return ver;
    }

    private Combo createType() {
        Combo tp = new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Type[] values = Type.values();
        for (int i = 0; i < values.length; i++) {
            Type t = values[i];
            tp.add(t.getType());
            if (t == Type.CODE) {
                tp.select(i);
            }

        }
        return tp;
    }

    private Label createLabel(String title) {
        Label label = new Label(container, SWT.NONE);
        label.setText(title);
        return label;
    }

    private void createVersionInput(Composite version) {
        majorVer = createVer(version, "major");
        minorVer = createVer(version, "minor");
    }

    private void setVersionLayout(Composite version) {
        FillLayout fillLayout = new FillLayout();
        version.setLayout(fillLayout);
    }

    private Composite createVersionContainer() {
        Composite version = new Composite(container, SWT.NONE);
        return version;
    }

    private void setModuleIdInputLayout() {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        moduleId.setLayoutData(gd);

    }

    private void setSourceFolderButtonLayout() {
        GridData browserLayout = new GridData();
        browserLayout.grabExcessHorizontalSpace = false;
        browserLayout.widthHint = 100;
        browser.setLayoutData(browserLayout);
    }

    private void createSourceFolderButton(Composite horizontal) {
        browser = new Button(horizontal, SWT.PUSH);
        browser.setText("Browse...");
    }

    private void setSourceFolderInputLayout() {
        GridData sourceLayout = new GridData(GridData.FILL_HORIZONTAL);
        sourceLayout.minimumWidth = 200;
        sourceLayout.grabExcessHorizontalSpace = true;
        source.setLayoutData(sourceLayout);
    }

    private void setSourceContainerLayout(Composite horizontal) {
        GridLayout layout2 = new GridLayout(2, false);
        layout2.marginWidth = 0;
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        horizontal.setLayout(layout2);
        horizontal.setLayoutData(gridData);
    }

    private void createMainContainer(Composite parent) {
        container = new Composite(parent, SWT.NONE);
    }

    private void setFocusOnModuleId() {
        moduleId.setFocus();
        moduleId.setSelection(moduleId.getText().length());
    }

    private void createSourceFolderInput(Composite horizontal) {
        source = new Text(horizontal, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
        source.setBackground(new Color(null, new RGB(255, 255, 255)));
        if (path != null) {
            source.setText(path.toFile().getName());
        }
    }

    private void addSourceFolderChangedListener() {
        source.addVerifyListener(new VerifyListener() {
            @Override
            public void verifyText(VerifyEvent e) {
                verifySourceFolder(e.text);
            }
        });
    }

    private void addBrowserEventHandler(final Shell shell) {
        browser.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ProjectSelectionDialog dialog = ProjectSelectionDialog.instance(shell);
                int open = dialog.open();
                if (Window.OK == open) {
                    IProject selectedProject = dialog.getSelectedProject();
                    if (selectedProject != null) {
                        source.setText(selectedProject.getLocation().toFile().getName());
                    }
                }
            }
        });

    }

    private Composite createSourceContainer(Composite parent) {
        Composite horizontal = new Composite(parent, SWT.NONE);
        return horizontal;
    }

    private void createModuleIdInputChangeListener() {
        moduleId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!verifySourceFolder(source.getText())) {
                    return;
                }
                if (ModuleUtil.isValidModuleIdOrPath(moduleId.getText())) {
                    setPageComplete(true);
                    setErrorMessage(null);
                } else {
                    setPageComplete(false);
                    setErrorMessage(Messages.ERROR_MODULE_ID);
                }
            }
        });
    }

    private void createModuleIdInput() {
        moduleId = new Text(container, SWT.BORDER | SWT.SINGLE);
        if (StringUtil.isEmpty(initModulePath)) {
            moduleId.setText(PREFIX);
        } else {
            moduleId.setText(initModulePath);
        }
    }

    private void setContainerLayout(Composite composite) {

        GridLayout layout = createGridLayout(2);
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    }

    private GridLayout createGridLayout(int cols) {
        GridLayout layout = new GridLayout(cols, false);
        return layout;
    }

    /**
     * fill information in the closure, which will be used to build
     * the JavaScript code.
     */
    public void fillModuleDefinition() {
        this.definition.setModuleID(this.moduleId.getText());
        this.definition.setVersion(ModuleUtil.stringToVersion(majorVer.getItem(majorVer.getSelectionIndex()),
                minorVer.getItem(minorVer.getSelectionIndex())));

        this.definition.setType(Type.toType(type.getItem(type.getSelectionIndex())));
        this.closure.setModuleDefinition(this.definition);
    }

    /**
     * @return IPath of the chosen source folder
     */
    public IPath getSelectedIPath() {
        return WorkbenchUtil.getPathByProjectName(source.getText());
    }

    private boolean verifySourceFolder(String txt) {
        if (WorkbenchUtil.isValidProjectName(txt)) {
            setErrorMessage(null);
            return true;
        }
        setErrorMessage(Messages.ERROR_SOURCE_DOLDER);
        setPageComplete(false);
        return false;
    }

}
