/**
 * Copyright (C) 2016 MadMusic4001
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

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Calendar;

import javax.inject.Singleton;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 4/26/2016.
 */
@Singleton
public final class DateUtils {
	public static String getFormattedDateOrTime(Context context, long timeInMillis) {
		String result;

		Calendar modDateTime = Calendar.getInstance();
		modDateTime.setTimeInMillis(timeInMillis);
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		if (modDateTime.after(today)) {
			result = DateFormat.getTimeFormat(context).format(modDateTime.getTime());
		}
		else {
			result = DateFormat.getDateFormat(context).format(modDateTime.getTime());
		}
		return result;
	}
}
