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

package org.jowidgets.impl.base.delegate;

import org.jowidgets.api.model.item.ISelectableMenuItemModel;
import org.jowidgets.impl.widgets.common.wrapper.invoker.ISelectableItemSpiInvoker;
import org.jowidgets.util.Assert;

public class SelectableItemModelBindingDelegate extends ItemModelBindingDelegate {

    private boolean selected;

    public SelectableItemModelBindingDelegate(final ISelectableItemSpiInvoker widget, final ISelectableMenuItemModel model) {
        super(widget, model);
        Assert.paramNotNull(model, "model");

        this.selected = false;
        updateThisFromModel();
    }

    @Override
    public ISelectableItemSpiInvoker getWidget() {
        return (ISelectableItemSpiInvoker) super.getWidget();
    }

    @Override
    public ISelectableMenuItemModel getModel() {
        return (ISelectableMenuItemModel) super.getModel();
    }

    public void setSelected(final boolean selected) {
        setSelectedValue(selected);
        unRegisterModel();
        getModel().setSelected(selected);
        registerModel();
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    protected void updateFromModel() {
        super.updateFromModel();
        updateThisFromModel();
    }

    private void updateThisFromModel() {
        setSelectedValue(getModel().isSelected());
    }

    private void setSelectedValue(final boolean selected) {
        if (this.selected != selected || getWidget().isSelected() != selected) {
            this.selected = selected;
            getWidget().setSelected(selected);
        }
    }

}
