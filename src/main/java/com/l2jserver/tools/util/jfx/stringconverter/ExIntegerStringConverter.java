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
package com.l2jserver.tools.util.jfx.stringconverter;

import javafx.util.StringConverter;

/**
 * @author HorridoJoho
 */
public class ExIntegerStringConverter extends StringConverter<Integer>
{
	private final Integer _min;
	private final Integer _max;
	private final int _radix;
	
	public ExIntegerStringConverter(Integer min, Integer max, int radix)
	{
		_min = min;
		_max = max;
		_radix = radix;
	}
	
	public ExIntegerStringConverter(int radix)
	{
		this(Integer.MIN_VALUE, Integer.MAX_VALUE, radix);
	}
	
	public ExIntegerStringConverter()
	{
		this(10);
	}
	
	@Override
	public Integer fromString(String value)
	{
		if (value == null)
		{
			return null;
		}
		
		value = value.trim();
		if (value.length() < 1)
		{
			return null;
		}
		
		Integer intValue = Integer.valueOf(value, _radix);
		if (intValue < _min)
		{
			intValue = _min;
		}
		else if (intValue > _max)
		{
			intValue = _max;
		}
		return intValue;
	}
	
	@Override
	public String toString(Integer value)
	{
		if (value == null)
		{
			return "";
		}
		
		if (value < _min)
		{
			value = _min;
		}
		else if (value > _max)
		{
			value = _max;
		}
		
		return Integer.toString(value, _radix);
	}
}
