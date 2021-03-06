/*
 * Copyright (c) 2011, grossmann
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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.controller.IContainerListener;
import org.jowidgets.api.controller.IContainerRegistry;
import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IListenerFactory;
import org.jowidgets.api.controller.ITabItemListener;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.descriptor.ITabItemDescriptor;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.base.delegate.ContainerDelegate;
import org.jowidgets.impl.base.delegate.PopupMenuCreationDelegate;
import org.jowidgets.impl.base.delegate.PopupMenuCreationDelegate.IPopupFactory;
import org.jowidgets.impl.widgets.common.wrapper.AbstractContainerSpiWrapper;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.controller.ITabItemListenerSpi;
import org.jowidgets.tools.controller.ShowingStateObservable;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.tools.widgets.invoker.ColorSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.LayoutSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.VisibiliySettingsInvoker;
import org.jowidgets.util.Assert;

public class TabItemImpl extends AbstractContainerSpiWrapper implements ITabItem {

    private final ContainerDelegate containerDelegate;

    private final TabFolderImpl tabFolderImpl;

    private ITabFolder parent;
    private boolean detached;
    private boolean selected;
    private String text;
    private String toolTipText;
    private IImageConstant icon;

    private final Set<ITabItemListener> itemListeners;
    private final ITabItemListenerSpi tabItemListenerSpi;

    private final Set<IPopupDetectionListener> popupDetectionListeners;
    private final IPopupDetectionListener popupDetectionListener;

    private final Set<IPopupDetectionListener> tabPopupDetectionListeners;
    private final IPopupDetectionListener tabPopupDetectionListener;

    private final PopupMenuCreationDelegate tabPopupMenuCreationDelegate;

    private boolean onRemoveByDispose;

    public TabItemImpl(final ITabItemSpi widgetSpi, final ITabItemDescriptor descriptor, final TabFolderImpl tabFolderImpl) {
        super(widgetSpi);
        this.tabFolderImpl = tabFolderImpl;
        this.detached = false;
        this.selected = false;
        this.itemListeners = new HashSet<ITabItemListener>();
        this.parent = tabFolderImpl;
        this.onRemoveByDispose = false;

        VisibiliySettingsInvoker.setVisibility(descriptor, this);
        ColorSettingsInvoker.setColors(descriptor, this);
        LayoutSettingsInvoker.setLayout(descriptor, this);

        setText(descriptor.getText());
        setToolTipText(descriptor.getToolTipText());
        setIcon(descriptor.getIcon());

        this.tabItemListenerSpi = new ITabItemListenerSpi() {

            @Override
            public void selected() {
                setSelected(true);
            }

            @Override
            public void onClose(final IVetoable vetoable) {
                final VetoHolder vetoHolder = new VetoHolder();

                for (final ITabItemListener listener : itemListeners) {
                    listener.onClose(vetoHolder);
                    if (vetoHolder.hasVeto()) {
                        vetoable.veto();
                        break;
                    }
                }

            }

            @Override
            public void closed() {
                getWidget().removeTabItemListener(tabItemListenerSpi);
                if (selected) {
                    setSelected(false);
                }
                tabFolderImpl.itemClosed(TabItemImpl.this);
                for (final ITabItemListener listener : itemListeners) {
                    listener.closed();
                }
                tabPopupMenuCreationDelegate.dispose();
                containerDelegate.dispose();
            }

        };
        widgetSpi.addTabItemListener(tabItemListenerSpi);

        this.popupDetectionListeners = new HashSet<IPopupDetectionListener>();
        this.tabPopupDetectionListeners = new HashSet<IPopupDetectionListener>();

        this.popupDetectionListener = new IPopupDetectionListener() {
            @Override
            public void popupDetected(final Position position) {
                for (final IPopupDetectionListener listener : popupDetectionListeners) {
                    listener.popupDetected(position);
                }
            }
        };

        this.tabPopupDetectionListener = new IPopupDetectionListener() {
            @Override
            public void popupDetected(final Position position) {
                for (final IPopupDetectionListener listener : tabPopupDetectionListeners) {
                    listener.popupDetected(position);
                }
            }
        };

        widgetSpi.addPopupDetectionListener(popupDetectionListener);
        widgetSpi.addTabPopupDetectionListener(tabPopupDetectionListener);

        this.tabPopupMenuCreationDelegate = new PopupMenuCreationDelegate(new IPopupFactory() {
            @Override
            public IPopupMenu create() {
                return new PopupMenuImpl(getWidget().createTabPopupMenu(), TabItemImpl.this);
            }
        });
        this.containerDelegate = new ContainerDelegate(widgetSpi, this);
    }

    @Override
    public ITabItemSpi getWidget() {
        return (ITabItemSpi) super.getWidget();
    }

    @Override
    public boolean isShowing() {
        return !detached && parent != null && parent.isShowing() && isVisible() && isSelected();
    }

    @Override
    public boolean isSelected() {
        return this == parent.getSelectedItem();
    }

    boolean getSelectedState() {
        return selected;
    }

    protected boolean onDeselection(final ITabItem newSelected) {
        final VetoHolder vetoHolder = new VetoHolder();
        for (final ITabItemListener listener : itemListeners) {
            listener.onDeselection(vetoHolder);
        }
        tabFolderImpl.fireOnDeselection(vetoHolder, this, newSelected);
        if (vetoHolder.hasVeto()) {
            getWidget().removeTabItemListener(tabItemListenerSpi);
            tabFolderImpl.setSelectedItem(this);
            getWidget().addTabItemListener(tabItemListenerSpi);
            return true;
        }
        else {
            return false;
        }
    }

    protected void setSelected(final boolean selected) {
        if (this.getSelectedState() != selected) {
            this.selected = selected;
            if (selected) {
                //un-select other items if this item is selected 
                for (final TabItemImpl item : tabFolderImpl.getItemsImpl()) {
                    if (item != this && item.getSelectedState()) {
                        final boolean veto = item.onDeselection(this);
                        if (veto) {
                            this.selected = false;
                            return;
                        }
                        else {
                            item.setSelected(false);
                        }
                    }
                }
                tabFolderImpl.fireItemSelected(this);
            }
            for (final ITabItemListener listener : itemListeners) {
                listener.selectionChanged(selected);
            }
            final ShowingStateObservable showingStateObservable = getShowingStateObservableLazy();
            if (showingStateObservable != null) {
                showingStateObservable.fireShowingStateChanged(isShowing());
            }
        }
    }

    protected void setDetached(final boolean detached) {
        this.detached = detached;
    }

    @Override
    public boolean isDetached() {
        return detached;
    }

    @Override
    public void addTabItemListener(final ITabItemListener listener) {
        checkDetached();
        itemListeners.add(listener);
    }

    @Override
    public void removeTabItemListener(final ITabItemListener listener) {
        checkDetached();
        itemListeners.remove(listener);
    }

    @Override
    public void setText(final String text) {
        checkDetached();
        this.text = text;
        getWidget().setText(text);
    }

    @Override
    public void setToolTipText(final String toolTipText) {
        checkDetached();
        this.toolTipText = toolTipText;
        getWidget().setToolTipText(toolTipText);
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        checkDetached();
        this.icon = icon;
        getWidget().setIcon(icon);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getToolTipText() {
        return toolTipText;
    }

    @Override
    public IImageConstant getIcon() {
        return icon;
    }

    @Override
    public boolean isReparentable() {
        checkDetached();
        return getWidget().isReparentable();
    }

    @Override
    public ITabFolder getParent() {
        return parent;
    }

    @Override
    public void setParent(final ITabFolder parent) {
        this.parent = parent;
    }

    @Override
    public void addDisposeListener(final IDisposeListener listener) {
        containerDelegate.addDisposeListener(listener);
    }

    @Override
    public void removeDisposeListener(final IDisposeListener listener) {
        containerDelegate.removeDisposeListener(listener);
    }

    @Override
    public void select() {
        checkDetached();
        parent.setSelectedItem(this);
    }

    @Override
    public void dispose() {
        if (!isDisposed()) {
            if (getParent().getItems().contains(this) && !onRemoveByDispose) {
                onRemoveByDispose = true;
                getParent().removeItem(this);
                onRemoveByDispose = false;
            }
            else {
                tabPopupMenuCreationDelegate.dispose();
                containerDelegate.dispose();
            }
        }
    }

    @Override
    public boolean isDisposed() {
        return containerDelegate.isDisposed();
    }

    @Override
    public List<IControl> getChildren() {
        checkDetached();
        return containerDelegate.getChildren();
    }

    @Override
    public boolean remove(final IControl control) {
        checkDetached();
        return containerDelegate.remove(control);
    }

    @Override
    public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
        Assert.paramNotNull(layoutFactory, "layoutFactory");
        final LAYOUT_TYPE result = layoutFactory.create(this);
        setLayout(result);
        return result;
    }

    @Override
    public void addContainerListener(final IContainerListener listener) {
        containerDelegate.addContainerListener(listener);
    }

    @Override
    public void removeContainerListener(final IContainerListener listener) {
        containerDelegate.removeContainerListener(listener);
    }

    @Override
    public void addContainerRegistry(final IContainerRegistry registry) {
        containerDelegate.addContainerRegistry(registry);
    }

    @Override
    public void removeContainerRegistry(final IContainerRegistry registry) {
        containerDelegate.removeContainerRegistry(registry);
    }

    @Override
    public void addComponentListenerRecursive(final IListenerFactory<IComponentListener> listenerFactory) {
        containerDelegate.addComponentListenerRecursive(listenerFactory);
    }

    @Override
    public void removeComponentListenerRecursive(final IListenerFactory<IComponentListener> listenerFactory) {
        containerDelegate.removeComponentListenerRecursive(listenerFactory);
    }

    @Override
    public void addFocusListenerRecursive(final IListenerFactory<IFocusListener> listenerFactory) {
        containerDelegate.addFocusListenerRecursive(listenerFactory);
    }

    @Override
    public void removeFocusListenerRecursive(final IListenerFactory<IFocusListener> listenerFactory) {
        containerDelegate.removeFocusListenerRecursive(listenerFactory);
    }

    @Override
    public void addKeyListenerRecursive(final IListenerFactory<IKeyListener> listenerFactory) {
        containerDelegate.addKeyListenerRecursive(listenerFactory);
    }

    @Override
    public void removeKeyListenerRecursive(final IListenerFactory<IKeyListener> listenerFactory) {
        containerDelegate.removeKeyListenerRecursive(listenerFactory);
    }

    @Override
    public void addMouseListenerRecursive(final IListenerFactory<IMouseListener> listenerFactory) {
        containerDelegate.addMouseListenerRecursive(listenerFactory);
    }

    @Override
    public void removeMouseListenerRecursive(final IListenerFactory<IMouseListener> listenerFactory) {
        containerDelegate.removeMouseListenerRecursive(listenerFactory);
    }

    @Override
    public void addPopupDetectionListenerRecursive(final IListenerFactory<IPopupDetectionListener> listenerFactory) {
        containerDelegate.addPopupDetectionListenerRecursive(listenerFactory);
    }

    @Override
    public void removePopupDetectionListenerRecursive(final IListenerFactory<IPopupDetectionListener> listenerFactory) {
        containerDelegate.removePopupDetectionListenerRecursive(listenerFactory);
    }

    @Override
    public void layoutLater() {
        containerDelegate.layoutLater();
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final Object layoutConstraints) {
        checkDetached();
        return containerDelegate.add(index, descriptor, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
        checkDetached();
        return containerDelegate.add(index, descriptor);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final int index,
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final Object layoutConstraints) {
        checkDetached();
        return containerDelegate.add(index, creator, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
        final Object layoutConstraints) {
        checkDetached();
        return containerDelegate.add(descriptor, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
        final ICustomWidgetCreator<WIDGET_TYPE> creator,
        final Object layoutConstraints) {
        checkDetached();
        return containerDelegate.add(creator, layoutConstraints);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final int index, final ICustomWidgetCreator<WIDGET_TYPE> creator) {
        checkDetached();
        return containerDelegate.add(index, creator);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
        checkDetached();
        return containerDelegate.add(descriptor);
    }

    @Override
    public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final ICustomWidgetCreator<WIDGET_TYPE> creator) {
        checkDetached();
        return containerDelegate.add(creator);
    }

    @Override
    public void setTabOrder(final Collection<? extends IControl> tabOrder) {
        containerDelegate.setTabOrder(tabOrder);
    }

    @Override
    public void setTabOrder(final IControl... controls) {
        containerDelegate.setTabOrder(controls);
    }

    @Override
    public void removeAll() {
        checkDetached();
        containerDelegate.removeAll();
    }

    @Override
    public IPopupMenu createPopupMenu() {
        checkDetached();
        return containerDelegate.createPopupMenu();
    }

    @Override
    public IPopupMenu createTabPopupMenu() {
        checkDetached();
        return tabPopupMenuCreationDelegate.createPopupMenu();
    }

    @Override
    public void setLayout(final ILayoutDescriptor layoutDescriptor) {
        checkDetached();
        super.setLayout(layoutDescriptor);
    }

    @Override
    public void layoutBegin() {
        checkDetached();
        super.layoutBegin();
    }

    @Override
    public void layoutEnd() {
        checkDetached();
        super.layoutEnd();
    }

    @Override
    public void redraw() {
        checkDetached();
        super.redraw();
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        checkDetached();
        super.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        checkDetached();
        super.setBackgroundColor(colorValue);
    }

    @Override
    public IColorConstant getForegroundColor() {
        checkDetached();
        return super.getForegroundColor();
    }

    @Override
    public IColorConstant getBackgroundColor() {
        checkDetached();
        return super.getBackgroundColor();
    }

    @Override
    public void setCursor(final Cursor cursor) {
        checkDetached();
        super.setCursor(cursor);
    }

    @Override
    public void setVisible(final boolean visible) {
        checkDetached();
        super.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        checkDetached();
        return super.isVisible();
    }

    @Override
    public Dimension getSize() {
        checkDetached();
        return super.getSize();
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        popupDetectionListeners.add(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        popupDetectionListeners.remove(listener);
    }

    @Override
    public void addTabPopupDetectionListener(final IPopupDetectionListener listener) {
        tabPopupDetectionListeners.add(listener);
    }

    @Override
    public void removeTabPopupDetectionListener(final IPopupDetectionListener listener) {
        tabPopupDetectionListeners.remove(listener);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        checkDetached();
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        checkDetached();
        return super.isEnabled();
    }

    private void checkDetached() {
        if (detached) {
            throw new IllegalStateException("The item is detached.");
        }
    }

}
