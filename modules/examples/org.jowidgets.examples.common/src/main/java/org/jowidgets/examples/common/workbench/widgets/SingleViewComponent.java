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

package org.jowidgets.examples.common.workbench.widgets;

import java.lang.reflect.Constructor;

import org.jowidgets.examples.common.workbench.base.AbstractDemoComponent;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.toolkit.api.IFolderLayoutBuilder;
import org.jowidgets.workbench.tools.FolderLayoutBuilder;
import org.jowidgets.workbench.tools.Layout;

public class SingleViewComponent extends AbstractDemoComponent implements IComponent {

    public static final String DEFAULT_LAYOUT_ID = "DEFAULT_LAYOUT_ID";
    public static final String MASTER_FOLDER_ID = "MASTER_FOLDER_ID";

    private final Class<? extends IView> viewClass;
    private final String id;
    private final String label;

    public SingleViewComponent(
        final IComponentContext context,
        final Class<? extends IView> viewClass,
        final String id,
        final String label) {

        this.viewClass = viewClass;
        this.id = id;
        this.label = label;

        context.setLayout(new Layout(DEFAULT_LAYOUT_ID, createMasterFolder()));
    }

    @Override
    public IView createView(final String viewId, final IViewContext context) {
        try {
            @SuppressWarnings("unchecked")
            final Constructor<IView> constructor = (Constructor<IView>) viewClass.getConstructor(IViewContext.class);
            return constructor.newInstance(context);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private IFolderLayoutBuilder createMasterFolder() {
        return new FolderLayoutBuilder(MASTER_FOLDER_ID).addView(id, label);
    }

}
