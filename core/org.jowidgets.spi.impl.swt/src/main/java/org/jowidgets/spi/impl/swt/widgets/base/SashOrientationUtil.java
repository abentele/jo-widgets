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

package org.jowidgets.spi.impl.swt.widgets.base;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;

final class SashOrientationUtil {
	static final ISashOrientationUtil HORIZONTAL = new HorizontalSashCalculator();
	static final ISashOrientationUtil VERTICAL = new VerticalSashCalculator();

	private SashOrientationUtil() {}

	private static final class HorizontalSashCalculator implements ISashOrientationUtil {

		@Override
		public int getPosition(final Rectangle bounds) {
			return bounds.x;
		}

		@Override
		public int getSize(final Rectangle bounds) {
			return bounds.width;
		}

		@Override
		public int getSize(final Point size) {
			return size.x;
		}

		@Override
		public void updateBounds(final Rectangle bounds, final Rectangle parentArea, final int position, final int size) {
			bounds.x = position;
			bounds.width = size;
			bounds.y = parentArea.y;
			bounds.height = parentArea.height;
		}

		@Override
		public int getEventPos(final Event event) {
			return event.x;
		}

		@Override
		public int getOrientation() {
			return SWT.HORIZONTAL;
		}
	}

	private static final class VerticalSashCalculator implements ISashOrientationUtil {

		@Override
		public int getPosition(final Rectangle bounds) {
			return bounds.y;
		}

		@Override
		public int getSize(final Rectangle bounds) {
			return bounds.height;
		}

		@Override
		public int getSize(final Point size) {
			return size.y;
		}

		@Override
		public void updateBounds(final Rectangle bounds, final Rectangle parentArea, final int position, final int size) {
			bounds.y = position;
			bounds.height = size;
			bounds.x = parentArea.x;
			bounds.width = parentArea.width;
		}

		@Override
		public int getEventPos(final Event event) {
			return event.y;
		}

		@Override
		public int getOrientation() {
			return SWT.VERTICAL;
		}
	}
}