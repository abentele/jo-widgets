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

package org.jowidgets.tools.powo;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.common.types.Border;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.util.Assert;

public class JoComposite extends Composite<IComposite, ICompositeBluePrint> implements IComposite {

	JoComposite(final IComposite widget) {
		this(bluePrint());
		Assert.paramNotNull(widget, "widget");
		initialize(widget);
	}

	public JoComposite() {
		this(bluePrint());
	}

	public JoComposite(final String borderTitle) {
		this(bluePrint(borderTitle));
	}

	public JoComposite(final boolean border) {
		this(bluePrint(border));
	}

	public JoComposite(final ILayoutDescriptor layout) {
		this(bluePrint(layout));
	}

	public JoComposite(final ICompositeDescriptor descriptor) {
		super(bluePrint().setSetup(descriptor));
	}

	public static JoComposite toJoComposite(final IComposite widget) {
		Assert.paramNotNull(widget, "widget");
		if (widget instanceof JoComposite) {
			return (JoComposite) widget;
		}
		else {
			return new JoComposite(widget);
		}
	}

	public static ICompositeBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().composite();
	}

	public static ICompositeBluePrint bluePrint(final ILayoutDescriptor layout) {
		Assert.paramNotNull(layout, "layout");
		return bluePrint().setLayout(layout);
	}

	public static ICompositeBluePrint bluePrint(final String borderTitle) {
		return bluePrint().setBorder(new Border(borderTitle));
	}

	public static ICompositeBluePrint bluePrint(final boolean border) {
		return border ? Toolkit.getBluePrintFactory().compositeWithBorder() : bluePrint();
	}

}