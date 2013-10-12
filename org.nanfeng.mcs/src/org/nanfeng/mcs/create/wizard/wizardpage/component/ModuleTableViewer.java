package org.nanfeng.mcs.create.wizard.wizardpage.component;

import java.util.Set;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.nanfeng.mcs.create.wizard.wizardpage.component.constructor.IModuleTableViewerConstructor;
import org.nanfeng.mcs.js.Constraint;
import org.nanfeng.mcs.js.Module;

/**
 * TableViewer used to build the visible area in wizard page.
 */
public class ModuleTableViewer extends TableViewer {

    private IModuleTableViewerConstructor constructor;

    /**
     * @param parent
     * @param constructor
     */
    public ModuleTableViewer(Composite parent, IModuleTableViewerConstructor constructor) {
        super(parent, SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        this.constructor = constructor;
        this.constructor.setModuleTableViewer(this);
    }

    /**
     * 
     */
    public void createTableViewer() {

        setViewerLayout();

        setHeaderVisible();
        setLinesVisible();

        constructor.setEachViewerHeader();
        constructor.setEachColumnWidthWithScale();

        constructor.setContentProvider();
        constructor.setLabelProvider();

    }

    private void setViewerLayout() {
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        layoutData.minimumWidth = 450;
        layoutData.grabExcessHorizontalSpace = true;
        this.getControl().setLayoutData(layoutData);
    }

    private void setLinesVisible() {
        this.getTable().setLinesVisible(true);
    }

    private void setHeaderVisible() {
        this.getTable().setHeaderVisible(true);
    }

    /**
     * @param modules
     */
    public void setModules(Set<Module> modules) {
        this.setInput(modules);
    }

    /**
     * @param cons
     */
    public void setConstraints(Set<Constraint> cons) {
        this.setInput(cons);
    }

    /**
     * @return Module
     */
    public Module getSelectedModule() {
        IStructuredSelection selection = (IStructuredSelection) this.getSelection();
        Module selectedModule = (Module) selection.getFirstElement();
        return selectedModule;
    }

    /**
     * @return Constraint
     */
    public Constraint getSelectedConstraint() {
        IStructuredSelection selection = (IStructuredSelection) this.getSelection();
        Constraint selectedModule = (Constraint) selection.getFirstElement();
        return selectedModule;
    }

}
