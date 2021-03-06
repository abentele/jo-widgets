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

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.spi.impl.swing.common.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.common.util.ModifierConvert;
import org.jowidgets.spi.impl.swing.common.util.VirtualKeyConvert;
import org.jowidgets.spi.widgets.IMenuItemSpi;

public class MenuItemImpl extends SwingWidget implements IMenuItemSpi {

    public MenuItemImpl() {
        this(new JMenuItem());
    }

    public MenuItemImpl(final JMenuItem menuItem) {
        super(menuItem);
    }

    @Override
    public JMenuItem getUiReference() {
        return (JMenuItem) super.getUiReference();
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        getUiReference().setIcon(SwingImageRegistry.getInstance().getImageIcon(icon));
    }

    @Override
    public void setText(final String text) {
        getUiReference().setText(text);
    }

    @Override
    public void setToolTipText(final String text) {
        getUiReference().setToolTipText(text);
    }

    @Override
    public void setMnemonic(final char mnemonic) {
        getUiReference().setMnemonic(mnemonic);
    }

    public void setAccelerator(final Accelerator accelerator) {
        getUiReference().setAccelerator(getKeyStroke(accelerator));
    }

    private KeyStroke getKeyStroke(final Accelerator accelerator) {
        final int modfifier = ModifierConvert.convert(accelerator.getModifier());
        if (accelerator.getCharacter() != null) {
            return KeyStroke.getKeyStroke(accelerator.getCharacter(), modfifier);
        }
        else {
            return KeyStroke.getKeyStroke(VirtualKeyConvert.convert(accelerator.getVirtualKey()), modfifier);
        }
    }
}
