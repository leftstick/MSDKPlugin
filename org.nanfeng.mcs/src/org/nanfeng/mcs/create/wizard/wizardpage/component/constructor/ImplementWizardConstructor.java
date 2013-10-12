package org.nanfeng.mcs.create.wizard.wizardpage.component.constructor;

import java.util.ArrayList;

import org.nanfeng.mcs.create.wizard.wizardpage.ModuleElementsFillWizard;
import org.nanfeng.mcs.js.Closure;
import org.nanfeng.mcs.js.Module;

/**
 * This constructor will be used to build wizard in
 * <code>ModuleElementsFillWizard</code>
 */
public class ImplementWizardConstructor implements IWizardConstructor {

    private Closure closure;
    private ModuleElementsFillWizard wizard;

    /**
     * @param closure
     */
    public ImplementWizardConstructor(Closure closure) {
        this.closure = closure;
    }

    @Override
    public String getWizardName() {
        return "AddImplementingModule";
    }

    @Override
    public String getWizardTitle() {
        return "MCS Module";
    }

    @Override
    public String getWizardDescription() {
        return "Add implementing";
    }

    @Override
    public void onCompleted() {
        this.closure.getModuleDefinition().setImplementing(new ArrayList<Module>(wizard.getSelection()));
    }

    @Override
    public void setWizard(ModuleElementsFillWizard wizard) {
        this.wizard = wizard;
    }

    @Override
    public String getWizardLabel() {
        return "Implementing : ";
    }
}
