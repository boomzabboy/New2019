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
package com.l2jserver.tools.util.jfx;

import java.util.Objects;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @author HorridoJoho
 */
public class GuardedChangeListener<T> implements ChangeListener<T>
{
	private final BooleanProperty _guard;
	private final ChangeListener<T> _wrapped;
	
	public GuardedChangeListener(BooleanProperty guard, ChangeListener<T> wrapped)
	{
		Objects.requireNonNull(guard);
		Objects.requireNonNull(wrapped);
		_guard = guard;
		_wrapped = wrapped;
	}
	
	@Override
	public final void changed(ObservableValue<? extends T> observable, T oldValue, T newValue)
	{
		_guard.set(true);
		try
		{
			_wrapped.changed(observable, oldValue, newValue);
		}
		finally
		{
			_guard.set(false);
		}
	}
}
