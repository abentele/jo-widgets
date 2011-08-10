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

package org.jowidgets.spi.impl.controller;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionObservable;

public class PopupDetectionObservable implements IPopupDetectionObservable {

	private final Set<IPopupDetectionListener> listeners;

	public PopupDetectionObservable() {
		super();
		this.listeners = new HashSet<IPopupDetectionListener>();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		listeners.remove(listener);
	}

	public void firePopupDetected(final Position position) {
		for (final IPopupDetectionListener listener : listeners) {
			listener.popupDetected(position);
		}
	}

}