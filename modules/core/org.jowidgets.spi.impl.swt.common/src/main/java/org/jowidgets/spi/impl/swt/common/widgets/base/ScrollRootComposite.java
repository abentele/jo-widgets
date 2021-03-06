/*
 * Copyright (c) 2015, MGrossmann
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

package org.jowidgets.spi.impl.swt.common.widgets.base;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.util.Assert;

public class ScrollRootComposite extends Composite implements IEnhancedLayoutable {

    private static final Point MIN_SIZE = new Point(0, 0);

    private LayoutMode layoutMode;

    public ScrollRootComposite(final Composite parent, final int style) {
        super(parent, style);
        this.layoutMode = LayoutMode.MIN_SIZE;
    }

    @Override
    public Point computeSize(final int wHint, final int hHint, final boolean changed) {
        if (layoutMode == LayoutMode.MIN_SIZE) {
            return MIN_SIZE;
        }
        else {
            return super.computeSize(wHint, hHint, changed);
        }
    }

    @Override
    public void setLayoutMode(final LayoutMode layoutMode) {
        Assert.paramNotNull(layoutMode, "layoutMode");
        this.layoutMode = layoutMode;
    }

}
