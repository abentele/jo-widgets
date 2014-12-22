/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.tools.clipboard;

import java.util.Collection;
import java.util.Collections;

import org.jowidgets.api.clipboard.ITransferable;
import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.util.Assert;

public final class SingleTypeTransfer<SINGLE_JAVA_TYPE> implements ITransferable {

	@SuppressWarnings("rawtypes")
	private final Collection supportedTypes;
	private final SINGLE_JAVA_TYPE data;

	public SingleTypeTransfer(final Class<SINGLE_JAVA_TYPE> javaType, final SINGLE_JAVA_TYPE data) {
		this(new TransferType<SINGLE_JAVA_TYPE>(javaType), data);
	}

	public SingleTypeTransfer(final TransferType<SINGLE_JAVA_TYPE> transferType, final SINGLE_JAVA_TYPE data) {
		Assert.paramNotNull(transferType, "transferType");
		this.supportedTypes = Collections.singleton(transferType);
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<TransferType<?>> getSupportedTypes() {
		return supportedTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <JAVA_TYPE> JAVA_TYPE getData(final TransferType<JAVA_TYPE> type) {
		Assert.paramNotNull(type, "type");
		if (supportedTypes.contains(type)) {
			return (JAVA_TYPE) data;
		}
		else {
			return null;
		}
	}

}
