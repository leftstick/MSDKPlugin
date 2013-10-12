package org.nanfeng.mcs.js;

/**
 * An interface to describe how a JavaScript closure looks like.
 */
public interface Closure extends JSCreator {

    /**
     * @return the annotation of this closure. empty String should be
     *         returned if no annotation.
     */
    public String getComments();

    /**
     * @return the header part of this Closure. For example:
     * 
     * <pre>
     *     (function() {
     * </pre>
     */
    public String getHeader();

    /**
     * @param definition
     */
    public void setModuleDefinition(ModuleDefinition definition);

    /**
     * @return <code>ModuleDefinition</code>
     */
    public ModuleDefinition getModuleDefinition();

    /**
     * implementation part of this closure, if no implementation, keep
     * it empty is fine.
     */
    public void implementation();

    /**
     * @param value
     */
    public void setReturnValue(ReturnValue value);

    /**
     * @return <code>ReturnValue</code>
     */
    public ReturnValue getReturnValue();

    /**
     * @return the footer part of this Closure. For example:
     * 
     * <pre>
     *     });
     * </pre>
     */
    public String getFooter();
}
