package org.nanfeng.mcs.create.wizard.wizardpage.component.factory;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.nanfeng.mcs.builtin.resource.ResourceLoader;
import org.nanfeng.mcs.create.wizard.wizardpage.component.ModuleSelectionDialog;

/**
 * A factory used to create instance of
 * <code>ModuleSelectionDialog</code>
 */
public class ModuleSelectionDialogFactory {
    private static Map<ResourceLoader, ModuleSelectionDialog> selectionDialogs = new HashMap<ResourceLoader, ModuleSelectionDialog>();

    /**
     * get the instance of <code>ModuleSelectionDialog</code>. If no
     * instance created before, create a new one. Otherwise, return
     * the exists one.
     * 
     * @param shell
     * @param loader
     * @return ModuleSelectionDialog
     */
    public static ModuleSelectionDialog getModuleSelectionDialog(Shell shell, ResourceLoader loader) {
        if (selectionDialogs.containsKey(loader)) {
            return selectionDialogs.get(loader);
        }
        ModuleSelectionDialog dialog = new ModuleSelectionDialog(shell, loader);
        selectionDialogs.put(loader, dialog);
        return dialog;
    }
}
