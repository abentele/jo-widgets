/*
 * Copyright (c) 2011, grossmann, Nikolaus Moll
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.impl.widgets.basic;

import java.util.ArrayList;
import java.util.List;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IListSelectionVetoListener;
import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.descriptor.ITableDescriptor;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.widgets.controller.ITableCellListener;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableCellPopupEvent;
import org.jowidgets.common.widgets.controller.ITableColumnListener;
import org.jowidgets.common.widgets.controller.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnPopupEvent;
import org.jowidgets.common.widgets.controller.ITableColumnResizeEvent;
import org.jowidgets.common.widgets.controller.ITableSelectionListener;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.widgets.common.wrapper.AbstractControlSpiWrapper;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.tools.controller.ListSelectionVetoObservable;
import org.jowidgets.tools.controller.TableSelectionObservable;
import org.jowidgets.tools.widgets.invoker.ColorSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.VisibiliySettingsInvoker;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.Interval;
import org.jowidgets.util.NullCompatibleEquivalence;

public class TableImpl extends AbstractControlSpiWrapper implements ITable {

    private static final TablePackPolicy DEFAULT_PACK_POLICY = TablePackPolicy.HEADER_AND_DATA_VISIBLE;

    private final ControlDelegate controlDelegate;
    private final ITableDataModel dataModel;
    private final ITableColumnModel columnModel;
    private final int maxPackWidth;

    private final TableCellObservableSpiAdapter cellObservable;
    private final TableCellPopupDetectionObservableSpiAdapter cellPopupDetectionObservable;
    private final TableColumnObservableSpiAdapter columnObservable;
    private final TableColumnPopupDetectionObservableSpiAdapter columnPopupDetectionObservable;
    private final TableSelectionObservable tableSelectionObservable;
    private final ListSelectionVetoObservable selectionVetoObservable;
    private final ITableSelectionListener tableSelectionListener;

    private final TableModelSpiAdapter modelSpiAdapter;

    private ArrayList<Integer> lastSelection;

    public TableImpl(final ITableSpi widgetSpi, final ITableDescriptor setup, final TableModelSpiAdapter modelSpiAdapter) {
        super(widgetSpi);

        this.controlDelegate = new ControlDelegate(widgetSpi, this);
        this.dataModel = setup.getDataModel();
        this.columnModel = setup.getColumnModel();
        this.maxPackWidth = setup.getColumnMaxPackWidth();
        this.modelSpiAdapter = modelSpiAdapter;

        this.cellObservable = new TableCellObservableSpiAdapter();
        this.selectionVetoObservable = new ListSelectionVetoObservable();
        this.tableSelectionObservable = new TableSelectionObservable();

        getWidget().setEditable(setup.isEditable());

        getWidget().addTableCellListener(new ITableCellListener() {

            @Override
            public void mouseReleased(final ITableCellMouseEvent event) {
                cellObservable.fireMouseReleased(event, modelSpiAdapter);
            }

            @Override
            public void mousePressed(final ITableCellMouseEvent event) {
                cellObservable.fireMousePressed(event, modelSpiAdapter);
            }

            @Override
            public void mouseDoubleClicked(final ITableCellMouseEvent event) {
                cellObservable.fireMouseDoubleClicked(event, modelSpiAdapter);
            }
        });

        this.cellPopupDetectionObservable = new TableCellPopupDetectionObservableSpiAdapter();
        getWidget().addTableCellPopupDetectionListener(new ITableCellPopupDetectionListener() {
            @Override
            public void popupDetected(final ITableCellPopupEvent event) {
                cellPopupDetectionObservable.firePopupDetected(event, modelSpiAdapter);
            }
        });

        this.columnObservable = new TableColumnObservableSpiAdapter();
        getWidget().addTableColumnListener(new ITableColumnListener() {

            @Override
            public void mouseClicked(final ITableColumnMouseEvent event) {
                columnObservable.fireMouseClicked(event, modelSpiAdapter);
            }

            @Override
            public void columnResized(final ITableColumnResizeEvent event) {
                columnObservable.fireColumnResized(event, modelSpiAdapter);
            }

            @Override
            public void columnPermutationChanged() {
                modelSpiAdapter.tableColumnPermutationChanged(getWidget().getColumnPermutation());
                columnObservable.fireColumnPermutationChanged();
            }
        });

        this.columnPopupDetectionObservable = new TableColumnPopupDetectionObservableSpiAdapter();
        getWidget().addTableColumnPopupDetectionListener(new ITableColumnPopupDetectionListener() {

            @Override
            public void popupDetected(final ITableColumnPopupEvent event) {
                columnPopupDetectionObservable.firePopupDetected(event, modelSpiAdapter);
            }
        });

        this.tableSelectionListener = new ITableSelectionListener() {
            @Override
            public void selectionChanged() {
                if (NullCompatibleEquivalence.equals(lastSelection, getWidget().getSelection())) {
                    //ignore selection already set
                    return;
                }
                if (selectionVetoObservable.allowSelectionChange(getWidget().getSelection())) {
                    lastSelection = getWidget().getSelection();
                    dataModel.setSelection(getSelection());
                    tableSelectionObservable.fireSelectionChanged();
                }
                else {
                    getWidget().removeTableSelectionListener(tableSelectionListener);
                    try {
                        getWidget().setSelection(lastSelection);
                    }
                    finally {
                        getWidget().addTableSelectionListener(tableSelectionListener);
                    }
                }
            }
        };
        getWidget().addTableSelectionListener(tableSelectionListener);

        VisibiliySettingsInvoker.setVisibility(setup, this);
        ColorSettingsInvoker.setColors(setup, this);

        resetFromModel();
        lastSelection = getWidget().getSelection();
    }

    @Override
    public ITableSpi getWidget() {
        return (ITableSpi) super.getWidget();
    }

    @Override
    public IContainer getParent() {
        return controlDelegate.getParent();
    }

    @Override
    public void setParent(final IContainer parent) {
        controlDelegate.setParent(parent);
    }

    @Override
    public void addParentListener(final IParentListener<IContainer> listener) {
        controlDelegate.addParentListener(listener);
    }

    @Override
    public void removeParentListener(final IParentListener<IContainer> listener) {
        controlDelegate.removeParentListener(listener);
    }

    @Override
    public boolean isReparentable() {
        return controlDelegate.isReparentable();
    }

    @Override
    public void addDisposeListener(final IDisposeListener listener) {
        controlDelegate.addDisposeListener(listener);
    }

    @Override
    public void removeDisposeListener(final IDisposeListener listener) {
        controlDelegate.removeDisposeListener(listener);
    }

    @Override
    public boolean isDisposed() {
        return controlDelegate.isDisposed();
    }

    @Override
    public void dispose() {
        controlDelegate.dispose();
    }

    @Override
    public IPopupMenu createPopupMenu() {
        return controlDelegate.createPopupMenu();
    }

    @Override
    public int getRowCount() {
        return dataModel.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return columnModel.getColumnCount();
    }

    @Override
    public boolean editCell(final int row, final int column) {
        return getWidget().editCell(row, modelSpiAdapter.convertModelToView(column));
    }

    @Override
    public void stopEditing() {
        getWidget().stopEditing();
    }

    @Override
    public void cancelEditing() {
        getWidget().cancelEditing();
    }

    @Override
    public boolean isEditing() {
        return getWidget().isEditing();
    }

    @Override
    public void resetFromModel() {
        getWidget().resetFromModel();
    }

    @Override
    public int convertColumnIndexToView(final int modelIndex) {
        final ArrayList<Integer> permutation = getColumnPermutation();
        return permutation.indexOf(Integer.valueOf(modelIndex));
    }

    @Override
    public int convertColumnIndexToModel(final int viewIndex) {
        return getColumnPermutation().get(viewIndex).intValue();
    }

    @Override
    public void moveColumn(final int oldViewIndex, final int newViewIndex) {
        final ArrayList<Integer> permutation = getColumnPermutation();
        final int oldPermutationValue = permutation.get(oldViewIndex).intValue();
        permutation.remove(oldViewIndex);
        permutation.add(newViewIndex, oldPermutationValue);
        setColumnPermutation(permutation);
    }

    @Override
    public void pack() {
        pack(DEFAULT_PACK_POLICY);
    }

    @Override
    public void pack(final int columnIndex) {
        pack(columnIndex, DEFAULT_PACK_POLICY);
    }

    @Override
    public void pack(final TablePackPolicy policy) {
        getWidget().pack(policy);
        afterPack(null);
    }

    @Override
    public void pack(final int columnIndex, final TablePackPolicy policy) {
        final int convertedIndex = modelSpiAdapter.convertModelToView(columnIndex);
        getWidget().pack(convertedIndex, policy);
        afterPack(columnIndex);
    }

    private void afterPack(final Integer columnIndex) {
        if (columnIndex != null) {
            final ITableColumn column = columnModel.getColumn(columnIndex.intValue());
            if (column.getWidth() > maxPackWidth) {
                column.setWidth(maxPackWidth);
            }
        }
        else {
            for (int i = 0; i < getColumnCount(); i++) {
                afterPack(Integer.valueOf(i));
            }
        }
    }

    @Override
    public void resetColumnPermutation() {
        final List<Integer> permutation = new ArrayList<Integer>(getColumnCount());
        for (int i = 0; i < getColumnCount(); i++) {
            permutation.add(Integer.valueOf(i));
        }
        setColumnPermutation(permutation);
    }

    @Override
    public void setEditable(final boolean editable) {
        getWidget().setEditable(editable);
    }

    @Override
    public Position getCellPosition(final int rowIndex, final int columnIndex) {
        return getWidget().getCellPosition(rowIndex, modelSpiAdapter.convertModelToView(columnIndex));
    }

    @Override
    public Dimension getCellSize(final int rowIndex, final int columnIndex) {
        return getWidget().getCellSize(rowIndex, modelSpiAdapter.convertModelToView(columnIndex));
    }

    @Override
    public ArrayList<Integer> getColumnPermutation() {
        return modelSpiAdapter.getCurrentPermutation();
    }

    @Override
    public void setColumnPermutation(final List<Integer> permutation) {
        getWidget().setColumnPermutation(modelSpiAdapter.convertColumnPermutationToView(permutation));
    }

    @Override
    public ArrayList<Integer> getSelection() {
        return lastSelection;
    }

    @Override
    public void setSelection(final List<Integer> selection) {
        getWidget().setSelection(selection);
    }

    @Override
    public void scrollToSelection() {
        final ArrayList<Integer> selection = getSelection();
        if (!EmptyCheck.isEmpty(selection)) {
            scrollToRow(selection.iterator().next());
        }
    }

    @Override
    public void scrollToEnd() {
        if (getRowCount() > 0) {
            scrollToRow(getRowCount() - 1);
        }
    }

    @Override
    public void scrollToRow(final int rowIndex) {
        getWidget().scrollToRow(rowIndex);
    }

    @Override
    public boolean isColumnPopupDetectionSupported() {
        return getWidget().isColumnPopupDetectionSupported();
    }

    @Override
    public Interval<Integer> getVisibleRows() {
        return getWidget().getVisibleRows();
    }

    @Override
    public void addTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
        cellPopupDetectionObservable.addTableCellPopupDetectionListener(listener);
    }

    @Override
    public void removeTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
        cellPopupDetectionObservable.removeTableCellPopupDetectionListener(listener);
    }

    @Override
    public void addTableCellListener(final ITableCellListener listener) {
        cellObservable.addTableCellListener(listener);
    }

    @Override
    public void removeTableCellListener(final ITableCellListener listener) {
        cellObservable.removeTableCellListener(listener);
    }

    @Override
    public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
        columnPopupDetectionObservable.addTableColumnPopupDetectionListener(listener);
    }

    @Override
    public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
        columnPopupDetectionObservable.removeTableColumnPopupDetectionListener(listener);
    }

    @Override
    public void addTableSelectionListener(final ITableSelectionListener listener) {
        tableSelectionObservable.addTableSelectionListener(listener);
    }

    @Override
    public void removeTableSelectionListener(final ITableSelectionListener listener) {
        tableSelectionObservable.removeTableSelectionListener(listener);
    }

    @Override
    public void addSelectionVetoListener(final IListSelectionVetoListener listener) {
        selectionVetoObservable.addSelectionVetoListener(listener);
    }

    @Override
    public void removeSelectionVetoListener(final IListSelectionVetoListener listener) {
        selectionVetoObservable.removeSelectionVetoListener(listener);
    }

    @Override
    public void addTableColumnListener(final ITableColumnListener listener) {
        columnObservable.addTableColumnListener(listener);
    }

    @Override
    public void removeTableColumnListener(final ITableColumnListener listener) {
        columnObservable.removeTableColumnListener(listener);
    }

    @Override
    public void setRowHeight(final int height) {
        getWidget().setRowHeight(height);

    }
}
