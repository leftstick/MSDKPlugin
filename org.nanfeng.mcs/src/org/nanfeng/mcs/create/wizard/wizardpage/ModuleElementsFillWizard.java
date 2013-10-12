package org.nanfeng.mcs.create.wizard.wizardpage;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.nanfeng.mcs.builtin.resource.ResourceLoader;
import org.nanfeng.mcs.create.wizard.wizardpage.component.ModuleSelectionDialog;
import org.nanfeng.mcs.create.wizard.wizardpage.component.ModuleTableViewer;
import org.nanfeng.mcs.create.wizard.wizardpage.component.constructor.IModuleTableViewerConstructor;
import org.nanfeng.mcs.create.wizard.wizardpage.component.constructor.IWizardConstructor;
import org.nanfeng.mcs.create.wizard.wizardpage.component.factory.ModuleSelectionDialogFactory;
import org.nanfeng.mcs.js.Module;

/**
 * This wizard page used to build implementing, dependency modules in
 * the ModuleDefinition
 */
public class ModuleElementsFillWizard extends WizardPage {
    private Set<Module> selection;

    private Label label;

    private ModuleTableViewer viewer;
    private IWizardConstructor wizardConst;
    private IModuleTableViewerConstructor tableConst;

    private ResourceLoader loader;

    /**
     * @param wizardConst
     * @param tableConst
     * @param loader
     */
    public ModuleElementsFillWizard(IWizardConstructor wizardConst, IModuleTableViewerConstructor tableConst,
            ResourceLoader loader) {
        super(wizardConst.getWizardName());
        this.wizardConst = wizardConst;
        this.tableConst = tableConst;
        this.loader = loader;
        setTitle(this.wizardConst.getWizardTitle());
        setDescription(this.wizardConst.getWizardDescription());
        this.selection = new LinkedHashSet<Module>();
        this.wizardConst.setWizard(this);
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = createMainContainer(parent);
        setContainerLayout(container);

        createLabel(container, wizardConst.getWizardLabel());
        setLabelLayout();

        createModuleTableViewer(container);

        Composite vertical = createButtonContainer(container);
        setButtonContainerLayout(vertical);

        final Button addButton = createButton(vertical, "Add...");
        final Button removeButton = createButton(vertical, "Remove...");

        addEventHandlerToButtons(addButton, removeButton, vertical);
        addTableViewSelectionChangedListener(removeButton);

        // Required to avoid an error in the system
        setControl(container);
        setPageComplete(true);
        viewer.setModules(selection);
    }

    private void addTableViewSelectionChangedListener(final Button removeButton) {
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (viewer.getSelection().isEmpty()) {
                    removeButton.setEnabled(false);
                } else {
                    removeButton.setEnabled(true);
                }
            }
        });
    }

    private Composite createButtonContainer(Composite container) {
        Composite vertical = createMainContainer(container);
        return vertical;
    }

    private void setButtonContainerLayout(Composite vertical) {
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 1;
        layout2.marginWidth = 0;
        vertical.setLayout(layout2);
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.grabExcessHorizontalSpace = false;
        layoutData.widthHint = 100;
        vertical.setLayoutData(layoutData);
    }

    private void createLabel(Composite container, String txt) {
        label = new Label(container, SWT.NONE);
        label.setText(txt);
    }

    private Composite createMainContainer(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        return container;
    }

    private GridLayout createGridLayout(int cols) {
        GridLayout layout = new GridLayout(cols, false);
        return layout;
    }

    private Button createButton(Composite buttonContainer, String text) {
        Button button = new Button(buttonContainer, SWT.PUSH);
        button.setText(text);
        button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return button;
    }

    private void addEventHandlerToButtons(Button addButton, final Button removeButton, final Composite container) {
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addModuleInTableViewer(container);
            }
        });

        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeModuleFromTableViewer();
            }
        });

        removeButton.setEnabled(false);
    }

    private void createModuleTableViewer(Composite container) {
        viewer = new ModuleTableViewer(container, tableConst);
        viewer.createTableViewer();
    }

    private void setLabelLayout() {
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        label.setLayoutData(gridData);
    }

    private void setContainerLayout(Composite container) {
        GridLayout layout = createGridLayout(2);
        container.setLayout(layout);
        container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void addModuleInTableViewer(Composite container) {
        ModuleSelectionDialog dialog = ModuleSelectionDialogFactory.getModuleSelectionDialog(container.getShell(),
                loader);
        int returnCode = dialog.open();
        if (returnCode == Window.OK) {
            Module selectedModule = dialog.getSelectedModule();
            selection.add(selectedModule);
            viewer.refresh();
        }
    }

    private void removeModuleFromTableViewer() {
        Module selectedModule = viewer.getSelectedModule();
        if (selectedModule != null) {
            selection.remove(selectedModule);
            this.viewer.refresh();
        }
    }

    /**
     * @return set of added <code>Module</code>, whatever
     *         <code>DependencyModuleImpl</code> or
     *         <code>ImplementingModuleImpl</code>
     */
    public Set<Module> getSelection() {
        return selection;
    }

    /**
     * this will be called when user click finish button on the whole
     * wizard
     */
    public void onCompleted() {
        this.wizardConst.onCompleted();
    }
}
