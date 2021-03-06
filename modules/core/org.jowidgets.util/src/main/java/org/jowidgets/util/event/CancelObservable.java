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

package org.jowidgets.util.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jowidgets.util.Assert;

public class CancelObservable implements ICancelObservable {

    private final Set<ICancelListener> listeners;

    public CancelObservable() {
        this.listeners = new LinkedHashSet<ICancelListener>();
    }

    @Override
    public final void addCancelListener(final ICancelListener listener) {
        Assert.paramNotNull(listener, "listener");
        synchronized (this) {
            listeners.add(listener);
        }
    }

    @Override
    public final void removeCancelListener(final ICancelListener listener) {
        Assert.paramNotNull(listener, "listener");
        synchronized (this) {
            listeners.remove(listener);
        }
    }

    public final void fireCanceledEvent() {
        final Collection<ICancelListener> listenerCopy;
        synchronized (this) {
            listenerCopy = new ArrayList<ICancelListener>(listeners);
        }

        for (final ICancelListener listener : listenerCopy) {
            listener.canceled();
        }
    }

    public final void dispose() {
        listeners.clear();
    }

}
