package org.nanfeng.mcs.create.wizard.wizardpage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.nanfeng.mcs.create.wizard.wizardpage.component.ModuleTableViewer;
import org.nanfeng.mcs.create.wizard.wizardpage.component.constructor.ConstraintTableViewerConstructor;
import org.nanfeng.mcs.js.Closure;
import org.nanfeng.mcs.js.Constraint;

/**
 * Wizard page for collecting constraints from end-user's input
 */
public class ConstraintsWizard extends WizardPage {
    private Set<Constraint> selection;

    private ModuleTableViewer viewer;

    private Text inputKey;
    private Text inputValue;

    private Closure closure;

    /**
     * @param closure
     */
    public ConstraintsWizard(Closure closure) {
        super("ConstraintsWizard");
        this.closure = closure;
        setTitle("MCS Module");
        setDescription("Add constraints");
        this.selection = new LinkedHashSet<Constraint>();
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = createContainer(parent);
        setContainerLayout(container);

        Composite inputContainer = createContainer(container);
        setInputContainerLayout(inputContainer);
        createLabel(inputContainer, "Constraint : ");
        createInputs(inputContainer);
        Button addButton = createButton(inputContainer, "Add...");
        initButtonStatus(addButton, false);
        addEventHandlerToAddButton(addButton);
        setInputChangedListener(addButton);

        createModuleTableViewer(container);

        Composite vertical = createContainer(container);
        setButtonContainerLayout(vertical);

        final Button removeButton = createRemoveButton(vertical);

        addEventHandlerToRemoveButton(removeButton);
        initButtonStatus(removeButton, false);
        addTableViewSelectionChangedListener(removeButton);

        // Required to avoid an error in the system
        setControl(container);
        setPageComplete(true);
        viewer.setConstraints(selection);
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

    private void initButtonStatus(Button button, boolean b) {
        button.setEnabled(b);
    }

    private Button createButton(Composite inputContainer, String txt) {
        Button button = new Button(inputContainer, SWT.PUSH);
        button.setText(txt);
        return button;
    }

    private void createInputs(Composite inputContainer) {
        inputKey = createInput(inputContainer);
        createLabel(inputContainer, " =");
        inputValue = createInput(inputContainer);
    }

    private void setInputContainerLayout(Composite inputContainer) {
        RowLayout rowlayout = new RowLayout();
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
        inputContainer.setLayout(rowlayout);
        inputContainer.setLayoutData(gridData);

    }

    private Composite createContainer(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        return container;
    }

    private GridLayout createGridLayout(int cols) {
        GridLayout layout = new GridLayout(cols, false);
        return layout;
    }

    private Button createRemoveButton(Composite buttonContainer) {
        Button removeButton = new Button(buttonContainer, SWT.PUSH);
        removeButton.setText("Remove...");
        removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return removeButton;
    }

    private void setInputChangedListener(final Button addButton) {
        inputKey.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                verifyInputText(addButton);
            }
        });

        inputValue.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                verifyInputText(addButton);
            }
        });
    }

    private void addEventHandlerToRemoveButton(final Button removeButton) {

        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeConstraintFromTableViewer();
            }
        });
    }

    private void createModuleTableViewer(Composite container) {
        viewer = new ModuleTableViewer(container, new ConstraintTableViewerConstructor());
        viewer.createTableViewer();
    }

    private void addEventHandlerToAddButton(Button button) {

        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addConstraintInTableViewer();
            }
        });
    }

    private Label createLabel(Composite parent, String txt) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(txt);
        return label;
    }

    private Text createInput(Composite inputContainer) {
        Text txt = new Text(inputContainer, SWT.BORDER | SWT.SINGLE);
        return txt;
    }

    private void setContainerLayout(Composite container) {
        GridLayout layout = createGridLayout(2);
        container.setLayout(layout);
        container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void addConstraintInTableViewer() {
        if (!inputKey.getText().trim().isEmpty() && !inputValue.getText().trim().isEmpty()) {
            String key = inputKey.getText().trim();
            String value = inputValue.getText().trim();
            this.selection.add(new Constraint(key, value));
            viewer.refresh();
            inputKey.setText("");
            inputValue.setText("");
        }
    }

    private void removeConstraintFromTableViewer() {
        Constraint selectedModule = viewer.getSelectedConstraint();
        if (selectedModule != null) {
            selection.remove(selectedModule);
            this.viewer.refresh();
        }
    }

    /**
     * @return set of added <code>Constraint</code>
     */
    public Set<Constraint> getSelection() {
        return selection;
    }

    /**
     * this will be called when user click finish button on the whole
     * wizard
     */
    public void onCompleted() {
        closure.getModuleDefinition().setConstraints(new ArrayList<Constraint>(this.selection));
    }

    private void verifyInputText(Button addButton) {
        if (inputKey.getText().trim().isEmpty() || inputValue.getText().trim().isEmpty()) {
            addButton.setEnabled(false);
        } else {
            addButton.setEnabled(true);
        }
    }
}
