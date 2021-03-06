/*
 * Copyright (c) 2014, Michael
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

package org.jowidgets.impl.dnd;

import org.jowidgets.api.dnd.IDropResponse;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.dnd.DropMode;
import org.jowidgets.spi.dnd.IDropResponseSpi;
import org.jowidgets.util.Assert;

final class DropResponseImpl implements IDropResponse {

    private final IDropResponseSpi dropResponseSpi;

    DropResponseImpl(final IDropResponseSpi dropResponseSpi) {
        Assert.paramNotNull(dropResponseSpi, "dropResponseSpi");
        this.dropResponseSpi = dropResponseSpi;
    }

    @Override
    public void accept(final DropAction operation) {
        dropResponseSpi.accept(operation);
    }

    @Override
    public void reject() {
        dropResponseSpi.reject();
    }

    @Override
    public void setDropMode(final DropMode dropMode) {
        dropResponseSpi.setDropMode(dropMode);
    }

}
