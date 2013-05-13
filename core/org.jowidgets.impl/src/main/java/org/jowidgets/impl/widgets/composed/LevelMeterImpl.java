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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.graphics.IGraphicContext;
import org.jowidgets.api.graphics.IPaintListener;
import org.jowidgets.api.model.levelmeter.ILevelListener;
import org.jowidgets.api.model.levelmeter.ILevelMeterModel;
import org.jowidgets.api.widgets.ICanvas;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.ILevelMeter;
import org.jowidgets.api.widgets.descriptor.setup.ILevelMeterSetup;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;

public final class LevelMeterImpl extends ControlWrapper implements ILevelMeter {

	private final ILevelMeterModel model;

	public LevelMeterImpl(final IComposite composite, final ILevelMeterSetup setup) {
		super(composite);

		this.model = setup.getModel();

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		composite.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final ICanvas canvas = composite.add(BPF.canvas(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		model.addLevelListener(new ILevelListener() {
			@Override
			public void levelChanged(final double oldValue, final double newValues) {
				canvas.redraw();
			}
		});

		canvas.addPaintListener(new IPaintListener() {
			@Override
			public void paint(final IGraphicContext gc) {
				final Rectangle bounds = gc.getBounds();

				gc.clearRectangle(0, 0, bounds.getWidth() - 1, bounds.getHeight() - 1);

				gc.setForegroundColor(setup.getLowPeakColor());
				final int y = (int) ((1.0 - model.getLevel()) * bounds.getHeight());
				gc.fillRectangle(0, y, bounds.getWidth(), bounds.getHeight() - y);

				gc.setForegroundColor(Colors.DISABLED);
				gc.drawRectangle(0, 0, bounds.getWidth() - 1, bounds.getHeight() - 1);
			}
		});

	}

	@Override
	public ILevelMeterModel getModel() {
		return model;
	}

}
