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
package com.l2jserver.tools.configurator.model;

import java.util.Objects;

/**
 * @author HorridoJoho
 */
public class ConfigDirectiveDefinition
{
	private final String _name;
	private final Class<?> _type;
	private final String _defaultValue;
	
	public ConfigDirectiveDefinition(String name, Class<?> type, String defaultValue)
	{
		Objects.requireNonNull(name);
		Objects.requireNonNull(type);
		Objects.requireNonNull(defaultValue);
		
		_name = name;
		_type = type;
		_defaultValue = defaultValue;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public Class<?> getType()
	{
		return _type;
	}
	
	public String getDefaultValue()
	{
		return _defaultValue;
	}
	
	@Override
	public ConfigDirectiveDefinition clone()
	{
		return this;
	}
}
