package org.nanfeng.mcs.util.gui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.nanfeng.mcs.js.Type;

/**
 * Bundle of most images used by the MCS Module plug-in.
 */
public class ModulePluginImages {

    private static Map<Type, Image> images = new HashMap<Type, Image>();

    private static final String NAME_PREFIX = "/icon";

    private static final String INTERFACE_DESC = NAME_PREFIX + "/" + "interface_obj.gif";
    private static final String CODE_DESC = NAME_PREFIX + "/" + "code_obj.gif";
    private static final String PACKAGE_DESC = NAME_PREFIX + "/" + "package_obj.gif";
    /**
     * <code>INTERFACE</code>
     */
    public static final Image INTERFACE = createImage(Type.INTERFACE);
    /**
     * <code>ABSTRACTION</code>
     */
    public static final Image ABSTRACTION = createImage(Type.ABSTRACTION);
    /**
     * <code>CODE</code>
     */
    public static final Image CODE = createImage(Type.CODE);

    /**
     * <code>PACKAGE</code>
     */
    public static final Image PACKAGE = createImage(Type.PACKAGE);

    private static Image createImage(Type key) {
        if (!images.containsKey(key)) {
            int adornmentFlags = 0;
            ImageDescriptor desc = null;

            switch (key) {
            case CODE:
                desc = ImageDescriptor.createFromURL(ModulePluginImages.class.getResource(CODE_DESC));
                break;
            case INTERFACE:
                desc = ImageDescriptor.createFromURL(ModulePluginImages.class.getResource(INTERFACE_DESC));
                break;
            case ABSTRACTION:
                desc = ImageDescriptor.createFromURL(ModulePluginImages.class.getResource(CODE_DESC));
                adornmentFlags |= JavaElementImageDescriptor.ABSTRACT;
                break;
            case PACKAGE:
                desc = ImageDescriptor.createFromURL(ModulePluginImages.class.getResource(PACKAGE_DESC));
                break;
            default:
                break;
            }
            images.put(key, new JavaElementImageDescriptor(desc, adornmentFlags, new Point(22, 16)).createImage());
        }
        return images.get(key);
    }

    /**
     * Get Image instance by specific Type
     * 
     * @param type
     * @return Image
     */
    public static Image getImageByType(Type type) {
        return createImage(type);
    }
}
