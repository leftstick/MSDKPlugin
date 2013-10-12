package org.nanfeng.mcs.create.wizard.wizardpage.component.constructor;

import java.util.ArrayList;

import org.nanfeng.mcs.create.wizard.wizardpage.ModuleElementsFillWizard;
import org.nanfeng.mcs.js.Closure;
import org.nanfeng.mcs.js.Module;

/**
 * This constructor will be used to build wizard in
 * <code>ModuleElementsFillWizard</code>
 */
public class DependencyWizardConstructor implements IWizardConstructor {

    private Closure closure;
    private ModuleElementsFillWizard wizard;

    /**
     * @param closure
     */
    public DependencyWizardConstructor(Closure closure) {
        this.closure = closure;
    }

    @Override
    public void setWizard(ModuleElementsFillWizard wizard) {
        this.wizard = wizard;
    }

    @Override
    public String getWizardName() {
        return "AddDependencyModule";
    }

    @Override
    public String getWizardTitle() {
        return "MCS Module";
    }

    @Override
    public String getWizardDescription() {
        return "Add dependencies";
    }

    @Override
    public void onCompleted() {
        this.closure.getModuleDefinition().setDependency(new ArrayList<Module>(wizard.getSelection()));
    }

    @Override
    public String getWizardLabel() {
        return "Dependency : ";
    }
}
