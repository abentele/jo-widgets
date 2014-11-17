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

package org.jowidgets.api.convert;

public final class LinearSliderConverter {

	private LinearSliderConverter() {}

	public static <VALUE_TYPE extends Number> ILinearSliderConverterBuilder<VALUE_TYPE> builder() {
		return SliderConverterFactory.linearConverterBuilder();
	}

	public static <VALUE_TYPE extends Number> ISliderViewerConverter<VALUE_TYPE> create(final VALUE_TYPE max) {
		return SliderConverterFactory.linearConverter(max);
	}

	public static <VALUE_TYPE extends Number> ISliderViewerConverter<VALUE_TYPE> create(final VALUE_TYPE min, final VALUE_TYPE max) {
		return SliderConverterFactory.linearConverter(min, max);
	}

	public static ISliderViewerConverter<Double> create() {
		return SliderConverterFactory.linearConverter();
	}

	public static ISliderViewerConverter<Double> create(final double max) {
		return SliderConverterFactory.linearConverter(max);
	}

	public static ISliderViewerConverter<Double> create(final double min, final double max) {
		return SliderConverterFactory.linearConverter(min, max);
	}

	public static ISliderViewerConverter<Float> create(final float max) {
		return SliderConverterFactory.linearConverter(max);
	}

	public static ISliderViewerConverter<Float> create(final float min, final float max) {
		return SliderConverterFactory.linearConverter(min, max);
	}

	public static ISliderViewerConverter<Integer> create(final int max) {
		return SliderConverterFactory.linearConverter(max);
	}

	public static ISliderViewerConverter<Integer> create(final int min, final int max) {
		return SliderConverterFactory.linearConverter(min, max);
	}

	public static ISliderViewerConverter<Long> create(final long max) {
		return SliderConverterFactory.linearConverter(max);
	}

	public static ISliderViewerConverter<Long> create(final long min, final long max) {
		return SliderConverterFactory.linearConverter(min, max);
	}

}