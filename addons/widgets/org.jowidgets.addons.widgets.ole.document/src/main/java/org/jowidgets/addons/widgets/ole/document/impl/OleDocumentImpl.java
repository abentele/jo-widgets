/*
 * Copyright (c) 2012, grossmann, waheckma
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

package org.jowidgets.addons.widgets.ole.document.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.jowidgets.addons.widgets.ole.api.IOleContext;
import org.jowidgets.addons.widgets.ole.api.IOleControl;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocument;
import org.jowidgets.addons.widgets.ole.document.api.IOleDocumentSetupBuilder;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IFactory;
import org.jowidgets.util.IMutableValue;
import org.jowidgets.util.IMutableValueListener;
import org.jowidgets.util.IValueChangedEvent;

class OleDocumentImpl extends ControlWrapper implements IOleDocument {

	private final String progId;
	private final IMutableValue<IOleContext> mutableOleContext;
	private final IFactory<File> tempFileFactory;

	private File tempDocumentStateFile;

	public OleDocumentImpl(final IOleControl oleControl, final IOleDocumentSetupBuilder<?> setup) {
		super(oleControl);
		Assert.paramNotNull(setup.getProgId(), "setup.getProgId()");
		this.progId = setup.getProgId();
		this.mutableOleContext = oleControl.getContext();

		if (setup.getTempFileFactory() != null) {
			tempFileFactory = setup.getTempFileFactory();
		}
		else {
			tempFileFactory = new DefaultTempFileFactory();
		}
		mutableOleContext.addMutableValueListener(new IMutableValueListener<IOleContext>() {
			@Override
			public void changed(final IValueChangedEvent<IOleContext> event) {
				oleContextChanged(event);
			}
		});

		oleContextChanged(null);
	}

	@Override
	protected IOleControl getWidget() {
		return (IOleControl) super.getWidget();
	}

	private void oleContextChanged(final IValueChangedEvent<IOleContext> event) {
		final IOleContext oleContext = mutableOleContext.getValue();
		if (oleContext != null) {
			if (tempDocumentStateFile != null && tempDocumentStateFile.exists()) {
				oleContext.setDocument(progId, tempDocumentStateFile);
				tempDocumentStateFile.delete();
				tempDocumentStateFile = null;
			}
			else {
				oleContext.setDocument(progId);
			}
		}
		else if (event != null) {
			final IOleContext oldContext = event.getOldValue();

			if (oldContext != null) {
				tempDocumentStateFile = tempFileFactory.create();
				oldContext.saveCurrentDocument(tempDocumentStateFile, true);

			}
		}
	}

	@Override
	public void openNewDocument() {
		if (mutableOleContext.getValue() != null && progId != null) {
			mutableOleContext.getValue().setDocument(progId);
		}
	}

	@Override
	public void openDocument(final File file) {
		if (mutableOleContext.getValue() != null) {
			if (progId != null) {
				mutableOleContext.getValue().setDocument(progId, file);
			}
			else {
				mutableOleContext.getValue().setDocument(file);
			}
		}
		else {
			throw new RuntimeException("Ole Context is null");
		}
	}

	@Override
	public boolean saveDocument(final File file, final Boolean includeOleInfo) {
		if (mutableOleContext.getValue() != null) {
			return mutableOleContext.getValue().saveCurrentDocument(file, includeOleInfo);
		}
		else {
			throw new RuntimeException("Ole Context is null");
		}
	}

	@Override
	public void openDocument(final InputStream inputStream) {
		try {
			final File tempFile = tempFileFactory.create();

			if (tempFile != null) {
				final OutputStream out = new FileOutputStream(tempFile);

				final byte[] buf = new byte[1024];
				int length;
				while ((length = inputStream.read(buf)) > 0) {
					out.write(buf, 0, length);
				}
				out.close();

				openDocument(tempFile);

				if (!(tempFile.delete())) {
					throw new IOException("Could not delete temp file: " + tempFile.getAbsolutePath());
				}
			}
		}
		catch (final IOException e) {
			throw new RuntimeException("Open Document failed: " + e);
		}
	}

	@Override
	public void saveDocument(OutputStream outputStream) {

		try {
			final File tempFile = tempFileFactory.create();
			saveDocument(tempFile, true);
			outputStream = new FileOutputStream(tempFile);
			if (!(tempFile.delete())) {
				throw new IOException("Could not delete temp file: " + tempFile.getAbsolutePath());
			}
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void dispose() {
		if (tempDocumentStateFile != null) {
			tempDocumentStateFile.delete();
		}
		super.dispose();
	}

	@Override
	public boolean isDirty() {
		if (mutableOleContext.getValue() != null) {
			return mutableOleContext.getValue().isDirty();
		}
		return false;
	}

	@Override
	public IMutableValue<IOleContext> getContext() {
		return getWidget().getContext();
	}

	private final class DefaultTempFileFactory implements IFactory<File> {
		@Override
		public File create() {
			File temp = null;

			try {

				temp = File.createTempFile("OleTempFile", UUID.randomUUID().toString() + ".doc");
			}
			catch (final IOException e) {
				throw new RuntimeException(e);
			}
			return temp;
		}
	}
}