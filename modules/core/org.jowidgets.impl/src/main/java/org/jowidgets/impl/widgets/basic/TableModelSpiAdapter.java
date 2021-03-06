/*
 * Copyright (c) 2011, Nikolaus Moll
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.tools.controller.TableColumnModelObservable;
import org.jowidgets.util.Assert;

public class TableModelSpiAdapter implements ITableColumnModelSpi, ITableDataModel {

    private final ITableColumnModel columnModel;
    private final ITableDataModel dataModel;

    private final TableColumnModelObservable columnModelObservable;
    private ITableSpi table;

    private int[] modelToView;
    private int[] viewToModel; // visible List
    private final ArrayList<Integer> currentColumnPermutation;
    private boolean ignoreTablePermutationEvents;

    public TableModelSpiAdapter(final ITableColumnModel columnModel, final ITableDataModel dataModel) {
        Assert.paramNotNull(columnModel, "columnModel");
        Assert.paramNotNull(dataModel, "dataModel");
        this.columnModel = columnModel;
        this.dataModel = dataModel;
        this.ignoreTablePermutationEvents = false;
        this.currentColumnPermutation = new ArrayList<Integer>(columnModel.getColumnCount());
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            currentColumnPermutation.add(Integer.valueOf(i));
        }

        this.columnModelObservable = new TableColumnModelObservable();

        modelToView = new int[columnModel.getColumnCount()];
        updateMappings();

        for (int modelIndex = 0; modelIndex < columnModel.getColumnCount(); modelIndex++) {
            final ITableColumn column = columnModel.getColumn(modelIndex);
            if (!column.isVisible() && modelToView[modelIndex] >= 0) {
                final int index = hideColumn(modelIndex);
                columnModelObservable.fireColumnsRemoved(new int[] {index});
            }
        }

        // Delegate events from app model to spi model
        final ITableColumnModelObservable tableColumnModelObservable = columnModel.getTableColumnModelObservable();
        if (tableColumnModelObservable != null) {
            tableColumnModelObservable.addColumnModelListener(new ITableColumnModelListener() {

                @Override
                public void columnsRemoved(final int[] columnIndices) {
                    table.stopEditing();
                    final List<Integer> sortedIndices = getSortedList(columnIndices);
                    for (final int modelIndex : sortedIndices) {
                        final int removedIndex = modelToView[modelIndex];
                        removeColumnFromModel(modelIndex);
                        updateMappings();

                        ignoreTablePermutationEvents = true;
                        columnModelObservable.fireColumnsRemoved(new int[] {removedIndex});
                        if (table != null) {
                            table.setColumnPermutation(createTableColumnPermutation());
                        }
                        ignoreTablePermutationEvents = false;
                    }
                }

                @Override
                public void columnsChanged(final int[] columnIndices) {
                    table.stopEditing();
                    final List<Integer> sortedIndices = getSortedList(columnIndices);
                    for (final int modelIndex : sortedIndices) {
                        final ITableColumn column = columnModel.getColumn(modelIndex);
                        if (column.isVisible()) {
                            if (modelToView[modelIndex] < 0) {
                                final int index = showColumn(modelIndex);
                                ignoreTablePermutationEvents = true;
                                columnModelObservable.fireColumnsAdded(new int[] {index});
                                if (table != null) {
                                    table.setColumnPermutation(createTableColumnPermutation());
                                }
                                ignoreTablePermutationEvents = false;
                            }
                            else {
                                columnModelObservable.fireColumnsChanged(new int[] {modelToView[modelIndex]});
                            }

                        }
                        else if (modelToView[modelIndex] >= 0) {
                            final int index = hideColumn(modelIndex);
                            columnModelObservable.fireColumnsRemoved(new int[] {index});
                        }
                    }
                }

                @Override
                public void columnsAdded(final int[] columnIndices) {
                    table.stopEditing();
                    final List<Integer> sortedIndices = getSortedList(columnIndices);
                    for (final int modelIndex : sortedIndices) {
                        final ITableColumn column = columnModel.getColumn(modelIndex);
                        insertColumnToModel(modelIndex, column.isVisible() ? 1 : -1);
                        updateMappings();

                        if (column.isVisible()) {
                            ignoreTablePermutationEvents = true;
                            columnModelObservable.fireColumnsAdded(new int[] {modelToView[modelIndex]});
                            if (table != null) {
                                table.setColumnPermutation(createTableColumnPermutation());
                            }
                            ignoreTablePermutationEvents = false;
                        }
                    }
                }
            });

        }
    }

    public void setTable(final ITableSpi table) {
        Assert.paramNotNull(table, "table");
        this.table = table;
    }

    private void updateMappings() {
        final ArrayList<Integer> visibleColumns = new ArrayList<Integer>();
        for (int i = 0; i < modelToView.length; i++) {
            if (modelToView[i] >= 0) {
                modelToView[i] = visibleColumns.size();
                visibleColumns.add(i);
            }
            else {
                modelToView[i] = -1;
            }
        }

        viewToModel = new int[visibleColumns.size()];
        for (int i = 0; i < viewToModel.length; i++) {
            viewToModel[i] = visibleColumns.get(i);
        }
    }

    private int showColumn(final int columnIndex) {
        if (modelToView[columnIndex] >= 0) {
            throw new IllegalStateException("Column is already visible (" + columnIndex + ").");
        }

        modelToView[columnIndex] = 1;
        updateMappings();

        int viewIndex = 0;
        for (int i = 0; i < columnIndex; i++) {
            if (modelToView[i] >= 0) {
                viewIndex++;
            }
        }

        return viewIndex;
    }

    private int hideColumn(final int columnIndex) {
        if (modelToView[columnIndex] < 0) {
            throw new IllegalStateException("Column is already hidden (" + columnIndex + ").");
        }

        final int result = modelToView[columnIndex];
        modelToView[columnIndex] = -1;
        updateMappings();
        return result;
    }

    @Override
    public int getColumnCount() {
        return viewToModel.length;
    }

    @Override
    public ITableColumn getColumn(final int columnIndex) {
        return columnModel.getColumn(viewToModel[columnIndex]);
    }

    @Override
    public ITableColumnModelObservable getTableColumnModelObservable() {
        return columnModelObservable;
    }

    @Override
    public int getRowCount() {
        return dataModel.getRowCount();
    }

    @Override
    public ITableCell getCell(final int rowIndex, final int columnIndex) {
        return dataModel.getCell(rowIndex, viewToModel[columnIndex]);
    }

    @Override
    public ArrayList<Integer> getSelection() {
        return dataModel.getSelection();
    }

    @Override
    public void setSelection(final Collection<Integer> selection) {
        dataModel.setSelection(selection);
    }

    @Override
    public ITableDataModelObservable getTableDataModelObservable() {
        return dataModel.getTableDataModelObservable();
    }

    public int convertViewToModel(final int columnIndex) {
        return viewToModel[columnIndex];
    }

    public int convertModelToView(final int columnIndex) {
        return modelToView[columnIndex];
    }

    private ArrayList<Integer> createTableColumnPermutation() {
        final ArrayList<Integer> result = new ArrayList<Integer>(viewToModel.length);
        for (int i = 0; i < currentColumnPermutation.size(); i++) {
            final int modelIndex = currentColumnPermutation.get(i);
            if (modelToView[modelIndex] >= 0) {
                result.add(Integer.valueOf(modelToView[modelIndex]));
            }
        }

        return result;
    }

    List<Integer> convertColumnPermutationToView(final List<Integer> permutation) {
        final ArrayList<Integer> result = new ArrayList<Integer>();
        for (final Integer modelIndex : permutation) {
            if (modelToView[modelIndex] >= 0) {
                result.add(Integer.valueOf(modelToView[modelIndex]));
            }
        }

        return result;
    }

    private int getNextVisibleIndex(int position) {
        while (position < currentColumnPermutation.size()) {
            final int currentIndex = currentColumnPermutation.get(position);
            if (modelToView[currentIndex] >= 0) {
                return position;
            }
            position++;
        }

        return -1;
    }

    void tableColumnPermutationChanged(final ArrayList<Integer> tablePermutation) {
        if (ignoreTablePermutationEvents) {
            return;
        }

        int position = 0;
        for (final int tableColumnIndex : tablePermutation) {
            final int modelColumnIndex = viewToModel[tableColumnIndex];

            final int nextVisible = getNextVisibleIndex(position);
            if (currentColumnPermutation.get(nextVisible) == modelColumnIndex) {
                position = nextVisible + 1;
                continue;
            }

            currentColumnPermutation.remove(Integer.valueOf(modelColumnIndex));
            currentColumnPermutation.add(position, Integer.valueOf(modelColumnIndex));
            position++;
        }
    }

    ArrayList<Integer> getCurrentPermutation() {
        return currentColumnPermutation;
    }

    private void insertColumnToModel(final int index, final int value) {
        final int permutationIndex = currentColumnPermutation.indexOf(Math.max(0, index - 1)) + 1;

        for (int i = 0; i < currentColumnPermutation.size(); i++) {
            final int permutationValue = currentColumnPermutation.get(i);
            if (permutationValue >= index) {
                currentColumnPermutation.set(i, Integer.valueOf(permutationValue + 1));
            }
        }
        currentColumnPermutation.add(permutationIndex, Integer.valueOf(index));

        final int[] newModelToView = new int[modelToView.length + 1];
        for (int i = 0; i < index; i++) {
            newModelToView[i] = modelToView[i];
        }
        newModelToView[index] = value;
        for (int i = index; i < modelToView.length; i++) {
            newModelToView[i + 1] = modelToView[i];
        }
        modelToView = newModelToView;
    }

    private void removeColumnFromModel(final int index) {
        currentColumnPermutation.remove(Integer.valueOf(index));

        for (int i = 0; i < currentColumnPermutation.size(); i++) {
            final int permutationValue = currentColumnPermutation.get(i);
            if (permutationValue >= index) {
                currentColumnPermutation.set(i, Integer.valueOf(permutationValue - 1));
            }
        }

        final int[] newModelToView = new int[modelToView.length - 1];
        for (int i = 0; i < index; i++) {
            newModelToView[i] = modelToView[i];
        }
        for (int i = index + 1; i < modelToView.length; i++) {
            newModelToView[i - 1] = modelToView[i];
        }
        modelToView = newModelToView;
    }

    private List<Integer> getSortedList(final int[] array) {
        final ArrayList<Integer> result = new ArrayList<Integer>(array.length);
        for (int i = 0; i < array.length; i++) {
            result.add(Integer.valueOf(array[i]));
        }
        Collections.sort(result);
        return result;
    }
}
