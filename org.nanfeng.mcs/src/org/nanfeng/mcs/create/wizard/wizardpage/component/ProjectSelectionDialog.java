package org.nanfeng.mcs.create.wizard.wizardpage.component;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * This dialog used to select a project as a source folder
 */
public class ProjectSelectionDialog extends ElementTreeSelectionDialog {

    private static ProjectSelectionDialog instance;

    /**
     * @param parent
     * @return ProjectSelectionDialog
     */
    public static ProjectSelectionDialog instance(Shell parent) {
        if (instance == null) {
            instance = new ProjectSelectionDialog(parent);
        }
        return instance;
    }

    private ProjectSelectionDialog(Shell parent) {
        super(parent, new WorkbenchLabelProvider(), new CustomWorkbenchContentProvider());
        this.setAllowMultiple(false);
        this.setTitle("Project selection");
        this.setMessage("Select the project from following list:");
        this.setInput(ResourcesPlugin.getWorkspace().getRoot());
    }

    /**
     * @return IProject
     */
    public IProject getSelectedProject() {
        Object firstResult = this.getFirstResult();
        return (IProject) firstResult;
    }

    /**
     * override the BaseWorkbenchContentProvider to disable displaying
     * the children
     */
    static class CustomWorkbenchContentProvider extends BaseWorkbenchContentProvider {
        @Override
        public boolean hasChildren(Object element) {
            return false;
        }
    }
}
