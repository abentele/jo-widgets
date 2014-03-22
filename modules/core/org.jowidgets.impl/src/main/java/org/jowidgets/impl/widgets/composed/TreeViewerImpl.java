/*
 * Copyright (c) 2014, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.model.tree.ITreeNodeModelListener;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.ITreeViewer;
import org.jowidgets.api.widgets.descriptor.setup.ITreeViewerSetup;
import org.jowidgets.tools.model.tree.TreeNodeModelAdapter;
import org.jowidgets.tools.widgets.wrapper.TreeWrapper;
import org.jowidgets.util.Assert;

public final class TreeViewerImpl<ROOT_NODE_VALUE_TYPE> extends TreeWrapper implements ITreeViewer<ROOT_NODE_VALUE_TYPE> {

	private final ITreeNodeModel<ROOT_NODE_VALUE_TYPE> rootNodeModel;

	private final Map<ITreeContainer, ModelNodeBinding> bindings;

	public TreeViewerImpl(final ITree tree, final ITreeViewerSetup<ROOT_NODE_VALUE_TYPE> setup) {
		super(tree);
		Assert.paramNotNull(setup.getRootNodeModel(), "setup.getRootNodeModel()");
		this.rootNodeModel = setup.getRootNodeModel();
		this.bindings = new HashMap<ITreeContainer, TreeViewerImpl<ROOT_NODE_VALUE_TYPE>.ModelNodeBinding>();

		final ModelNodeBinding rootBinding = new ModelNodeBinding(tree, rootNodeModel);
		bindings.put(tree, rootBinding);

		rootNodeModel.addTreeNodeModelListener(new TreeNodeModelAdapter() {
			@Override
			public void dispose() {
				disposeBindings();
			}
		});

		tree.addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				disposeBindings();
			}
		});
	}

	private void disposeBindings() {
		for (final ModelNodeBinding binding : bindings.values()) {
			binding.dispose();
		}
	}

	@Override
	public ITreeNodeModel<ROOT_NODE_VALUE_TYPE> getRootNodeModel() {
		return rootNodeModel;
	}

	private final class ModelNodeBinding {

		private final ITreeContainer parentNode;
		private final ITreeNodeModel<?> parentNodeModel;

		private final ITreeNodeModelListener dataListener;
		private final ITreeNodeModelListener childrenListener;

		private ModelNodeBinding(final ITreeContainer parentNode, final ITreeNodeModel<?> parentNodeModel) {
			Assert.paramNotNull(parentNode, "parentNode");
			Assert.paramNotNull(parentNodeModel, "parentNodeModel");
			this.parentNode = parentNode;
			this.parentNodeModel = parentNodeModel;

			this.dataListener = new DataListener();
			this.childrenListener = new ChildrenListener();

			if (parentNode instanceof ITreeNode) {
				renderDataChanged(parentNodeModel, (ITreeNode) parentNode);
				parentNodeModel.addTreeNodeModelListener(dataListener);
			}

			onChildrenChanged();
			parentNodeModel.addTreeNodeModelListener(childrenListener);
		}

		private ITreeNodeModel<?> getParentNodeModel() {
			return parentNodeModel;
		}

		private void onChildrenChanged() {
			//Brute force, remove all nodes and add new ones
			for (final ITreeNode childNode : parentNode.getChildren()) {
				final ModelNodeBinding binding = bindings.remove(childNode);
				if (binding != null) {
					final ITreeNodeModel<?> childNodeModel = binding.getParentNodeModel();
					renderDisposeNode(childNodeModel, childNode);
					binding.dispose();
				}
			}
			parentNode.removeAllNodes();

			//than add the new nodes
			for (int i = 0; i < parentNodeModel.getChildrenCount(); i++) {
				final ITreeNodeModel<?> childNodeModel = parentNodeModel.getChildNode(i);
				final ITreeNode childNode = parentNode.addNode();
				renderNodeCreated(childNodeModel, childNode);
				final ModelNodeBinding childBinding = new ModelNodeBinding(childNode, childNodeModel);
				bindings.put(childNode, childBinding);

				if (childNodeModel.isExpanded()) {
					childNode.setExpanded(true);
				}

				if (childNodeModel.isChecked()) {
					childNode.setChecked(true);
				}

				if (childNodeModel.isSelected()) {
					childNode.setSelected(true);
				}
			}
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderNodeCreated(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().nodeCreated(model.getData(), node);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderDataChanged(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().dataChanged(model.getData(), node);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderDisposeNode(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().disposeNode(model.getData(), node);
		}

		private void dispose() {
			if (parentNode instanceof ITreeNode) {
				parentNodeModel.removeTreeNodeModelListener(dataListener);
			}
			parentNodeModel.removeTreeNodeModelListener(childrenListener);
		}

		private final class DataListener extends TreeNodeModelAdapter {
			@Override
			public void dataChanged() {
				renderDataChanged(parentNodeModel, (ITreeNode) parentNode);
			}
		}

		private final class ChildrenListener extends TreeNodeModelAdapter {
			@Override
			public void childrenChanged() {
				onChildrenChanged();
			}
		}

	}
}