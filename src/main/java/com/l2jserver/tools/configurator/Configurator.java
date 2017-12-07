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

import com.l2jserver.tools.configurator.frontend.IConfiguratorFrontend;
import com.l2jserver.tools.configurator.frontend.jfx.JfxApplication;

import javafx.application.Application;

/**
 * @author HorridoJoho
 */
public final class Configurator
{
	public static final ConfiguratorResources RESOURCES = new ConfiguratorResources();
	public static final ConfiguratorModel MODEL = new ConfiguratorModel();
	
	public static void main(String[] args)
	{
		Application.launch(JfxApplication.class, args);
	}
	
	public static final Boolean quit(IConfiguratorFrontend frontend)
	{
		return true;
	}
}
