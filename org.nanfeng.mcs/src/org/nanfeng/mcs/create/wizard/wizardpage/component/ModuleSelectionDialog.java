package org.nanfeng.mcs.create.wizard.wizardpage.component;

import java.text.Collator;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.nanfeng.mcs.activator.Activator;
import org.nanfeng.mcs.builtin.resource.ResourceLoader;
import org.nanfeng.mcs.js.Module;
import org.nanfeng.mcs.util.gui.ModulePluginImages;

/**
 * A selectionDialog will be used for implementing, dependency module
 * searching purpose.
 */
public class ModuleSelectionDialog extends FilteredItemsSelectionDialog {

    private static final String DIALOG_SETTINGS = "ModuleSelectionSettings";
    private Map<String, Module> modules;

    private ResourceLoader loader;

    /**
     * @param shell parent shell
     * @param loader resourceloader
     */
    public ModuleSelectionDialog(Shell shell, ResourceLoader loader) {
        super(shell);
        this.loader = loader;
        setTitle("Find Module");
        TypeItemLabelProvider listLabelProvider = new TypeItemLabelProvider();
        setListLabelProvider(listLabelProvider);
        setListSelectionLabelDecorator(listLabelProvider);
        setDetailsLabelProvider(new TypeItemDetailsLabelProvider());
        setSelectionHistory(new ResourceSelectionHistory());
    }

    private void loadModules() {
        if (modules == null) {
            modules = new LinkedHashMap<String, Module>();
        } else {
            modules.clear();
        }

        List<Module> mods = loader.loadMods();

        for (Module m : mods) {
            modules.put(m.getFile(), m);
        }
    }

    @Override
    public int open() {
        loadModules();

        int open = 0;
        open = super.open();
        return open;
    }

    @Override
    protected Control createExtendedContentArea(Composite parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = Activator.getDefault().getDialogSettings().getSection(DIALOG_SETTINGS);
        if (settings == null) {
            settings = Activator.getDefault().getDialogSettings().addNewSection(DIALOG_SETTINGS);
        }
        return settings;
    }

    /**
     * @return return the selected <code>Module</code> when you click
     *         OK on this dialog
     */
    public Module getSelectedModule() {
        return (Module) this.getFirstResult();
    }

    @Override
    protected IStatus validateItem(Object item) {
        return Status.OK_STATUS;
    }

    @Override
    protected ItemsFilter createFilter() {
        return new ItemsFilter() {
            @Override
            public boolean matchItem(Object item) {
                if (!(item instanceof Module)) {
                    return false;
                }
                Module m = (Module) item;
                return matches(m.getFile()) || matches(m.getName());
            }

            @Override
            public boolean isConsistentItem(Object item) {
                return true;
            }
        };
    }

    @Override
    protected Comparator<Module> getItemsComparator() {
        return new Comparator<Module>() {
            @Override
            public int compare(Module o1, Module o2) {
                return Collator.getInstance().compare(o1.getFile(), o2.getFile());
            }
        };
    }

    @Override
    protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
            IProgressMonitor progressMonitor) throws CoreException {

        progressMonitor.beginTask("Searching", modules.size()); //$NON-NLS-1$
        for (Entry<String, Module> m : modules.entrySet()) {
            contentProvider.add(m.getValue(), itemsFilter);
            progressMonitor.worked(1);
        }
        progressMonitor.done();
    }

    @Override
    public String getElementName(Object item) {
        if (!(item instanceof Module)) {
            return item.toString();
        }
        Module m = (Module) item;
        return m.getName();
    }

    private class ResourceSelectionHistory extends SelectionHistory {
        @Override
        protected Object restoreItemFromMemento(IMemento element) {
            if (modules.containsKey(element.getString("resource"))) {
                Module m = modules.get(element.getString("resource"));
                return m.getName() + " - " + m.getNamespace();
            }
            return null;
        }

        @Override
        protected void storeItemToMemento(Object item, IMemento element) {
            if (!(element instanceof Module)) {
                return;
            }
            Module m = (Module) element;
            element.putString("resource", m.getFile());
        }
    }

    /**
     * A <code>LabelProvider</code> for (the table of) types.
     */
    private class TypeItemLabelProvider extends LabelProvider implements ILabelDecorator {

        @Override
        public Image getImage(Object element) {
            if (!(element instanceof Module)) {
                return null;
            }
            Module m = (Module) element;
            return ModulePluginImages.getImageByType(m.getType());
        }

        @Override
        public String getText(Object element) {
            if (!(element instanceof Module)) {
                return null;
            }
            Module m = (Module) element;
            return m.getName();
        }

        @Override
        public Image decorateImage(Image image, Object element) {
            return image;
        }

        @Override
        public String decorateText(String text, Object element) {
            if (!(element instanceof Module)) {
                return "";
            }
            Module m = (Module) element;
            return m.getName() + " - " + m.getNamespace();
        }
    }

    /**
     * A <code>LabelProvider</code> for the label showing type
     * details.
     */
    private class TypeItemDetailsLabelProvider extends LabelProvider {

        /*
         * (non-Javadoc)
         * @see
         * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang
         * .Object)
         */
        @Override
        public Image getImage(Object element) {
            if (element instanceof Module) {
                return ModulePluginImages.PACKAGE;
            }

            return super.getImage(element);
        }

        /*
         * (non-Javadoc)
         * @see
         * org.eclipse.jface.viewers.LabelProvider#getText(java.lang
         * .Object)
         */
        @Override
        public String getText(Object element) {
            if (element instanceof Module) {
                return ((Module) element).getArtifact();
            }

            return super.getText(element);
        }
    }
}
