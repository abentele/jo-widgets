/*
 * Copyright (c) 2012, Michael Grossmann
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
package org.jowidgets.examples.common;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextAreaBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class HelloWorldApplication implements IApplication {

	public void start() {
		Toolkit.getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IFrameBluePrint frameBp = BPF.frame().setTitle("Hello World").setBackgroundColor(Colors.WHITE);
		frameBp.setSize(new Dimension(800, 600));
		frameBp.setPosition(new Position(500, 100));
		final ILabelBluePrint labelBp = BPF.label().setText("Vorname: ").alignRight();
		final ILabelBluePrint label2Bp = BPF.label().setText("Name: ").alignRight();
		final ILabelBluePrint label3Bp = BPF.label().setText("Sonstiges: ").alignRight();
		final IButtonBluePrint buttonSubmitBp = BPF.button().setText("Submit").setEnabled(false).setToolTipText("Submit");
		final IButtonBluePrint buttonClearBp = BPF.button().setText("Clear").setToolTipText("Clear");
		final IFrame rootFrame = Toolkit.createRootFrame(frameBp, lifecycle);
		final ITextFieldBluePrint textfieldBp = BPF.textField().setMaxLength(20);
		final ITextFieldBluePrint textfield2Bp = BPF.textField().setMaxLength(20);
		final ITextFieldBluePrint textfield3Bp = BPF.textField().setEditable(false).setPasswordPresentation(true);
		final ITextAreaBluePrint textareaBp = BPF.textArea().setFontName("Century Gothic").setFontSize(10).setBorder(false);

		//		rootFrame.setLayout(Toolkit.getLayoutFactoryProvider().borderLayoutBuilder().margin(5).gap(5).build());
		//		final IButton button2 = rootFrame.add(buttonBp2, BorderLayoutConstraints.TOP);
		//		final IButton button = rootFrame.add(buttonBp, BorderLayoutConstraints.BOTTOM);
		//		final ILabel label = rootFrame.add(labelBp, BorderLayoutConstraints.LEFT);
		//		final ITextControl textfield = rootFrame.add(textfieldBp, BorderLayoutConstraints.CENTER);

		rootFrame.setLayout(Toolkit.getLayoutFactoryProvider().migLayoutBuilder().constraints("insets 10 10 10 10").columnConstraints(
				"[][]").rowConstraints("[]20[]20[]20[]20[]20[]").build());
		rootFrame.add(labelBp, "cell 0 0");
		rootFrame.add(label2Bp, "cell 0 1");
		rootFrame.add(label3Bp, "cell 0 2");
		final ITextControl textfield = rootFrame.add(textfieldBp, "cell 1 0");
		final ITextControl textfield2 = rootFrame.add(textfield2Bp, "cell 1 1");
		final ITextControl textfield3 = rootFrame.add(textfield3Bp, "cell 1 2");
		final IButton buttonsub = rootFrame.add(buttonSubmitBp, "cell 0 3");
		final IButton buttonclear = rootFrame.add(buttonClearBp, "cell 1 3");
		final ITextArea textarea = rootFrame.add(textareaBp, "cell 0 4, span");

		final IFocusListener listener = new IFocusListener() {
			@Override
			public void focusLost() {
				if ((!textfield.getText().isEmpty()) && (!textfield2.getText().isEmpty())) {
					textfield3.setEditable(true);
					buttonsub.setEnabled(true);
				}
				else {
					textfield3.setEditable(false);
					buttonsub.setEnabled(false);
				}
				textfield.setBackgroundColor(Colors.DARK_GREY);
			}

			@Override
			public void focusGained() {
				textfield.setBackgroundColor(Colors.GREEN);
			}
		};

		buttonclear.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				textfield.setText("");
				textfield2.setText("");
				textfield3.setText("");
				textarea.setText("");
			}
		});

		buttonsub.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				final String tmp = textarea.getText()
					+ "\n"
					+ textfield.getText()
					+ "\n"
					+ textfield2.getText()
					+ "\n"
					+ textfield3.getText();
				textarea.setText(tmp);
			}
		});

		textfield2.addInputListener(new IInputListener() {

			@Override
			public void inputChanged() {
				if (textfield2.getText().isEmpty()) {
					buttonsub.setEnabled(false);
				}
				else {
					buttonsub.setEnabled(true);
				}

			}
		});

		textfield.addFocusListener(listener);
		textfield2.addFocusListener(listener);
		rootFrame.setVisible(true);

	}
}