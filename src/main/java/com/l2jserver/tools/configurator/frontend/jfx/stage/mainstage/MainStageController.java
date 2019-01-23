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
package com.l2jserver.tools.configurator.frontend.jfx.stage.mainstage;

import java.util.Collection;

import com.l2jserver.tools.configurator.frontend.jfx.JfxApplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author HorridoJoho
 */
public class MainStageController
{
	@FXML
	private MenuItem _menuFileLoad;
	@FXML
	private MenuItem _menuFileSave;
	@FXML
	private MenuItem _menuFileQuit;
	@FXML
	private MenuItem _menuHelpAbout;
	@FXML
	private TabPane _tabsConfigurations;
	
	@FXML
	private void onMenuFileLoad(ActionEvent event)
	{
		JfxApplication.loadConfigs();
	}
	
	@FXML
	private void onMenuFileSave(ActionEvent event)
	{
		JfxApplication.saveConfigs();
	}
	
	@FXML
	private void onMenuFileQuit(ActionEvent event)
	{
		JfxApplication.quit();
	}
	
	@FXML
	private void onMenuHelpAbout(ActionEvent event)
	{
		JfxApplication.about();
	}
	
	void clearConfigTabs()
	{
		_tabsConfigurations.getTabs().clear();
	}
	
	void addConfigTab(Tab configTab)
	{
		_tabsConfigurations.getTabs().add(configTab);
	}
	
	void addConfigTabs(Collection<Tab> configTabs)
	{
		_tabsConfigurations.getTabs().addAll(configTabs);
	}
}
