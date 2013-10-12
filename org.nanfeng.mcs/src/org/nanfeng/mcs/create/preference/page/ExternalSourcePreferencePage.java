package org.nanfeng.mcs.create.preference.page;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.nanfeng.mcs.activator.Activator;
import org.nanfeng.mcs.create.preference.component.IncludeFieldEditor;
import org.nanfeng.mcs.js.external.SourceScanRunner;
import org.nanfeng.mcs.util.StringUtil;

/**
 * Preference page for collecting preferences that indicates what
 * external source code will be scanned for building the module
 * resource
 */
public class ExternalSourcePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    private DirectoryFieldEditor baseDir;
    private IncludeFieldEditor includes;

    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        super.setDescription("This feature offers you to extend the modules to be selected in 'Find Module' Dialog in 'New MCS Module' wizard."
                + StringUtil.NEW_LINE
                + StringUtil.NEW_LINE
                + "If no Includes specified,  Default is all (recursive) files with a .js extention under given Base Dir.");
    }

    @Override
    protected void createFieldEditors() {
        baseDir = new DirectoryFieldEditor("BaseDir", "&Base Dir:", getFieldEditorParent());
        baseDir.setEmptyStringAllowed(false);
        baseDir.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
        addField(baseDir);
        includes = new IncludeFieldEditor("Includes", getFieldEditorParent());
        addField(includes);
    }

    @Override
    protected void performApply() {
        super.performOk();
        askForScan();
    }

    @Override
    public boolean performOk() {
        super.performOk();
        askForScan();
        return true;
    }

    private void askForScan() {
        SourceScanRunner runner = SourceScanRunner.getRunner();
        if (runner.isBusy()) {
            return;
        }
        MessageDialog msg = new MessageDialog(
                getShell(),
                "New Scan",
                null,
                "Would you like to force a new scan of the sources you specified by given Base Dir and it's Includes right now?",
                MessageDialog.CONFIRM, new String[] { "Yes", "No" }, Window.CANCEL);
        int open = msg.open();
        if (open == Window.OK) {
            runner.setBaseDir(this.baseDir.getStringValue());
            runner.setIncludes(this.includes.getIncludes());
            runner.run();
        }
    }

}
