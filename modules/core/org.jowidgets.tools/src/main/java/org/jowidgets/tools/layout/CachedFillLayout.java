/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.tools.layout;

import org.jowidgets.api.layout.ICachedFillLayout;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.util.Assert;

/**
 * @deprecated Use {@link org.jowidgets.api.layout.CachedFillLayout} instead
 */
@Deprecated
public final class CachedFillLayout implements ICachedFillLayout {

    private final ICachedFillLayout original;

    public CachedFillLayout(final IContainer container) {
        Assert.paramNotNull(container, "container");
        this.original = Toolkit.getLayoutFactoryProvider().cachedFillLayout().create(container);
    }

    @Override
    public void layout() {
        original.layout();
    }

    @Override
    public void invalidate() {
        original.invalidate();
    }

    @Override
    public Dimension getPreferredSize() {
        return original.getPreferredSize();
    }

    @Override
    public Dimension getMinSize() {
        return original.getMinSize();
    }

    @Override
    public Dimension getMaxSize() {
        return original.getMaxSize();
    }

    @Override
    public void clearCache() {
        original.clearCache();
    }

}
