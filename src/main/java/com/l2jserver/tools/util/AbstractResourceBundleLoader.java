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

import java.lang.reflect.Field;
import java.util.ResourceBundle;

import javax.annotation.Resource;

/**
 * @author HorridoJoho
 */
public abstract class AbstractResourceBundleLoader
{
	private final ResourceBundle _bundle;
	
	protected AbstractResourceBundleLoader(String bundleName)
	{
		_bundle = ResourceBundle.getBundle(bundleName);
	}
	
	private boolean checkModifiers(Field f, int... modifiers)
	{
		for (int m : modifiers)
		{
			if ((f.getModifiers() & m) == 0)
			{
				return false;
			}
		}
		return true;
	}
	
	protected void load()
	{
		for (Field f : getClass().getFields())
		{
			Resource r = f.getAnnotation(Resource.class);
			if (r == null)
			{
				continue;
			}
			
			boolean isAccessible = f.isAccessible();
			try
			{
				if (!isAccessible)
				{
					f.setAccessible(true);
				}
				f.set(this, _bundle.getString(f.getName()));
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
				if (!isAccessible)
				{
					f.setAccessible(false);
				}
			}
		}
	}
	
	public String get(String key)
	{
		return _bundle.getString(key);
	}
}
