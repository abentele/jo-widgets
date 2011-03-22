/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.addons.testtool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jowidgets.addons.testtool.internal.IListModelListener;
import org.jowidgets.addons.testtool.internal.ListModel;
import org.jowidgets.addons.testtool.internal.TestDataObject;
import org.jowidgets.addons.testtool.internal.TestToolViewUtilities;
import org.jowidgets.addons.testtool.internal.UserAction;
import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableColumn;
import org.jowidgets.common.model.ITableColumnModel;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TableColumnPackPolicy;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.command.EnabledChecker;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.table.DefaultTableColumnBuilder;
import org.jowidgets.util.Assert;

public class TestToolView {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();
	private static final IActionBuilderFactory ABF = Toolkit.getActionBuilderFactory();
	private ISimpleTableModel tableDataModel;
	private final ITestTool testTool;
	private final TestToolViewUtilities viewUtilities;
	private IFrame frame;
	private ITable table;

	public TestToolView(final ITestTool testTool) {
		this.testTool = testTool;
		this.viewUtilities = new TestToolViewUtilities();
		createContentArea();
	}

	public void createContentArea() {
		final IFrameBluePrint frameBP = BPF.frame("TestTool").setSize(new Dimension(100, 400));
		frame = Toolkit.createRootFrame(frameBP);
		frame.setPosition(new Position(1700, 130));
		frame.setLayout(new MigLayoutDescriptor("[grow]", "[]"));
		createMenuBar(frame);
		createToolBar(frame);
		createTable(frame);
		setupTestTool();
		frame.pack();
		frame.setVisible(true);
	}

