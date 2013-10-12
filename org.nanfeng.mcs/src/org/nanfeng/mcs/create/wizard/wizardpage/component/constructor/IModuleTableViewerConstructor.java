package org.nanfeng.mcs.create.wizard.wizardpage.component.constructor;

import org.nanfeng.mcs.create.wizard.wizardpage.component.ModuleTableViewer;


/**
 * A <code>IModuleTableViewerConstructor</code> used to build
 * <code>ModuleTableViewer</code>
 */
public interface IModuleTableViewerConstructor {

    /**
     * Implement this method to set header for each column in this
     * tableViewer
     */
    public void setEachViewerHeader();

    /**
     * Implement this method to set width as percentage for each
     * column in this tableViewer
     */
    public void setEachColumnWidthWithScale();

    /**
     * Implement this method to set a right contentProvider
     */
    public void setContentProvider();

    /**
     * Implement this method to set a right labelProvider
     */
    public void setLabelProvider();

    /**
     * set <code>ModuleTableViewer</code> which is the context of this
     * constructor
     * 
     * @param viewer
     */
    void setModuleTableViewer(ModuleTableViewer viewer);

}
