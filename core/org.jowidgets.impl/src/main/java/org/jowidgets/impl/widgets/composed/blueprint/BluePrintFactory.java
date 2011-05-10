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
package org.jowidgets.impl.widgets.composed.blueprint;

import java.text.DateFormat;
import java.util.Date;

import org.jowidgets.api.image.Icons;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextSeparatorBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.impl.convert.DefaultConverterProvider;
import org.jowidgets.impl.widgets.composed.blueprint.convenience.registry.ComposedSetupConvenienceRegistry;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.registry.ComposedDefaultsInitializerRegistry;

public final class BluePrintFactory extends SimpleBluePrintFactory implements IBluePrintFactory {

	public BluePrintFactory() {
		super(new ComposedSetupConvenienceRegistry(), new ComposedDefaultsInitializerRegistry());
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////input fields here///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	@Override
	public IInputFieldBluePrint<String> inputFieldString() {
		return inputField(DefaultConverterProvider.STRING);
	}

	@Override
	public IInputFieldBluePrint<Long> inputFieldLongNumber() {
		return inputField(DefaultConverterProvider.LONG_NUMBER);
	}

	@Override
	public IInputFieldBluePrint<Short> inputFieldShortNumber() {
		return inputField(DefaultConverterProvider.SHORT_NUMBER);
	}

	@Override
	public IInputFieldBluePrint<Integer> inputFieldIntegerNumber() {
		return inputField(DefaultConverterProvider.INTEGER_NUMBER);
	}

	@Override
	public IInputFieldBluePrint<Date> inputFieldDate(final DateFormat dateFormat, final String formatHint, final ITextMask mask) {
		return inputField(Toolkit.getConverterProvider().date(dateFormat, formatHint, mask));
	}

	@Override
	public IInputFieldBluePrint<Date> inputFieldDate(final DateFormat dateFormat, final String formatHint) {
		return inputField(Toolkit.getConverterProvider().date(dateFormat, formatHint));
	}

	@Override
	public IInputFieldBluePrint<Date> inputFieldDate() {
		return inputField(Toolkit.getConverterProvider().date());
	}

	@Override
	public IInputFieldBluePrint<Date> inputFieldDateTime() {
		return inputField(Toolkit.getConverterProvider().dateTime());
	}

	@Override
	public IInputFieldBluePrint<Date> inputFieldTime() {
		return inputField(Toolkit.getConverterProvider().time());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////some convenience methods starting here///////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public ILabelBluePrint label(final IImageConstant icon) {
		return label().setIcon(icon);
	}

	@Override
	public ILabelBluePrint label(final IImageConstant icon, final String text) {
		return label().setIcon(icon).setText(text);
	}

	@Override
	public ILabelBluePrint label(final IImageConstant icon, final String text, final String toolTiptext) {
		return label().setIcon(icon).setText(text).setToolTipText(toolTiptext);
	}

	@Override
	public ITextSeparatorBluePrint textSeparator(final String text, final String tooltipText) {
		return textSeparator(text).setToolTipText(tooltipText);
	}

	@Override
	public ITextSeparatorBluePrint textSeparator(final String text) {
		return textSeparator().setText(text);
	}

	@Override
	public IMessageDialogBluePrint infoDialog() {
		return messageDialog().setIcon(Icons.INFO);
	}

	@Override
	public IMessageDialogBluePrint warningDialog() {
		return messageDialog().setIcon(Icons.WARNING);
	}

	@Override
	public IMessageDialogBluePrint errorDialog() {
		return messageDialog().setIcon(Icons.ERROR);
	}

	@Override
	public IMessageDialogBluePrint infoDialog(final String message) {
		return infoDialog().setText(message);
	}

	@Override
	public IMessageDialogBluePrint warningDialog(final String message) {
		return warningDialog().setText(message);
	}

	@Override
	public IMessageDialogBluePrint errorDialog(final String message) {
		return errorDialog().setText(message);
	}

	@Override
	public IQuestionDialogBluePrint yesNoQuestion() {
		return questionDialog();
	}

	//TODO I18N
	@Override
	public IQuestionDialogBluePrint yesNoCancelQuestion() {
		return questionDialog().setCancelButton(button("Cancel"));
	}

	@Override
	public IQuestionDialogBluePrint yesNoQuestion(final String question) {
		return yesNoQuestion().setText(question);
	}

	@Override
	public IQuestionDialogBluePrint yesNoCancelQuestion(final String question) {
		return yesNoCancelQuestion().setText(question);
	}

	@Override
	public IProgressBarBluePrint progressBar(final int minimum, final int maximum) {
		return progressBar(maximum).setMinimum(minimum);
	}

	@Override
	public IProgressBarBluePrint progressBar(final int maximum) {
		return progressBar().setIndeterminate(false).setMaximum(maximum);
	}

}
