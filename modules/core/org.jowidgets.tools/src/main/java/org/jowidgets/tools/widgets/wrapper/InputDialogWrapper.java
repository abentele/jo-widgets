/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.tools.widgets.wrapper;

import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;

public class InputDialogWrapper<INPUT_TYPE> extends WindowWrapper implements IInputDialog<INPUT_TYPE> {

    public InputDialogWrapper(final IInputDialog<INPUT_TYPE> widget) {
        super(widget);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected IInputDialog<INPUT_TYPE> getWidget() {
        return (IInputDialog<INPUT_TYPE>) super.getWidget();
    }

    @Override
    public void addValidator(final IValidator<INPUT_TYPE> validator) {
        getWidget().addValidator(validator);
    }

    @Override
    public boolean hasModifications() {
        return getWidget().hasModifications();
    }

    @Override
    public void resetModificationState() {
        getWidget().resetModificationState();
    }

    @Override
    public void setValue(final INPUT_TYPE value) {
        getWidget().setValue(value);
    }

    @Override
    public INPUT_TYPE getValue() {
        return getWidget().getValue();
    }

    @Override
    public IValidationResult validate() {
        return getWidget().validate();
    }

    @Override
    public void addValidationConditionListener(final IValidationConditionListener listener) {
        getWidget().addValidationConditionListener(listener);
    }

    @Override
    public void removeValidationConditionListener(final IValidationConditionListener listener) {
        getWidget().removeValidationConditionListener(listener);
    }

    @Override
    public void setEditable(final boolean editable) {
        getWidget().setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return getWidget().isEditable();
    }

    @Override
    public void setMinSize(final Dimension minSize) {
        getWidget().setMinSize(minSize);
    }

    @Override
    public void addInputListener(final IInputListener listener) {
        getWidget().addInputListener(listener);
    }

    @Override
    public void removeInputListener(final IInputListener listener) {
        getWidget().removeInputListener(listener);
    }

    @Override
    public boolean isOkPressed() {
        return getWidget().isOkPressed();
    }

}
