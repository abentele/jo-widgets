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

package org.jowidgets.examples.common.workbench.demo2.application;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.examples.common.workbench.demo1.ComponentDemo1;
import org.jowidgets.examples.common.workbench.demo2.component.ComponentDemo2;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.tools.ComponentNodeModelBuilder;
import org.jowidgets.workbench.tools.WorkbenchApplicationModelBuilder;

public class Application2 {

    private final IWorkbenchApplicationModel model;

    public Application2() {
        final IWorkbenchApplicationModelBuilder builder = new WorkbenchApplicationModelBuilder();
        builder.setId(Application2.class.getName());
        builder.setLabel("App 2");
        builder.setTooltip("Application 2");
        builder.setIcon(SilkIcons.USER_RED);
        this.model = builder.build();

        createComponentTree(model);
    }

    public IWorkbenchApplicationModel getModel() {
        return model;
    }

    private void createComponentTree(final IWorkbenchApplicationModel model) {
        final IComponentNodeModel folder1 = model.addChild("FOLDER_1_ID", "Folder 1", SilkIcons.FOLDER);

        for (int i = 0; i < 5; i++) {
            final IComponentNodeModelBuilder nodeModelBuilder = new ComponentNodeModelBuilder();
            nodeModelBuilder.setId(ComponentDemo1.class.getName() + i);
            nodeModelBuilder.setLabel("Component " + (i + 1));
            nodeModelBuilder.setComponentFactory(ComponentDemo1.class);
            folder1.addChild(nodeModelBuilder.build());
        }

        final IComponentNodeModel folder2 = model.addChild("FOLDER_2_ID", "Folder 2", SilkIcons.FOLDER);
        for (int i = 0; i < 5; i++) {
            final IComponentNodeModelBuilder nodeModelBuilder = new ComponentNodeModelBuilder();
            nodeModelBuilder.setId(ComponentDemo2.class.getName() + i);
            nodeModelBuilder.setLabel("Simple Component " + (i + 1));
            nodeModelBuilder.setComponentFactory(ComponentDemo2.class);
            folder2.addChild(nodeModelBuilder.build());
        }
    }

}
