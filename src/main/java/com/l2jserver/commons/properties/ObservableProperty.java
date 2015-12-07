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

import java.util.function.BiConsumer;

/**
 * @author HorridoJoho
 * @param <T>
 */
public final class ObservableProperty<T> extends Property<T>
{
	private final BiConsumer<T, T> _changingObserver;
	private final BiConsumer<T, T> _changedObserver;
	
	public ObservableProperty(T initialValue)
	{
		super(initialValue);
		_changingObserver = null;
		_changedObserver = null;
	}
	
	public ObservableProperty(T initialValue, BiConsumer<T, T> changingObserver, BiConsumer<T, T> changedObserver)
	{
		super(initialValue);
		_changingObserver = changingObserver;
		_changedObserver = changedObserver;
	}
	
	@Override
	public void set(T newValue)
	{
		if (_changingObserver != null)
		{
			try
			{
				_changingObserver.accept(get(), newValue);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		super.set(newValue);
		if (_changedObserver != null)
		{
			try
			{
				_changedObserver.accept(get(), newValue);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
