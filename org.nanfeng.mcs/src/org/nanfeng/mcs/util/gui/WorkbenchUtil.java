package org.nanfeng.mcs.util.gui;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Static utility methods for manipulating workbench
 */
public class WorkbenchUtil {

    /**
     * @return ActiveWorkbench
     */
    public static IWorkbenchWindow getActiveWorkbench() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    }

    /**
     * @return ActiveSelection
     */
    public static ISelection getActiveSelection() {
        return getActiveWorkbench().getSelectionService().getSelection();
    }

    /**
     * @return IWorkspaceRoot
     */
    public static IWorkspaceRoot getWorkspaceRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

    /**
     * @return ActiveProject
     */
    public static Object getActiveProject() {
        ISelection activeSelection = WorkbenchUtil.getActiveSelection();
        if ((activeSelection == null) || activeSelection.isEmpty()) {
            return null;
        }

        if (!(activeSelection instanceof IStructuredSelection)) {
            return null;
        }
        IStructuredSelection selection = (IStructuredSelection) activeSelection;
        if (selection.size() != 1) {
            return null;
        }
        return selection.getFirstElement();
    }

    /**
     * @param name name of a project
     * @return IPath
     */
    public static IPath getPathByProjectName(String name) {
        return getWorkspaceRoot().getProject(name).getLocation();
    }

    /**
     * @param name name of a project
     * @return IProject
     */
    public static IProject getProjectByProjectName(String name) {
        return getWorkspaceRoot().getProject(name);
    }

    /**
     * @param name name of a project
     * @return true if the given project name is valid. Otherwise
     *         false
     */
    public static boolean isValidProjectName(String name) {
        IProject proj = null;
        try {
            proj = getProjectByProjectName(name);
        } catch (Exception e) {
            return false;
        }
        return proj == null ? false : true;
    }

    /**
     * @return ActivePage
     */
    public static IWorkbenchPage getActivePage() {
        return getActiveWorkbench().getActivePage();
    }

    /**
     * Convert a <code>File</code> instance to an <code>IFile</code>
     * instance. Note: the file must be in the workbench(Not working
     * for external file).
     * 
     * @param file
     * @return IFile
     */
    public static IFile getIFile(File file) {
        IWorkspaceRoot root = getWorkspaceRoot();
        IPath location = Path.fromOSString(file.getAbsolutePath());
        IFile ifile = root.getFileForLocation(location);
        return ifile;
    }

    /**
     * get a default editor by the given <code>IFile</code>
     * 
     * @param file
     * @return IEditorDescriptor
     */
    public static IEditorDescriptor getDefaultEditor(IFile file) {
        IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
        return desc;

    }

    /**
     * Open the given <code>File</code> in its default editor view.
     * 
     * @param jsFile
     */
    public static void openEditor(File jsFile) {

        IFile file = WorkbenchUtil.getIFile(jsFile);
        if (file == null) {
            return;
        }
        try {
            IEditorDescriptor desc = WorkbenchUtil.getDefaultEditor(file);
            WorkbenchUtil.getActivePage().openEditor(new FileEditorInput(file), desc.getId());
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh a project in workbench by specifying a project name.
     * This method should be used after a new file created
     * programmatically to the project.
     * 
     * @param name
     */
    public static void refreshProject(String name) {
        try {
            IProject targetProj = WorkbenchUtil.getProjectByProjectName(name);
            targetProj.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
}
