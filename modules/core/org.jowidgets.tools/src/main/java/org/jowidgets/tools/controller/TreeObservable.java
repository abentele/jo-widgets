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

package org.jowidgets.tools.controller;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.controller.ITreeListener;
import org.jowidgets.api.controller.ITreeObservable;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.util.Assert;

public class TreeObservable implements ITreeObservable {

    private final Set<ITreeListener> listeners;

    public TreeObservable() {
        this.listeners = new LinkedHashSet<ITreeListener>();
    }

    @Override
    public final void addTreeListener(final ITreeListener listener) {
        Assert.paramNotNull(listener, "listener");
        listeners.add(listener);
    }

    @Override
    public final void removeTreeListener(final ITreeListener listener) {
        Assert.paramNotNull(listener, "listener");
        listeners.remove(listener);
    }

    public final void fireNodeExpanded(final ITreeNode node) {
        Assert.paramNotNull(node, "node");
        for (final ITreeListener listener : new LinkedList<ITreeListener>(listeners)) {
            listener.nodeExpanded(node);
        }
    }

    public final void fireNodeCollapsed(final ITreeNode node) {
        Assert.paramNotNull(node, "node");
        for (final ITreeListener listener : new LinkedList<ITreeListener>(listeners)) {
            listener.nodeCollapsed(node);
        }
    }

    public final void fireNodeChecked(final ITreeNode node) {
        Assert.paramNotNull(node, "node");
        for (final ITreeListener listener : new LinkedList<ITreeListener>(listeners)) {
            listener.nodeChecked(node);
        }
    }

    public final void fireNodeUnchecked(final ITreeNode node) {
        Assert.paramNotNull(node, "node");
        for (final ITreeListener listener : new LinkedList<ITreeListener>(listeners)) {
            listener.nodeUnchecked(node);
        }
    }

}
