/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Component;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IMouseMotionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.widgets.IActionWidgetSpi;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public abstract class AbstractActionComponent extends AbstractActionWidget implements IActionWidgetSpi, IComponentSpi {

    private final SwingComponent swingComponentDelegate;

    public AbstractActionComponent(final Component component) {
        super(component);
        this.swingComponentDelegate = new SwingComponent(component);
    }

    @Override
    public void redraw() {
        swingComponentDelegate.redraw();
    }

    @Override
    public void setRedrawEnabled(final boolean enabled) {
        swingComponentDelegate.setRedrawEnabled(enabled);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        swingComponentDelegate.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        swingComponentDelegate.setBackgroundColor(colorValue);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return swingComponentDelegate.getForegroundColor();
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return swingComponentDelegate.getBackgroundColor();
    }

    @Override
    public void setCursor(final Cursor cursor) {
        swingComponentDelegate.setCursor(cursor);
    }

    @Override
    public void setVisible(final boolean visible) {
        swingComponentDelegate.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return swingComponentDelegate.isVisible();
    }

    @Override
    public Dimension getSize() {
        return swingComponentDelegate.getSize();
    }

    @Override
    public void setSize(final Dimension size) {
        swingComponentDelegate.setSize(size);
    }

    @Override
    public Position getPosition() {
        return swingComponentDelegate.getPosition();
    }

    @Override
    public void setPosition(final Position position) {
        swingComponentDelegate.setPosition(position);
    }

    @Override
    public IPopupMenuSpi createPopupMenu() {
        return swingComponentDelegate.createPopupMenu();
    }

    @Override
    public boolean requestFocus() {
        return swingComponentDelegate.requestFocus();
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        swingComponentDelegate.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        swingComponentDelegate.removeFocusListener(listener);
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        swingComponentDelegate.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        swingComponentDelegate.removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener mouseListener) {
        swingComponentDelegate.addMouseListener(mouseListener);
    }

    @Override
    public void removeMouseListener(final IMouseListener mouseListener) {
        swingComponentDelegate.removeMouseListener(mouseListener);
    }

    @Override
    public void addMouseMotionListener(final IMouseMotionListener listener) {
        swingComponentDelegate.addMouseMotionListener(listener);
    }

    @Override
    public void removeMouseMotionListener(final IMouseMotionListener listener) {
        swingComponentDelegate.removeMouseMotionListener(listener);
    }

    @Override
    public void addComponentListener(final IComponentListener componentListener) {
        swingComponentDelegate.addComponentListener(componentListener);
    }

    @Override
    public void removeComponentListener(final IComponentListener componentListener) {
        swingComponentDelegate.removeComponentListener(componentListener);
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        swingComponentDelegate.addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        swingComponentDelegate.removePopupDetectionListener(listener);
    }

}
