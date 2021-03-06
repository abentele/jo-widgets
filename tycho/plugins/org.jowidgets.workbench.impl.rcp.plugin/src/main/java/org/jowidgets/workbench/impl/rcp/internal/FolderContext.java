/*
 * Copyright (c) 2011, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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

package org.jowidgets.workbench.impl.rcp.internal;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentNodeContext;
import org.jowidgets.workbench.api.IFolderContext;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.impl.rcp.internal.part.PartSupport;

public final class FolderContext implements IFolderContext {

	private final String folderId;
	private final ComponentContext componentContext;
	private final MenuModel menuModel = new MenuModel();

	public FolderContext(final String folderId, final ComponentContext componentContext) {
		this.folderId = folderId;
		this.componentContext = componentContext;
	}

	@Override
	public String getFolderId() {
		return folderId;
	}

	@Override
	public String getOriginalFolderId() {
		return folderId;
	}

	@Override
	public void addView(final IViewLayout viewLayout) {
		addView(false, viewLayout);
	}

	@Override
	public void addView(final boolean addToFront, final IViewLayout viewLayout) {
		PartSupport.getInstance().showView(viewLayout, componentContext, this);
	}

	@Override
	public IMenuModel getPopupMenu() {
		return menuModel;
	}

	@Override
	public IComponentContext getComponentContext() {
		return componentContext;
	}

	@Override
	public IComponentNodeContext getComponentNodeContext() {
		return getComponentContext().getComponentNodeContext();
	}

	@Override
	public IWorkbenchApplicationContext getWorkbenchApplicationContext() {
		return getComponentNodeContext().getWorkbenchApplicationContext();
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return getWorkbenchApplicationContext().getWorkbenchContext();
	}

}
