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

package org.jowidgets.workbench.impl;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchConfigurationService;
import org.jowidgets.workbench.api.IWorkbenchFactory;
import org.jowidgets.workbench.api.IWorkbenchRunner;

public class WorkbenchRunner implements IWorkbenchRunner {

    @Override
    public void run(final IWorkbenchFactory workbenchFactory) {
        run(workbenchFactory, new DefaultConfigurationService());
    }

    @Override
    public void run(final IWorkbenchFactory workbenchFactory, final IWorkbenchConfigurationService configurationService) {
        Assert.paramNotNull(workbenchFactory, "workbenchFactory");
        Toolkit.getApplicationRunner().run(new IApplication() {
            @Override
            public void start(final IApplicationLifecycle lifecycle) {
                final IWorkbench workbench = workbenchFactory.create();
                final VetoHolder vetoHolder = new VetoHolder();
                workbench.onLogin(vetoHolder);
                if (!vetoHolder.hasVeto()) {
                    new WorkbenchContext(workbench, lifecycle).run();
                }
                else {
                    lifecycle.finish();
                }
            }
        });
    }

}
