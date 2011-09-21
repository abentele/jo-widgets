/*
 * Copyright (c) 2011, Michael Grossmann
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
package org.jowidgets.api.widgets.blueprint.builder;

import java.util.Collection;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.widgets.descriptor.setup.ICollectionInputDialogSetup;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.validation.IValidator;

public interface ICollectionInputFieldSetupBuilder<INSTANCE_TYPE extends ICollectionInputFieldSetupBuilder<?, ?>, ELEMENT_TYPE> extends
		IInputComponentSetupBuilder<INSTANCE_TYPE, Collection<ELEMENT_TYPE>> {

	INSTANCE_TYPE setCollectionInputDialogSetup(final ICollectionInputDialogSetup<ELEMENT_TYPE> setup);

	INSTANCE_TYPE setEditButtonIcon(IImageConstant icon);

	INSTANCE_TYPE setConverter(IConverter<ELEMENT_TYPE> converter);

	INSTANCE_TYPE setElementValidator(IValidator<ELEMENT_TYPE> validator);

	INSTANCE_TYPE setSeparator(Character separator);

	INSTANCE_TYPE setMaskingCharacter(Character mask);

}