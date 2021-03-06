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
package org.jowidgets.spi.impl.swing.common.util;

import org.jowidgets.common.types.Cursor;
import org.jowidgets.util.Assert;

public final class CursorConvert {

    private CursorConvert() {};

    public static java.awt.Cursor convert(final Cursor cursor) {
        Assert.paramNotNull(cursor, "cursor");

        if (cursor == Cursor.DEFAULT) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
        }
        else if (cursor == Cursor.WAIT) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR);
        }
        else if (cursor == Cursor.ARROW) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
        }
        else if (cursor == Cursor.CROSS) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR);
        }
        else if (cursor == Cursor.HAND) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
        }
        else if (cursor == Cursor.SIZEWE) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.W_RESIZE_CURSOR);
        }
        else if (cursor == Cursor.SIZENS) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR);
        }
        else if (cursor == Cursor.SIZENE) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR);
        }
        else if (cursor == Cursor.SIZESE) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR);
        }
        else if (cursor == Cursor.SIZESW) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR);
        }
        else if (cursor == Cursor.SIZENW) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR);
        }
        else if (cursor == Cursor.ARROW) {
            return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
        }
        else {
            throw new IllegalArgumentException("Cursor '" + cursor + "' is unknown");
        }

    }
}
