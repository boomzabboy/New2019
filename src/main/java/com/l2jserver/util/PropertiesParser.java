/*
 * Copyright (C) 2004-2016 L2J Server
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
package com.l2jserver.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.Config;

/**
 * Simplifies loading of property files and adds logging if a non existing property is requested.
 * @author NosBit, HorridoJoho
 */
public final class PropertiesParser
{
	private static final Logger _LOG = LoggerFactory.getLogger(PropertiesParser.class.getName());
	private static final Path[] _GENERATED_CONFIG_FILES =
	{
		Config.CONFIG_GENERATED_FOLDER.resolve(Config.HEXID_FILE)
	};
	
	private final Properties _properties = new Properties();
	private final Path _file;
	
	public PropertiesParser(String name)
	{
		this(Paths.get(name));
	}
	
	public PropertiesParser(Path file)
	{
		_file = file;
		
		boolean isGeneratedFile = isGeneratedProperties(Config.CONFIG_GENERATED_FOLDER.resolve(file));
		if (!isGeneratedFile)
		{
			try (InputStream is = Files.newInputStream(Config.CONFIG_DEFAULT_FOLDER.resolve(file), StandardOpenOption.READ))
			{
				_properties.load(is);
			}
			catch (FileNotFoundException | NoSuchFileException e)
			{
				_LOG.warn("Default config {} could not be found!", file, e);
			}
			catch (Exception e)
			{
				_LOG.warn("Default config {} could not be loaded!", file, e);
			}
		}
		
		Path fileFolder = isGeneratedFile ? Config.CONFIG_GENERATED_FOLDER : Config.CONFIG_OVERWRITE_FOLDER;
		try (InputStream is = Files.newInputStream(fileFolder.resolve(file), StandardOpenOption.READ))
		{
			Properties overwriteProps = new Properties();
			overwriteProps.load(is);
			
			for (Map.Entry<Object, Object> e : overwriteProps.entrySet())
			{
				if (!_properties.containsKey(e.getKey()) && !isGeneratedFile)
				{
					_LOG.info("{}: Overwrite config has deprecated property {}.", file, e.getKey());
				}
				
				_properties.put(e.getKey(), e.getValue());
			}
		}
		catch (FileNotFoundException | NoSuchFileException e)
		{
			if (isGeneratedFile)
			{
				_LOG.warn("Generated config {} couldn't be found!", file, e);
			}
		}
		catch (Exception e)
		{
			if (isGeneratedFile)
			{
				_LOG.warn("Generated config {} couldn't be loaded!", file, e);
			}
			else
			{
				_LOG.warn("Overwrite config {} couldn't be loaded!", file, e);
			}
		}
	}
	
	private boolean isGeneratedProperties(Path file)
	{
		for (Path generatedConfigFile : _GENERATED_CONFIG_FILES)
		{
			try
			{
				if (Files.isSameFile(file, generatedConfigFile))
				{
					return true;
				}
			}
			catch (IOException e)
			{
				_LOG.debug("Failed to check for same file!", e);
			}
		}
		
		return false;
	}
	
	public boolean containskey(String key)
	{
		return _properties.containsKey(key);
	}
	
	public boolean containsNonEmptyKey(String key)
	{
		return _properties.containsKey(key) && !_properties.getProperty(key).trim().isEmpty();
	}
	
	/**
	 * @param <T> the defaultValue type
	 * @param key the property key
	 * @param defaultValue the default value
	 * @return the trimmed value or null if the specified property key does not exist
	 */
	private <T> String getStringValue(String key, T defaultValue)
	{
		Objects.requireNonNull(key);
		Objects.requireNonNull(defaultValue);
		
		String value = _properties.getProperty(key);
		if (value == null)
		{
			_LOG.warn("{}: The property {} is missing! Using default value {}.", _file, key, defaultValue);
			return null;
		}
		return value.trim();
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getGenericValue(String key, T defaultValue)
	{
		Objects.requireNonNull(key);
		
		String value = getStringValue(key, defaultValue);
		if (value == null)
		{
			return defaultValue;
		}
		
		Class<?> defaultValueClass = defaultValue.getClass();
		try
		{
			return (T) defaultValue.getClass().getMethod("valueOf", String.class).invoke(null, value);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			_LOG.warn("{}: The property {} could not be parsed! Failed to call {}#valueOf. Using default value {}.", _file, key, defaultValueClass.getSimpleName(), defaultValue, e);
			return defaultValue;
		}
		catch (Exception e)
		{
			_LOG.warn("{}: The value {} for the property {} is invalid! The type is {}. Using default value {}.", _file, value, key, defaultValueClass.getSimpleName(), defaultValue, e);
			return defaultValue;
		}
	}
	
	public boolean getBoolean(String key, boolean defaultValue)
	{
		return getGenericValue(key, defaultValue);
	}
	
	public byte getByte(String key, byte defaultValue)
	{
		return getGenericValue(key, defaultValue);
	}
	
	public short getShort(String key, short defaultValue)
	{
		return getGenericValue(key, defaultValue);
	}
	
	public int getInt(String key, int defaultValue)
	{
		return getGenericValue(key, defaultValue);
	}
	
	public long getLong(String key, long defaultValue)
	{
		return getGenericValue(key, defaultValue);
	}
	
	public float getFloat(String key, float defaultValue)
	{
		return getGenericValue(key, defaultValue);
	}
	
	public double getDouble(String key, double defaultValue)
	{
		return getGenericValue(key, defaultValue);
	}
	
	public String getString(String key, String defaultValue)
	{
		String value = getStringValue(key, defaultValue);
		if (value == null)
		{
			return defaultValue;
		}
		
		return value;
	}
	
	public <T extends Enum<T>> T getEnum(String key, Class<T> clazz, T defaultValue)
	{
		return getGenericValue(key, defaultValue);
	}
}
