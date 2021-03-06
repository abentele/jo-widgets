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

package org.jowidgets.impl.widgets.basic.factory.internal;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.convert.IStringObjectConverter;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.IComboBoxBluePrintSpi;
import org.jowidgets.impl.widgets.basic.ComboBoxImpl;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ComboBoxBuilderConverter;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.tools.verify.InputVerifierComposite;

public class ComboBoxFactory<VALUE_TYPE> extends AbstractWidgetFactory implements
        IWidgetFactory<IComboBox<VALUE_TYPE>, IComboBoxDescriptor<VALUE_TYPE>> {

    public ComboBoxFactory(
        final IGenericWidgetFactory genericWidgetFactory,
        final IWidgetsServiceProvider widgetsServiceProvider,
        final ISpiBluePrintFactory bpF) {

        super(genericWidgetFactory, widgetsServiceProvider, bpF);
    }

    @Override
    public IComboBox<VALUE_TYPE> create(final Object parentUiReference, final IComboBoxDescriptor<VALUE_TYPE> descriptor) {
        final IComboBoxBluePrintSpi bp = getSpiBluePrintFactory().comboBox().setSetup(descriptor);
        ComboBoxBuilderConverter.convert(bp, descriptor);

        final IStringObjectConverter<?> converter = descriptor.getStringObjectConverter();

        final IInputVerifier inputVerifier = descriptor.getInputVerifier();
        final IInputVerifier converterInputVerifier = converter.getInputVerifier();

        InputVerifierComposite spiInputVerifier = null;
        if (inputVerifier != null || converterInputVerifier != null) {
            spiInputVerifier = new InputVerifierComposite();
            if (inputVerifier != null) {
                spiInputVerifier.addVerifier(inputVerifier);
            }
            if (converterInputVerifier != null) {
                spiInputVerifier.addVerifier(converterInputVerifier);
            }
        }

        final List<String> regExps = descriptor.getAcceptingRegExps();
        final String converterRegExp = converter.getAcceptingRegExp();

        final List<String> spiRegExps = new LinkedList<String>();
        spiRegExps.addAll(regExps);
        if (converterRegExp != null) {
            spiRegExps.add(converterRegExp);
        }

        bp.setInputVerifier(spiInputVerifier);
        bp.setAcceptingRegExps(spiRegExps);
        bp.setMaxLength(descriptor.getMaxLength());
        bp.setMask(converter.getMask());

        final IComboBoxSpi widget = getSpiWidgetFactory().createComboBox(parentUiReference, bp);
        return new ComboBoxImpl<VALUE_TYPE>(widget, descriptor);
    }
}
