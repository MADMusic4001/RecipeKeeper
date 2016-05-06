/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.recipekeeper.model.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Static utility methods for dealing with numeric values
 */
public final class NumberUtils {
	private static final Map<BigDecimal, String> fractionMap = new HashMap<>();

	static {
		fractionMap.put(new BigDecimal(".125"), "1/8");
		fractionMap.put(new BigDecimal(".25"), "1/4");
		fractionMap.put(new BigDecimal(".375"), "3/8");
		fractionMap.put(new BigDecimal(".5"), "1/2");
		fractionMap.put(new BigDecimal(".625"), "5/8");
		fractionMap.put(new BigDecimal(".75"), "3/4");
		fractionMap.put(new BigDecimal(".875"), "7/8");
		fractionMap.put(new BigDecimal(".333"), "1/3");
		fractionMap.put(new BigDecimal(".667"), "2/3");
	}

	public static BigDecimal fromDisplayString(Locale locale, String value)
	throws ParseException {
		if(value == null) {
			return null;
		}

		if(value.contains("/")) {
			int position = value.indexOf('/');
			BigDecimal numerator = new BigDecimal(value.substring(0, position));
			BigDecimal denominator = new BigDecimal(value.substring(position + 1));
			return numerator.divide(denominator);
		} else {
			DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance(locale);
			formatter.setParseBigDecimal(true);
			return (BigDecimal)formatter.parse(value);
		}
	}

	public static String toDisplayString(Locale locale, BigDecimal value) {
		if(value == null) {
			return null;
		}

		BigDecimal fraction = value.remainder(BigDecimal.ONE);
		if(fraction.compareTo(BigDecimal.ZERO) == 0) {
			return value.toString();
		}

		BigInteger integer = value.toBigInteger();
		StringBuilder result = new StringBuilder();
		if(integer.compareTo(BigInteger.ZERO) != 0) {
			result.append(integer.toString()).append(" ");
		}
		for(Map.Entry<BigDecimal, String> entry : fractionMap.entrySet()) {
			if(entry.getKey().compareTo(fraction) == 0) {
				return result.append(entry.getValue()).toString();
			}
		}
		return String.format(locale, "%1$.3f", value);
	}
}
