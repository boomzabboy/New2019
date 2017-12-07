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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author HorridoJoho
 */
public final class ConfigDirective extends ConfigDirectiveDefinition implements Cloneable
{
	private final SimpleStringProperty _value;
	private final SimpleBooleanProperty _isDefault;
	private final SimpleBooleanProperty _overwrite;
	
	public ConfigDirective(String name, Class<?> type, String defaultValue, String value, boolean overwrite)
	{
		super(name, type, defaultValue);
		Objects.requireNonNull(value);
		
		_value = new SimpleStringProperty(value);
		_isDefault = new SimpleBooleanProperty();
		if (type == Boolean.class)
		{
			_isDefault.bind(_value.isEqualToIgnoreCase(getDefaultValue()));
		}
		else
		{
			_isDefault.bind(_value.isEqualTo(getDefaultValue()));
		}
		_overwrite = new SimpleBooleanProperty(overwrite);
	}
	
	public ConfigDirective(String name, Class<?> type, String defaultValue, boolean overwrite)
	{
		this(name, type, defaultValue, defaultValue, overwrite);
	}
	
	public void reset()
	{
		valueProperty().set(getDefaultValue());
	}
	
	public SimpleStringProperty valueProperty()
	{
		return _value;
	}
	
	public SimpleBooleanProperty overwriteProperty()
	{
		return _overwrite;
	}
	
	public SimpleBooleanProperty isDefaultProperty()
	{
		return _isDefault;
	}
	
	public String getValue()
	{
		return valueProperty().get();
	}
	
	@Override
	public ConfigDirective clone()
	{
		return new ConfigDirective(getName(), getType(), getDefaultValue(), valueProperty().get(), overwriteProperty().get());
	}
}
