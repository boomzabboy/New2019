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

import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author HorridoJoho
 */
public class BackgroundTaskRunner<T> implements Runnable
{
	private final Stage _dialog;
	private final BackgroundTask<T> _task;
	private final Thread _thread;
	
	private Throwable _thrown;
	private T _result;
	
	public BackgroundTaskRunner(Stage dialog, BackgroundTask<T> task)
	{
		Objects.requireNonNull(dialog);
		Objects.requireNonNull(task);
		_dialog = dialog;
		_task = task;
		_thread = new Thread(this, "L2J-TOOLS-JFX-BackgroundTaskRunner");
		
		_dialog.initModality(Modality.WINDOW_MODAL);
		_dialog.addEventHandler(WindowEvent.WINDOW_SHOWN, this::dialogShown);
		_dialog.showAndWait();
	}
	
	@Override
	public void run()
	{
		try
		{
			_result = _task.get();
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
	
	public Throwable getThrown()
	{
		return _thrown;
	}
	
	public T getResult()
	{
		return _result;
	}
	
	private void dialogShown(WindowEvent e)
	{
		_dialog.removeEventHandler(WindowEvent.WINDOW_SHOWN, this::dialogShown);
		try
		{
			_thread.start();
		}
		catch (Throwable t)
		{
			_thrown = t;
			finishDialog();
		}
	}
}
