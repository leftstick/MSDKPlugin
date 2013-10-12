package org.nanfeng.mcs.js;

import java.util.List;

/**
 * An interface indicates what constitutes a module in
 * <code>Closure</code>.
 */
public interface ModuleDefinition extends JSCreator {
    /**
     * @param moduleID
     */
    public void setModuleID(String moduleID);

    /**
     * @return id of this module
     */
    public String getModuleID();

    /**
     * @param version
     */
    public void setVersion(int[] version);

    /**
     * @return version of this module
     */
    public abstract int[] getVersion();

    /**
     * @param type
     */
    public void setType(Type type);

    /**
     * @return type of this module
     */
    public abstract Type getType();

    /**
     * @param cons List of <code>Constraint</code> will be injected
     *            into this module
     */
    public void setConstraints(List<Constraint> cons);

    /**
     * @return list of <code>Constraint</code>
     */
    public abstract List<Constraint> getConstraints();

    /**
     * @param impls
     */
    public void setImplementing(List<Module> impls);

    /**
     * @param depends
     */
    public void setDependency(List<Module> depends);

    /**
     * @return list of <code>Module</code> will be injected into
     *         Closure's dependency phase
     */
    public List<Module> getDependencies();

    /**
     * @return list of <code>Module</code> will be injected into
     *         Closure's implementing phase
     */
    public List<Module> getImplementing();

}
