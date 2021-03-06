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

package org.jowidgets.impl.widgets.common.wrapper;

import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.ITextLabelCommon;
import org.jowidgets.spi.widgets.ITextLabelSpi;

public abstract class AbstractTextLabelSpiWrapper extends AbstractControlSpiWrapper implements ITextLabelCommon {

    private String text;

    public AbstractTextLabelSpiWrapper(final ITextLabelSpi widget) {
        super(widget);
    }

    @Override
    public ITextLabelSpi getWidget() {
        return (ITextLabelSpi) super.getWidget();
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
    public void setText(final String text) {
        this.text = text;
        getWidget().setText(text);
    }

    public String getText() {
        return text;
    }

    protected void setTextCache(final String text) {
        this.text = text;
    }

}
