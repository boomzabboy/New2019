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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author HorridoJoho
 */
public final class Config
{
	private final String _name;
	private final Map<String, ConfigDirective> _directives;
	
	public Config(String name)
	{
		Objects.requireNonNull(name);
		_name = name;
		_directives = new LinkedHashMap<>();
	}
	
	public ConfigDirective addDirective(String name, Class<?> type, String defaultValue, String value)
	{
		ConfigDirective cd = new ConfigDirective(name, type, defaultValue, value, false);
		_directives.put(name, cd);
		return cd;
	}
	
	public ConfigDirective addDirective(String name, Class<?> type, String defaultValue)
	{
		ConfigDirective cd = new ConfigDirective(name, type, defaultValue, false);
		_directives.put(name, cd);
		return cd;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public Map<String, ConfigDirective> getDirectives()
	{
		return Collections.unmodifiableMap(_directives);
	}
	
	public ConfigDirective getDirective(String name)
	{
		return _directives.get(name);
	}
}
