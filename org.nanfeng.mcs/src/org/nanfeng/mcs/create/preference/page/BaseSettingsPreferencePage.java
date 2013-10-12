package org.nanfeng.mcs.create.preference.page;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This PreferencePage is main description page of MCS settings.
 */
public class BaseSettingsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
    @Override
    protected Control createContents(Composite parent) {
        return new Composite(parent, SWT.NULL);
    }

    /**
     * @see IWorkbenchPreferencePage
     */
    @Override
    public void init(IWorkbench workbench) {
        //        
    }
}
