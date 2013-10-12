/**
 * 
 */
package org.nanfeng.mcs.create.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.nanfeng.mcs.create.wizard.CreationWizard;

/**
 * @author ehaozuo
 */
public class CreateModuleCommand extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveShell(event);
        displayNewModuleWizard(shell);
        return null;
    }

    private int displayNewModuleWizard(Shell shell) {
        WizardDialog dialog = new WizardDialog(shell, new CreationWizard());
        return dialog.open();
    }
}
