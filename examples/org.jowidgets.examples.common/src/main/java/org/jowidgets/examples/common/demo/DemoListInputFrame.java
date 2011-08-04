/*
 * Copyright (c) 2011, grossmann, Nikolaus Moll
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

package org.jowidgets.examples.common.demo;

import java.util.ArrayList;
import java.util.Collection;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.validation.ValidationResult;

//CHECKSTYLE:OFF
@SuppressWarnings({"unchecked", "serial", "rawtypes"})
public class DemoListInputFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	public DemoListInputFrame() {
		super("List input demo");

		setLayout(new MigLayoutDescriptor("[grow, 0::]", "[]10[grow, 0::]"));

		add(BPF.validationResultLabel(), "wrap").setResult(
				ValidationResult.create().withError("Entry 4: Must be a propper value"));

		final IScrollComposite scrollComposite = add(BPF.scrollComposite(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		scrollComposite.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final IInputControl<Collection<String>> add = scrollComposite.add(
				BPF.collectionInputControl(getControlBp()),
				MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		add.setValue(new ArrayList<String>() {
			{
				add("Germany");
				add("Spain");
				add("Italy");
				add("USA");
			}
		});
	}

	//	private IWidgetDescriptor getControlBp() {
	//		return BPF.comboBoxSelection("Germany", "Spain", "Italy", "USA", "Frankreich", "Portugal", "Bayern");
	//	}

	private IWidgetDescriptor getControlBp() {
		return BPF.comboBox("Germany", "Spain", "Italy", "USA", "Frankreich", "Portugal", "Bayern");
	}

	//	private IWidgetDescriptor getControlBp() {
	//		return BPF.inputFieldDate();
	//	}

	//	private IWidgetDescriptor getControlBp() {
	//		return BPF.inputFieldIntegerNumber();
	//	}

}