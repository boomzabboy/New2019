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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.l2jserver.tools.configurator.Configurator;
import com.l2jserver.tools.configurator.frontend.IConfiguratorFrontend;
import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.ConfigDirectiveTable;
import com.l2jserver.tools.configurator.model.Config;
import com.l2jserver.tools.configurator.model.ConfigDirective;
import com.l2jserver.tools.util.JfxUtil;
import com.l2jserver.tools.util.jfx.BackgroundTask;
import com.l2jserver.tools.util.jfx.stage.ApplicationStage;
import com.l2jserver.tools.util.jfx.stage.msgbox.MsgBox;
import com.l2jserver.tools.util.jfx.stage.msgbox.MsgBoxButton;
import com.l2jserver.tools.util.jfx.stage.msgbox.MsgBoxResult;

import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 * @author HorridoJoho
 */
public final class MainStage extends ApplicationStage<BorderPane, MainStageController> implements IConfiguratorFrontend
{
	private static final String _FXML_RES_PATH = "/resources/tools/configurator/frontend/jfx/stage/mainstage/MainStage";
	private static final URL _FXML_URL = JfxUtil.getInternationalizedFxmlLocation(MainStage.class, _FXML_RES_PATH);
	private static final String _RES_BUNDLE_NAME = "resources.tools.configurator.frontend.jfx.stage.mainstage.MainStage";
	
	public MainStage()
	{
		super(null, Modality.NONE, StageStyle.DECORATED, Configurator.RESOURCES.AppName, _FXML_URL, ResourceBundle.getBundle(_RES_BUNDLE_NAME), null);
	}
	
	private MsgBoxResult showMessageBox(String message, MsgBoxButton... buttons)
	{
		MsgBox mb = new MsgBox(this, Modality.WINDOW_MODAL, null, Configurator.RESOURCES.AppName, message, buttons);
		mb.initStyle(StageStyle.UTILITY);
		mb.setResizable(false);
		mb.showAndWait();
		mb.close();
		return mb.getResult();
	}
	
	private <T> T runTask(String message, String successMessage, String errorMessage, BackgroundTask<T> task)
	{
		MsgBox mb = new MsgBox(this, Modality.WINDOW_MODAL, null, Configurator.RESOURCES.AppName, message, MsgBoxButton.ABORT);
		mb.initStyle(StageStyle.UTILITY);
		mb.setResizable(false);
		try
		{
			T result = JfxUtil.runBackgroundTaskWithDialog(mb, task);
			if (successMessage != null)
			{
				showMessageBox(successMessage, MsgBoxButton.OK);
			}
			return result;
		}
		catch (Throwable e)
		{
			showMessageBox(errorMessage + "\n\n" + e.toString(), MsgBoxButton.OK);
		}
		finally
		{
			mb.close();
		}
		
		return null;
	}
	
	private Collection<Tab> loadConfigsTask() throws IOException
	{
		Map<String, Config> configs = Configurator.MODEL.loadConfigs(this);
		
		List<Tab> configTabs = new ArrayList<>(configs.size());
		for (Config config : configs.values())
		{
			Tab configTab = new Tab(config.getName());
			configTab.setUserData(config);
			ConfigDirectiveTable directiveList = ConfigDirectiveTable.create();
			
			for (ConfigDirective directive : config.getDirectives().values())
			{
				directiveList.getItems().add(directive);
			}
			
			configTab.setContent(directiveList);
			configTabs.add(configTab);
		}
		
		return configTabs;
	}
	
	private Void saveConfigsTask() throws IOException
	{
		Configurator.MODEL.saveConfigs(this);
		return null;
	}
	
	private Boolean quitTask()
	{
		return Configurator.quit(this);
	}
	
	public void loadConfigs()
	{
		Collection<Tab> configTabs = runTask(Configurator.RESOURCES.LoadingConfigs, Configurator.RESOURCES.LoadingConfigsSuccessful, Configurator.RESOURCES.LoadingConfigsError, this::loadConfigsTask);
		if (configTabs != null)
		{
			getController().clearConfigTabs();
			getController().addConfigTabs(configTabs);
		}
	}
	
	public void saveConfigs()
	{
		runTask(Configurator.RESOURCES.SavingConfigs, Configurator.RESOURCES.SavingConfigsSuccessful, Configurator.RESOURCES.SavingConfigsError, this::saveConfigsTask);
	}
	
	public void quit()
	{
		Boolean close = runTask(Configurator.RESOURCES.ClosingApp, null, null, this::quitTask);
		if ((close != null) && close)
		{
			close();
		}
	}
	
	public void about()
	{
		showMessageBox(Configurator.RESOURCES.AppAbout, MsgBoxButton.OK);
	}
	
	@Override
	public void reportInfo(boolean drawAttention, String message)
	{
	}
	
	@Override
	public void reportInfo(boolean drawAttention, String message, Object... args)
	{
	}
	
	@Override
	public void reportWarn(boolean drawAttention, String message)
	{
	}
	
	@Override
	public void reportWarn(boolean drawAttention, String message, Object... args)
	{
	}
	
	@Override
	public void reportError(boolean drawAttention, String message)
	{
	}
	
	@Override
	public void reportError(boolean drawAttention, Throwable t, String message)
	{
	}
	
	@Override
	public void reportError(boolean drawAttention, Throwable t, String message, Object... args)
	{
	}
	
	@Override
	public String requestUserInput(String message, Object... args)
	{
		return null;
	}
	
	@Override
	public boolean requestUserConfirm(String message, Object... args)
	{
		return false;
	}
}
