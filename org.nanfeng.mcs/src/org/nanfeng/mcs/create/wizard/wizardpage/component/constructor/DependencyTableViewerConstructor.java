package org.nanfeng.mcs.create.wizard.wizardpage.component.constructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;
import org.nanfeng.mcs.create.wizard.wizardpage.component.ModuleTableViewer;
import org.nanfeng.mcs.js.Module;
import org.nanfeng.mcs.util.ModuleUtil;

/**
 * This constructor will be used to build tableViewer in
 * <code>ModuleElementsFillWizard</code>
 */
public class DependencyTableViewerConstructor implements IModuleTableViewerConstructor {

    private ModuleTableViewer viewer;

    private List<TableViewerColumn> columnViews;

    private ColumnInfo[] infos;

    /**
     */
    public DependencyTableViewerConstructor() {
        this.infos = createImplementColumnInfos();
        this.columnViews = new ArrayList<TableViewerColumn>();
    }

    @Override
    public void setEachViewerHeader() {
        setColumnHeaders(viewer, infos);
    }

    @Override
    public void setEachColumnWidthWithScale() {
        viewer.getTable().addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                final int tableWidth = viewer.getControl().getSize().x;
                for (int i = 0; i < viewer.getTable().getColumnCount(); i++) {
                    BigDecimal columnWidth = calculateWidthByScale(infos, tableWidth, i);
                    TableColumn column = viewer.getTable().getColumn(i);
                    column.setWidth(columnWidth.intValue());
                }
            }
        });

    }

    @Override
    public void setContentProvider() {
        viewer.setContentProvider(ArrayContentProvider.getInstance());
    }

    @Override
    public void setLabelProvider() {
        viewer.setLabelProvider(new ImplementLabelProvider());
        TableViewerColumn optionalCol = this.columnViews.get(2);
        optionalCol.setEditingSupport(new EditingSupport(viewer) {
            @Override
            protected void setValue(Object element, Object value) {
                ((Module) element).setOptional((boolean) value);
                viewer.update(element, null);
            }

            @Override
            protected Object getValue(Object element) {
                return ((Module) element).getOptional();
            }

            @Override
            protected CellEditor getCellEditor(Object element) {
                return new CheckboxCellEditor(viewer.getTable());
            }

            @Override
            protected boolean canEdit(Object element) {
                return true;
            }
        });

        TableViewerColumn disableCol = this.columnViews.get(3);
        disableCol.setEditingSupport(new EditingSupport(viewer) {
            @Override
            protected void setValue(Object element, Object value) {
                ((Module) element).setDisableIfMissing((boolean) value);
                viewer.update(element, null);
            }

            @Override
            protected Object getValue(Object element) {
                return ((Module) element).getDisableIfMissing();
            }

            @Override
            protected CellEditor getCellEditor(Object element) {
                return new CheckboxCellEditor(viewer.getTable());
            }

            @Override
            protected boolean canEdit(Object element) {
                return true;
            }
        });
    }

    private void setColumnHeaders(final ModuleTableViewer viewer, ColumnInfo[] infos) {
        for (ColumnInfo info : infos) {
            TableViewerColumn col = new TableViewerColumn(viewer, SWT.NONE);
            col.getColumn().setText(info.getText());
            col.getColumn().setMoveable(false);
            col.getColumn().setResizable(true);
            this.columnViews.add(col);
        }
    }

    private BigDecimal calculateWidthByScale(final ColumnInfo[] is, final int width, int i) {
        BigDecimal widthScale = new BigDecimal(is[i].getWidthScale());
        BigDecimal percentage = widthScale.divide(new BigDecimal(100));
        BigDecimal columnWidth = percentage.multiply(new BigDecimal(width));
        return columnWidth;
    }

    private ColumnInfo[] createImplementColumnInfos() {
        ColumnInfo[] is = new ColumnInfo[4];
        is[0] = new ColumnInfo("Id", 49);
        is[1] = new ColumnInfo("Version", 13);
        is[2] = new ColumnInfo("Optional", 15);
        is[3] = new ColumnInfo("DisableIfMissing", 23);
        return is;
    }

    /**
     * This LabelProvider will be used for rendering constraints in
     * <code>ModuleElementsFillWizard</code>.
     */
    class ImplementLabelProvider extends LabelProvider implements ITableLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof Module) {
                Module m = (Module) element;
                switch (columnIndex) {
                case 0:
                    String name = m.getId();
                    return name;
                case 1:
                    String version = ModuleUtil.versionToStr(m.getVersion());
                    return version;
                case 2:
                    String optional = getCheckMark(m.getOptional());
                    return optional;
                case 3:
                    String disableIfMissing = getCheckMark(m.getDisableIfMissing());
                    return disableIfMissing;
                default:
                    break;
                }
            }
            return null;
        }

        private String getCheckMark(boolean condition) {
            return condition ? "TRUE" : "FALSE";
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }

    @Override
    public void setModuleTableViewer(ModuleTableViewer viewer) {
        this.viewer = viewer;
    }
}
