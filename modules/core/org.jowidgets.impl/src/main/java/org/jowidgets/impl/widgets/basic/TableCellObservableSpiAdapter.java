/*
 * Copyright (c) 2011, Nikolaus Moll
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

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.widgets.controller.ITableCellListener;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controller.ITableCellObservable;
import org.jowidgets.impl.event.TableCellMouseEvent;

class TableCellObservableSpiAdapter implements ITableCellObservable {

	private final Set<ITableCellListener> listeners;

	TableCellObservableSpiAdapter() {
		this.listeners = new HashSet<ITableCellListener>();
	}

	@Override
	public void addTableCellListener(final ITableCellListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeTableCellListener(final ITableCellListener listener) {
		listeners.remove(listener);
	}

	public void fireMouseDoubleClicked(final ITableCellMouseEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		if (!listeners.isEmpty()) {
			final ITableCellMouseEvent decoratedEvent = createDecoratedEvent(event, modelSpiAdapter);
			for (final ITableCellListener listener : listeners) {
				listener.mouseDoubleClicked(decoratedEvent);
			}
		}
	}

	public void fireMousePressed(final ITableCellMouseEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		if (!listeners.isEmpty()) {
			final ITableCellMouseEvent decoratedEvent = createDecoratedEvent(event, modelSpiAdapter);
			for (final ITableCellListener listener : listeners) {
				listener.mousePressed(decoratedEvent);
			}
		}
	}

	public void fireMouseReleased(final ITableCellMouseEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		if (!listeners.isEmpty()) {
			final ITableCellMouseEvent decoratedEvent = createDecoratedEvent(event, modelSpiAdapter);
			for (final ITableCellListener listener : listeners) {
				listener.mouseReleased(decoratedEvent);
			}
		}
	}

	private ITableCellMouseEvent createDecoratedEvent(final ITableCellMouseEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		return new TableCellMouseEvent(
			event.getRowIndex(),
			modelSpiAdapter.convertViewToModel(event.getColumnIndex()),
			event.getMouseButton(),
			event.getModifiers());

	}
}