	private void createTable(final IFrame frame) {
		tableDataModel = Toolkit.getModelFactoryProvider().getTableModelFactory().simpleTableModel();
		final DefaultTableColumnBuilder colBuilder = new DefaultTableColumnBuilder();
		tableDataModel.addColumn(colBuilder.setText("step").build());
		tableDataModel.addColumn(colBuilder.setText("Widget").build());
		tableDataModel.addColumn(colBuilder.setText("User Action").build());
		tableDataModel.addColumn(colBuilder.setText("ID").build());

		final int columnCount = tableDataModel.getColumnCount();
		final Map<Integer, ITableColumn> columns = new HashMap<Integer, ITableColumn>();
		final ITableColumnModel columnModel = new ITableColumnModel() {

			@Override
			public int getColumnCount() {
				return columnCount;
			}

			@Override
			public ITableColumn getColumn(final int columnIndex) {
				ITableColumn result = columns.get(Integer.valueOf(columnIndex));
				if (result == null) {
					result = new ITableColumn() {

						private int width = 100;

						@Override
						public void setWidth(final int width) {
							this.width = width;
						}

						@Override
						public int getWidth() {
							return width;
						}

						@Override
						public String getToolTipText() {
							return "";
						}

						@Override
						public String getText() {
							switch (columnIndex) {
								case 0:
									return "Step";
								case 1:
									return "Widget";
								case 2:
									return "User Action";
								case 3:
									return "ID";
								default:
									return "Column " + columnIndex;
							}
						}

						@Override
						public IImageConstant getIcon() {
							return null;
						}

						@Override
						public AlignmentHorizontal getAlignment() {
							return null;
						}
					};
				}
				columns.put(Integer.valueOf(columnIndex), result);
				return result;
			}

			@Override
			public ITableColumnModelObservable getTableColumnModelObservable() {
				return null;
			}

		};

		final ITableBluePrint tableBluePrint = BPF.table(columnModel, tableDataModel);
		table = frame.add(tableBluePrint, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		table.pack(TableColumnPackPolicy.HEADER_AND_CONTENT);
	}

	private void createMenuBar(final IFrame frame) {
		final IMenuBarModel menuBarModel = frame.getMenuBarModel();
		final IMenuModel fileModel = new MenuModel("File");
		fileModel.setMnemonic('F');

		final IActionItemModel saveActionItem = fileModel.addActionItem("Save Test As...");
		saveActionItem.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				final List<TestDataObject> list = new LinkedList<TestDataObject>();
				if (!(tableDataModel.getRowCount() <= 0)) {
					for (int rowIndex = 0; rowIndex < tableDataModel.getRowCount(); rowIndex++) {
						final TestDataObject obj = new TestDataObject();
						obj.setType(tableDataModel.getCell(rowIndex, 1).getText());
						final String action = tableDataModel.getCell(rowIndex, 2).getText();
						obj.setAction(UserAction.valueOf(action));
						obj.setId(tableDataModel.getCell(rowIndex, 3).getText());
						list.add(obj);
					}
					final IInputDialog<String> dialog = viewUtilities.createInputDialog(frame, "Enter File Name", "file name");
					dialog.setVisible(true);
					if (dialog.isOkPressed()) {
						testTool.save(list, dialog.getValue());
					}
					dialog.dispose();
					Toolkit.getMessagePane().showInfo("Test successfully saved!");
				}
				else {
					Toolkit.getMessagePane().showWarning("There is nothing to save.");
				}
			}
		});
		final IActionItemModel loadActionItem = fileModel.addActionItem("Load Test...");
		loadActionItem.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				// TODO LG load TestDataObjects
			}
		});

		fileModel.addSeparator();
		fileModel.addActionItem("exit");
		menuBarModel.addMenu(fileModel);
	}

	private void createToolBar(final IFrame frame) {
		final IToolBar toolBar = frame.add(BPF.toolBar(), "north, wrap");
		final EnabledChecker playEnabledChecker = new EnabledChecker();
		final EnabledChecker stopEnabledChecker = new EnabledChecker();
		final EnabledChecker recordEnabledChecker = new EnabledChecker();

		final IActionBuilder playBuilder = ABF.create();
		final IToolBarButton playButton = toolBar.addItem(BPF.toolBarButton());
		playEnabledChecker.setEnabledState(EnabledState.ENABLED);
		final ICommandExecutor playCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				stopEnabledChecker.setEnabledState(EnabledState.ENABLED);
				recordEnabledChecker.setEnabledState(EnabledState.DISABLED);
				playEnabledChecker.setEnabledState(EnabledState.DISABLED);
				testTool.activateReplayMode();
				// TODO LG replay table content
			}
		};
		playBuilder.setCommand(playCommand, playEnabledChecker);
		playButton.setAction(playBuilder.setText("play").build());

		final IActionBuilder stopBuilder = ABF.create();
		final IToolBarButton stopButton = toolBar.addItem(BPF.toolBarButton());
		stopEnabledChecker.setEnabledState(EnabledState.DISABLED);
		final ICommandExecutor stopCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				playEnabledChecker.setEnabledState(EnabledState.ENABLED);
				recordEnabledChecker.setEnabledState(EnabledState.ENABLED);
				stopEnabledChecker.setEnabledState(EnabledState.DISABLED);
				testTool.deactivateReplayAndRecord();
			}
		};
		stopBuilder.setCommand(stopCommand, stopEnabledChecker);
		stopButton.setAction(stopBuilder.setText("stop").build());

		final IActionBuilder recordBuilder = ABF.create();
		final IToolBarButton recordButton = toolBar.addItem(BPF.toolBarButton());
		recordEnabledChecker.setEnabledState(EnabledState.ENABLED);
		final ICommandExecutor recordCommand = new ICommandExecutor() {

			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				playEnabledChecker.setEnabledState(EnabledState.DISABLED);
				stopEnabledChecker.setEnabledState(EnabledState.ENABLED);
				recordEnabledChecker.setEnabledState(EnabledState.DISABLED);
				testTool.activateRecordMode();
			}
		};
		recordBuilder.setCommand(recordCommand, recordEnabledChecker);
		recordButton.setAction(recordBuilder.setText("record").build());
	}

	private void setupTestTool() {
		@SuppressWarnings("unchecked")
		final ListModel<TestDataObject> listModel = (ListModel<TestDataObject>) testTool.getListModel();
		listModel.setListener(new IListModelListener<TestDataObject>() {

			@Override
			public void listChanged(final TestDataObject item) {
				Assert.paramNotNull(item, "item");
				tableDataModel.addRow(
						Integer.toString(tableDataModel.getRowCount()),
						item.getType(),
						item.getAction().name(),
						item.getId());
				table.pack(TableColumnPackPolicy.HEADER_AND_CONTENT);
				table.redraw();
			}
		});
	}
}