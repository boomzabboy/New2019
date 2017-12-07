/*
 * Copyright (C) 2004-2017 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.tools.util;

import java.util.Locale;

/**
 * @author HorridoJoho
 */
public final class LocaleUtil
{
	public static Locale getParentLocale(Locale locale)
	{
		String variant = locale.getVariant();
		if (!variant.equals(""))
		{
			return new Locale(locale.getLanguage(), locale.getCountry());
		}
		
		String country = locale.getCountry();
		if (!country.equals(""))
		{
			return new Locale(locale.getLanguage());
		}
		
		String language = locale.getLanguage();
		if (!language.equals(""))
		{
			return Locale.ROOT;
		}
		
		return null;
	}
	
	public static String getInternationalizationString(Locale locale)
	{
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();
		
		if (!variant.equals(""))
		{
			return language + "_" + country + "_" + variant;
		}
		else if (!country.equals(""))
		{
			return language + "_" + country;
		}
		else if (!language.equals(""))
		{
			return language;
		}
		
		return "";
	}
}
