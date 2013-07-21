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

package org.jowidgets.api.toolkit;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.jowidgets.api.animation.IAnimationRunnerBuilder;
import org.jowidgets.api.animation.IWaitAnimationProcessor;
import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.convert.IConverterProvider;
import org.jowidgets.api.event.IDelayedEventRunnerBuilder;
import org.jowidgets.api.layout.ILayoutFactoryProvider;
import org.jowidgets.api.mask.ITextMaskBuilder;
import org.jowidgets.api.model.IModelFactoryProvider;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.utils.IWidgetUtils;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentCreatorFactory;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.util.Assert;
import org.jowidgets.util.ITypedKey;

public final class Toolkit {

	private static IToolkitProvider toolkitProvider;

	private Toolkit() {}

	public static void initialize(final IToolkitProvider toolkitProvider) {
		Assert.paramNotNull(toolkitProvider, "toolkitProvider");
		if (Toolkit.toolkitProvider != null) {
			throw new IllegalStateException("Toolkit is already initialized");
		}
		Toolkit.toolkitProvider = toolkitProvider;
	}

	public static boolean isInitialized() {
		return toolkitProvider != null;
	}

	public static synchronized IToolkit getInstance() {
		if (toolkitProvider == null) {
			final ServiceLoader<IToolkitProvider> toolkitProviderLoader = ServiceLoader.load(
					IToolkitProvider.class,
					SharedClassLoader.getCompositeClassLoader());
			final Iterator<IToolkitProvider> iterator = toolkitProviderLoader.iterator();

			if (!iterator.hasNext()) {
				throw new IllegalStateException("No implementation found for '" + IToolkitProvider.class.getName() + "'");
			}

			Toolkit.toolkitProvider = iterator.next();

			if (iterator.hasNext()) {
				throw new IllegalStateException("More than one implementation found for '"
					+ IToolkitProvider.class.getName()
					+ "'");
			}

		}
		return toolkitProvider.get();
	}

	public static <VALUE_TYPE> void setValue(final ITypedKey<VALUE_TYPE> key, final VALUE_TYPE value) {
		getInstance().setValue(key, value);
	}

	public static <VALUE_TYPE> VALUE_TYPE getValue(final ITypedKey<VALUE_TYPE> key) {
		return getInstance().getValue(key);
	}

	public static boolean hasSpiMigLayoutSupport() {
		return getInstance().hasSpiMigLayoutSupport();
	}

	public static IImageRegistry getImageRegistry() {
		return getInstance().getImageRegistry();
	}

	public static IWidgetWrapperFactory getWidgetWrapperFactory() {
		return getInstance().getWidgetWrapperFactory();
	}

	public static IGenericWidgetFactory getWidgetFactory() {
		return getInstance().getWidgetFactory();
	}

	public static ILayoutFactoryProvider getLayoutFactoryProvider() {
		return getInstance().getLayoutFactoryProvider();
	}

	public static IBluePrintFactory getBluePrintFactory() {
		return getInstance().getBluePrintFactory();
	}

	public static ISupportedWidgets getSupportedWidgets() {
		return getInstance().getSupportedWidgets();
	}

	public static IActionBuilderFactory getActionBuilderFactory() {
		return getInstance().getActionBuilderFactory();
	}

	public static IModelFactoryProvider getModelFactoryProvider() {
		return getInstance().getModelFactoryProvider();
	}

	public static IInputContentCreatorFactory getInputContentCreatorFactory() {
		return getInstance().getInputContentCreatorFactory();
	}

	public static IConverterProvider getConverterProvider() {
		return getInstance().getConverterProvider();
	}

	public static IApplicationRunner getApplicationRunner() {
		return getInstance().getApplicationRunner();
	}

	public static IUiThreadAccess getUiThreadAccess() {
		return getInstance().getUiThreadAccess();
	}

	public static IWaitAnimationProcessor getWaitAnimationProcessor() {
		return getInstance().getWaitAnimationProcessor();
	}

	public static IAnimationRunnerBuilder getAnimationRunnerBuilder() {
		return getInstance().getAnimationRunnerBuilder();
	}

	public static IDelayedEventRunnerBuilder getDelayedEventRunnerBuilder() {
		return getInstance().getDelayedEventRunnerBuilder();
	}

	public static IWindow getActiveWindow() {
		return getInstance().getActiveWindow();
	}

	public static List<IWindow> getAllWindows() {
		return getInstance().getAllWindows();
	}

	public static IMessagePane getMessagePane() {
		return getInstance().getMessagePane();
	}

	public static IQuestionPane getQuestionPane() {
		return getInstance().getQuestionPane();
	}

	public static IWidgetUtils getWidgetUtils() {
		return getInstance().getWidgetUtils();
	}

	public static ITextMaskBuilder createTextMaskBuilder() {
		return getInstance().createTextMaskBuilder();
	}

	public static ILoginPane getLoginPane() {
		return getInstance().getLoginPane();
	}

	public static IFrame createRootFrame(final IFrameDescriptor descriptor) {
		return getInstance().createRootFrame(descriptor);
	}

	public static IFrame createRootFrame(final IFrameDescriptor descriptor, final IApplicationLifecycle lifecycle) {
		return getInstance().createRootFrame(descriptor, lifecycle);
	}

	public static Position toScreen(final Position localPosition, final IComponent component) {
		return getInstance().toScreen(localPosition, component);
	}

	public static Position toLocal(final Position screenPosition, final IComponent component) {
		return getInstance().toLocal(screenPosition, component);
	}
}