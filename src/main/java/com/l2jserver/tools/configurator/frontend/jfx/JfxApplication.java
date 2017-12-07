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
package com.l2jserver.tools.configurator.frontend.jfx;

import com.l2jserver.tools.configurator.frontend.jfx.stage.mainstage.MainStage;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author HorridoJoho
 */
public class JfxApplication extends Application
{
	private static MainStage _mainStage = null;
	
	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.close();
		
		_mainStage = new MainStage();
		_mainStage.setOnShown(this::mainStageShown);
		_mainStage.show();
	}
	
	private void mainStageShown(WindowEvent e)
	{
		_mainStage.setOnShown(null);
		_mainStage.layout();
		_mainStage.loadConfigs();
	}
	
	public static void loadConfigs()
	{
		_mainStage.loadConfigs();
	}
	
	public static void saveConfigs()
	{
		_mainStage.saveConfigs();
	}
	
	public static void quit()
	{
		_mainStage.quit();
	}
	
	public static void about()
	{
		_mainStage.about();
	}
}
