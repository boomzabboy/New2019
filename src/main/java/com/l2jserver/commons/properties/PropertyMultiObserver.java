/*
 * Copyright (C) 2004-2015 L2J Server
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
package com.l2jserver.commons.properties;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author HorridoJoho
 * @param <T> the property type
 */
public class PropertyMultiObserver<T> implements IPropertyObserver<T>
{
	private final ConcurrentLinkedQueue<IPropertyObserver<T>> _observers;
	
	public PropertyMultiObserver()
	{
		_observers = new ConcurrentLinkedQueue<>();
	}
	
	public void addObserver(IPropertyObserver<T> observer)
	{
		_observers.add(observer);
	}
	
	public void removeObserver(IPropertyObserver<T> observer)
	{
		_observers.remove(observer);
	}
	
	@Override
	public void observe(T oldValue, T newValue)
	{
		for (IPropertyObserver<T> observer : _observers)
		{
			try
			{
				observer.observe(oldValue, newValue);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
