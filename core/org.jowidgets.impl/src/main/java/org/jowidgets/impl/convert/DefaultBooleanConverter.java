/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.convert;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.tools.converter.AbstractConverter;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

class DefaultBooleanConverter extends AbstractConverter<Boolean> implements IConverter<Boolean> {

	private final String[] trueStrings;
	private final String[] falseStrings;
	private final String matchingRegExp;

	private final IValidator<String> stringValidator;

	DefaultBooleanConverter(final String[] trueStrings, final String[] falseStrings, final String matchingRegExp) {
		super();
		this.trueStrings = trueStrings;
		this.falseStrings = falseStrings;
		this.matchingRegExp = matchingRegExp;
		this.stringValidator = new IValidator<String>() {
			@Override
			public IValidationResult validate(final String input) {
				if (input != null && !input.isEmpty() && convertToObject(input) == null) {
					return ValidationResult.error("Must be '" + trueStrings[0] + "' or '" + falseStrings[0] + "'");
				}
				return ValidationResult.ok();
			}
		};
	}

	@Override
	public Boolean convertToObject(final String string) {
		if (contains(trueStrings, string)) {
			return Boolean.valueOf(true);
		}
		else if (contains(falseStrings, string)) {
			return Boolean.valueOf(false);
		}
		else {
			return null;
		}
	}

	@Override
	public String convertToString(final Boolean value) {
		if (value != null) {
			if (value.booleanValue()) {
				return trueStrings[0];
			}
			else {
				return falseStrings[0];
			}
		}
		return "";
	}

	@Override
	public String getAcceptingRegExp() {
		return matchingRegExp;
	}

	@Override
	public IValidator<String> getStringValidator() {
		return stringValidator;
	}

	private boolean contains(final String[] tangas, final String tanga) {
		for (final String string : tangas) {
			if (string.equals(tanga)) {
				return true;
			}
		}
		return false;
	}

}