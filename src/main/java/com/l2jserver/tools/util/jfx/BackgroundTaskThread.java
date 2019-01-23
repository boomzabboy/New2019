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

import com.l2jserver.tools.util.BackgroundTaskContext;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author HorridoJoho
 */
public final class BackgroundTaskThread<T> extends Thread implements BackgroundTaskContext<T>
{
	private final Stage _dialog;
	private final BackgroundTask<T> _task;
	private final BooleanProperty _isCancelationRequested;
	
	private Throwable _thrown;
	private T _result;
	
	public BackgroundTaskThread(Stage dialog, BackgroundTask<T> task)
	{
		super("L2J-TOOLS-JFX-BackgroundTaskThread");
		Objects.requireNonNull(dialog);
		Objects.requireNonNull(task);
		
		_dialog = dialog;
		_task = task;
		_isCancelationRequested = new SimpleBooleanProperty(false);
	}
	
	@Override
	public void start()
	{
		_dialog.addEventHandler(WindowEvent.WINDOW_SHOWN, this::dialogShown);
		_dialog.showAndWait();
		try
		{
			// in case the dialog got closed by UI interaction (cancelation etc.), we need to wait for the thread to terminate
			join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		if (getThrown() != null)
		{
			throw new RuntimeException(getThrown());
		}
	}
	
	private void dialogShown(WindowEvent e)
	{
		_dialog.removeEventHandler(WindowEvent.WINDOW_SHOWN, this::dialogShown);
		try
		{
			super.start();
		}
		catch (Throwable t)
		{
			_thrown = t;
			finishDialog();
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			_result = _task.get(this);
		}
		catch (Throwable t)
		{
			_thrown = t;
		}
		finally
		{
			finishDialog();
		}
	}
	
	private void finishDialog()
	{
		if (_dialog != null)
		{
			Platform.runLater(() -> _dialog.close());
		}
	}
	
	public ReadOnlyBooleanProperty isCancelationRequestedProperty()
	{
		return _isCancelationRequested;
	}
	
	@Override
	public void requestCancelation()
	{
		_isCancelationRequested.set(true);
	}
	
	@Override
	public boolean isCancelationRequested()
	{
		return _isCancelationRequested.get();
	}
	
	@Override
	public Throwable getThrown()
	{
		return _thrown;
	}
	
	@Override
	public T getResult()
	{
		return _result;
	}
}
