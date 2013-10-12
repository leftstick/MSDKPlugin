package org.nanfeng.mcs.create.wizard;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.nanfeng.mcs.builtin.resource.ResourceLoaderFactory;
import org.nanfeng.mcs.create.wizard.wizardpage.ConstraintsWizard;
import org.nanfeng.mcs.create.wizard.wizardpage.ModuleDefinitionWizard;
import org.nanfeng.mcs.create.wizard.wizardpage.ModuleElementsFillWizard;
import org.nanfeng.mcs.create.wizard.wizardpage.component.constructor.DependencyTableViewerConstructor;
import org.nanfeng.mcs.create.wizard.wizardpage.component.constructor.DependencyWizardConstructor;
import org.nanfeng.mcs.create.wizard.wizardpage.component.constructor.ImplementTableViewerConstructor;
import org.nanfeng.mcs.create.wizard.wizardpage.component.constructor.ImplementWizardConstructor;
import org.nanfeng.mcs.js.Closure;
import org.nanfeng.mcs.js.ModuleDefinition;
import org.nanfeng.mcs.js.impl.ClosureImpl;
import org.nanfeng.mcs.js.impl.ReturnValueImpl;
import org.nanfeng.mcs.util.FileUtil;
import org.nanfeng.mcs.util.ModuleUtil;
import org.nanfeng.mcs.util.gui.WorkbenchUtil;

/**
 * <code>CreationWizard</code> will be used
 */
public class CreationWizard extends Wizard implements INewWizard {

    private ModuleDefinitionWizard first;
    private ModuleElementsFillWizard second;
    private ModuleElementsFillWizard third;
    private ConstraintsWizard forth;
    private Closure closure;
    private IPath path;
    private String modulePath;

    /**
     * <code>CreationWizard</code>.
     */
    public CreationWizard() {
        super();
        setWindowTitle("New MCS Module");
        setNeedsProgressMonitor(true);
        createClosureInstance();
        setInitSourceFolder();
    }

    private void createClosureInstance() {
        closure = new ClosureImpl();
        closure.setReturnValue(new ReturnValueImpl());
    }

    private void setInitSourceFolder() {

        Object objP = WorkbenchUtil.getActiveProject();
        if (objP == null) {
            return;
        }
        if (objP instanceof IProject) {
            IProject proj = (IProject) objP;
            this.path = proj.getLocation();
        } else if (objP instanceof IJavaProject) {
            IJavaProject proj = (IJavaProject) objP;
            this.path = proj.getResource().getLocation();
        } else if (objP instanceof IPackageFragment) {
            IPackageFragment proj = (IPackageFragment) objP;
            String mp = proj.getElementName();
            if (ModuleUtil.isValidModuleIdOrPath(mp)) {
                modulePath = mp;
            }
            IJavaProject javaProject = proj.getJavaProject();
            this.path = javaProject.getResource().getLocation();
        } else if (objP instanceof IPackageFragmentRoot) {
            IPackageFragmentRoot root = (IPackageFragmentRoot) objP;
            IJavaProject javaProject = root.getJavaProject();
            this.path = javaProject.getResource().getLocation();
        }

    }

    @Override
    public void addPages() {
        first = new ModuleDefinitionWizard(closure);
        first.setInitInfoFromActionPoint(path, modulePath);
        second = new ModuleElementsFillWizard(new ImplementWizardConstructor(closure),
                new ImplementTableViewerConstructor(), ResourceLoaderFactory.getImplementLoader());
        third = new ModuleElementsFillWizard(new DependencyWizardConstructor(closure),
                new DependencyTableViewerConstructor(), ResourceLoaderFactory.getDependencyLoader());
        forth = new ConstraintsWizard(closure);
        addPage(first);
        addPage(second);
        addPage(third);
        addPage(forth);
    }

    @Override
    public boolean performFinish() {
        // Print the result to the console
        first.fillModuleDefinition();
        setSelectedSourceFolder();
        second.onCompleted();
        third.onCompleted();
        forth.onCompleted();
        File outputJs = writeJS();
        openJS(outputJs);
        return true;
    }

    private void setSelectedSourceFolder() {
        path = first.getSelectedIPath();
    }

    private void openJS(File writeJS) {
        WorkbenchUtil.openEditor(writeJS);
    }

    private File writeJS() {
        File projectFolder = path.toFile();
        ModuleDefinition moduleDefinition = this.closure.getModuleDefinition();
        String idPath = ModuleUtil.moduleIdToPath(moduleDefinition.getModuleID(), moduleDefinition.getVersion(),
                moduleDefinition.getType());
        File jsFile = new File(projectFolder, idPath);
        String formattedJSCode = ModuleUtil.formatCode(this.closure.toJavaScript());
        FileUtil.writeInFile(formattedJSCode, jsFile);
        WorkbenchUtil.refreshProject(path.toFile().getName());
        return jsFile;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        //
    }
}
