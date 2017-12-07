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
package com.l2jserver.tools.configurator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.l2jserver.tools.configurator.frontend.IConfiguratorFrontend;
import com.l2jserver.tools.configurator.model.Config;
import com.l2jserver.tools.configurator.model.ConfigDirective;
import com.l2jserver.tools.configurator.model.LinkedProperties;

/**
 * @author HorridoJoho
 */
public final class ConfiguratorModel
{
	private static final Path _CONFIGS_PATH = Paths.get("config");
	private static final Path _CONFIGS_DEFAULT_PATH = _CONFIGS_PATH.resolve("default");
	private static final Path _CONFIGS_OVERWRITE_PATH = _CONFIGS_PATH;
	
	private final Map<String, Config> _configs = new LinkedHashMap<>();
	
	ConfiguratorModel()
	{
	}
	
	private Class<?> guessTypeClass(String value)
	{
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
		{
			return Boolean.class;
		}
		else if (value.equalsIgnoreCase("localhost") || value.equalsIgnoreCase("127.0.0.1"))
		{
			return InetAddress.class;
		}
		
		return String.class;
	}
	
	public Map<String, Config> loadConfigs(IConfiguratorFrontend frontend) throws IOException
	{
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(_CONFIGS_DEFAULT_PATH, "*.properties"))
		{
			Map<String, Config> configs = new LinkedHashMap<>();
			
			for (Path dirEntry : dirStream)
			{
				if (Files.isDirectory(dirEntry))
				{
					continue;
				}
				
				Path fileName = dirEntry.getFileName();
				Config config = new Config(fileName.toString());
				
				LinkedProperties defaultProps = new LinkedProperties();
				try (InputStream is = Files.newInputStream(dirEntry))
				{
					defaultProps.load(is);
				}
				
				for (Map.Entry<String, String> directiveEntry : defaultProps.linkedEntrySet())
				{
					config.addDirective(directiveEntry.getKey(), guessTypeClass(directiveEntry.getValue()), directiveEntry.getValue());
				}
				
				Path overwritePath = _CONFIGS_OVERWRITE_PATH.resolve(fileName);
				if (Files.exists(overwritePath) && !Files.isDirectory(overwritePath))
				{
					Properties overwriteProps = new Properties();
					try (InputStream is = Files.newInputStream(overwritePath))
					{
						overwriteProps.load(is);
					}
					
					for (Map.Entry<Object, Object> directiveEntry : overwriteProps.entrySet())
					{
						ConfigDirective cd = config.getDirective((String) directiveEntry.getKey());
						if (cd == null)
						{
							continue;
						}
						cd.valueProperty().set((String) directiveEntry.getValue());
						cd.overwriteProperty().set(true);
					}
				}
				
				configs.put(config.getName(), config);
			}
			
			_configs.clear();
			_configs.putAll(configs);
		}
		
		return _configs;
	}
	
	public void saveConfigs(IConfiguratorFrontend frontend) throws IOException
	{
		for (Config config : _configs.values())
		{
			Properties props = null;
			for (Map.Entry<String, ConfigDirective> directiveEntry : config.getDirectives().entrySet())
			{
				ConfigDirective directive = directiveEntry.getValue();
				if (!directive.overwriteProperty().get() && directive.isDefaultProperty().get())
				{
					continue;
				}
				
				if (props == null)
				{
					props = new Properties();
				}
				
				props.setProperty(directive.getName(), directive.valueProperty().get());
			}
			
			Path overwritePath = _CONFIGS_OVERWRITE_PATH.resolve(config.getName());
			if (props != null)
			{
				try (OutputStream os = Files.newOutputStream(overwritePath))
				{
					props.store(os, null);
				}
			}
			else
			{
				Files.deleteIfExists(overwritePath);
			}
		}
	}
	
	public Map<String, Config> getConfigs()
	{
		return _configs;
	}
}
