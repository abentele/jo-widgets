/*
 * Copyright (c) 2011, H.Westphal
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

package org.jowidgets.addons.map.common.impl;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.jowidgets.addons.map.common.IDesignationListener;

public abstract class AbstractDesignationCallback<T> implements IDesignationCallback<T> {

	private BrowserFunction browserFunction;

	@Override
	public final boolean register(final Browser browser, final IDesignationListener<? super T> listener) {
		browserFunction = new BrowserFunction(browser, getCallbackName()) {
			@Override
			public Object function(final Object[] arguments) {
				super.function(arguments);
				final T result = createResult(arguments);
				getBrowser().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						listener.onDesignation(result);
					}
				});
				return null;
			}
		};
		return browser.execute(getJsStartFunctionName() + "('" + getCallbackName() + "');");
	}

	@Override
	public final boolean unregister() {
		try {
			return browserFunction.getBrowser().execute(getJsEndFunctionName() + "();");
		}
		finally {
			browserFunction.dispose();
			browserFunction = null;
		}
	}

	protected String getCallbackName() {
		return getClass().getSimpleName();
	}

	protected abstract String getJsStartFunctionName();

	protected abstract String getJsEndFunctionName();

	protected abstract T createResult(final Object[] arguments);

}