/*
 * Copyright (c) 2010, grossmann
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

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.controller.IParentListener;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.descriptor.setup.ITextFieldSetup;
import org.jowidgets.common.types.Markup;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.widgets.common.wrapper.AbstractInputControlSpiWrapper;
import org.jowidgets.spi.widgets.ITextControlSpi;
import org.jowidgets.tools.widgets.invoker.ColorSettingsInvoker;
import org.jowidgets.tools.widgets.invoker.VisibiliySettingsInvoker;

public class TextFieldImpl extends AbstractInputControlSpiWrapper implements ITextControl {

    private final ControlDelegate controlDelegate;

    public TextFieldImpl(final ITextControlSpi textInputWidgetSpi, final ITextFieldSetup setup) {
        super(textInputWidgetSpi);

        this.controlDelegate = new ControlDelegate(textInputWidgetSpi, this);

        if (setup.getText() != null) {
            setText(setup.getText());
        }

        if (setup.getMarkup() != null) {
            setMarkup(setup.getMarkup());
        }
        if (setup.getFontSize() != null) {
            setFontSize(Integer.valueOf(setup.getFontSize()));
        }
        if (setup.getFontName() != null) {
            setFontName(setup.getFontName());
        }

        setEditable(setup.isEditable());

        VisibiliySettingsInvoker.setVisibility(setup, this);
        ColorSettingsInvoker.setColors(setup, this);
    }

    @Override
    public ITextControlSpi getWidget() {
        return (ITextControlSpi) super.getWidget();
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
    public String getText() {
        return getWidget().getText();
    }

    @Override
    public void setText(final String text) {
        getWidget().setText(text);
    }

    @Override
    public void setFontSize(final int size) {
        getWidget().setFontSize(size);
    }

    @Override
    public void setFontName(final String fontName) {
        getWidget().setFontName(fontName);
    }

    @Override
    public void setMarkup(final Markup markup) {
        getWidget().setMarkup(markup);
    }

    @Override
    public void setSelection(final int start, final int end) {
        getWidget().setSelection(start, end);
    }

    @Override
    public void setCaretPosition(final int pos) {
        getWidget().setCaretPosition(pos);
    }

    @Override
    public int getCaretPosition() {
        return getWidget().getCaretPosition();
    }

    @Override
    public void selectAll() {
        final String text = getText();
        if (text != null) {
            setSelection(0, text.length());
        }
    }

    @Override
    public void select() {
        selectAll();
    }

}
