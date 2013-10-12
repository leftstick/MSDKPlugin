package org.nanfeng.mcs.js;

import java.util.Map;

/**
 * An interface indicates What constitutes a module in implementing,
 * dependency phase.
 */
public interface Module extends JSCreator {
    /**
     * @return Artifact
     */
    public String getArtifact();

    /**
     * @return id of this module.
     */
    public String getId();

    /**
     * @return namespace of this module.
     */
    public String getNamespace();

    /**
     * @return name of this module.
     */
    public String getName();

    /**
     * @return full file name of this module.
     */
    public String getFile();

    /**
     * @return alias of this module.
     */
    public String getAlias();

    /**
     * @return version of this module
     */
    public int[] getVersion();

    /**
     * @return a map indicates the publics part of this module. key is
     *         attribute name which will be implemented in the
     *         implementation module. value is either an array of
     *         argument name for this attribute if this attribute is
     *         an method, or type of this attribute if this attribute
     *         is a property.
     */
    public Map<String, Object> getPublics();

    /**
     * @return a map indicates the publics part of this module. key is
     *         property name which will be implemented in the
     *         implementation module. value is type of this property.
     */
    public Map<String, Object> getStatics();

    /**
     * @param isOptional
     */
    public void setOptional(boolean isOptional);

    /**
     * @return optional state of this module
     */
    public boolean getOptional();

    /**
     * @param isDissableIfMissing
     */
    public void setDisableIfMissing(boolean isDissableIfMissing);

    /**
     * @return dissableIfMissing of this module
     */
    public boolean getDisableIfMissing();

    /**
     * @return type of this module.
     */
    public Type getType();
}
