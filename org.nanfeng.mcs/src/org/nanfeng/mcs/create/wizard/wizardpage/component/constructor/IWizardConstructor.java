package org.nanfeng.mcs.create.wizard.wizardpage.component.constructor;

import org.nanfeng.mcs.create.wizard.wizardpage.ModuleElementsFillWizard;

/**
 * An interface to defines how to build up a wizard.
 */
public interface IWizardConstructor {

    /**
     * @param wizard
     */
    public void setWizard(ModuleElementsFillWizard wizard);

    /**
     * @return name of this wizard
     */
    public String getWizardName();

    /**
     * @return title of this wizard
     */
    public String getWizardTitle();

    /**
     * @return description of this wizard
     */
    public String getWizardDescription();

    /**
     * @return label which will be displayed on above specified
     *         <code>ModuleElementsFillWizard</code>
     */
    public String getWizardLabel();

    /**
     * What need to be done when this wizard is finished
     */
    public void onCompleted();
}
