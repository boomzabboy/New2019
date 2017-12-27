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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.l2jserver.tools.configurator.Configurator;
import com.l2jserver.tools.configurator.frontend.ConfiguratorFrontend;
import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.ConfigDirectiveTable;
import com.l2jserver.tools.configurator.model.Config;
import com.l2jserver.tools.configurator.model.ConfigDirective;
import com.l2jserver.tools.util.BackgroundTaskContext;
import com.l2jserver.tools.util.JfxUtil;
import com.l2jserver.tools.util.jfx.BackgroundTask;
import com.l2jserver.tools.util.jfx.BackgroundTaskThread;
import com.l2jserver.tools.util.jfx.stage.ApplicationStage;
import com.l2jserver.tools.util.jfx.stage.msgbox.MsgBox;
import com.l2jserver.tools.util.jfx.stage.msgbox.MsgBoxButton;
import com.l2jserver.tools.util.jfx.stage.msgbox.MsgBoxResult;

import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 * @author HorridoJoho
 */
public final class MainStage extends ApplicationStage<BorderPane, MainStageController> implements ConfiguratorFrontend
{
	private static final String _FXML_RES_PATH = "/resources/tools/configurator/frontend/jfx/stage/mainstage/MainStage";
	private static final URL _FXML_URL = JfxUtil.getInternationalizedFxmlLocation(MainStage.class, _FXML_RES_PATH);
	private static final String _RES_BUNDLE_NAME = "resources.tools.configurator.frontend.jfx.stage.mainstage.MainStage";
	
	public MainStage()
	{
		super(null, Modality.NONE, StageStyle.DECORATED, Configurator.RESOURCES.AppName, _FXML_URL, ResourceBundle.getBundle(_RES_BUNDLE_NAME), null);
	}
	
	private MsgBoxResult showMessageBox(Image icon, String headline, String description, String details, MsgBoxButton... buttons)
	{
		MsgBox mb = new MsgBox(this, Modality.WINDOW_MODAL, StageStyle.DECORATED, Configurator.RESOURCES.AppName);
		if (icon != null)
		{
			mb.setIcon(icon);
		}
		if (headline != null)
		{
			mb.setHeadline(headline);
		}
		if (description != null)
		{
			mb.setDescription(description);
		}
		if (details != null)
		{
			mb.setDetails(details);
		}
		mb.setButtons(buttons);
		
		mb.showAndWait();
		mb.close();
		return mb.getResult();
	}
	
	private <T> T runTask(String message, String successMessage, String errorMessage, BackgroundTask<T> task, boolean cancelable)
	{
		try
		{
			MsgBox mb = new MsgBox(this, Modality.WINDOW_MODAL, StageStyle.DECORATED, Configurator.RESOURCES.AppName);
			BackgroundTaskThread<T> thread = new BackgroundTaskThread<>(mb, task);
			
			mb.setHeadline(message);
			if (cancelable)
			{
				mb.setButtons(MsgBoxButton.CANCEL);
				mb.setButtonHandler(MsgBoxButton.CANCEL, e -> thread.requestCancelation());
				mb.setCloseHandler(e -> thread.requestCancelation());
			}
			else
			{
				// prevent closing the dialog when cancelation is not supported
				mb.setCloseHandler(e -> e.consume());
			}
			
			thread.start();
			if (thread.isCancelationRequested())
			{
				// the task was canceled
				return null;
			}
			
			if (successMessage != null)
			{
				showMessageBox(null, message, successMessage, null, MsgBoxButton.OK);
			}
			
			return thread.getResult();
		}
		catch (Throwable e)
		{
			try (ByteArrayOutputStream b = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(b))
			{
				e.printStackTrace(ps);
				showMessageBox(null, message, errorMessage, b.toString(), MsgBoxButton.OK);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		
		return null;
	}
	
	private Collection<Tab> loadConfigsTask(BackgroundTaskContext<Collection<Tab>> ctx) throws IOException
	{
		Map<String, Config> configs = Configurator.MODEL.loadConfigs(ctx, this);
		if (ctx.isCancelationRequested())
		{
			return null;
		}
		
		List<Tab> configTabs = new ArrayList<>(configs.size());
		for (Config config : configs.values())
		{
			if (ctx.isCancelationRequested())
			{
				return null;
			}
			
			Tab configTab = new Tab(config.getName());
			configTab.setUserData(config);
			ConfigDirectiveTable directiveList = ConfigDirectiveTable.create();
			
			for (ConfigDirective directive : config.getDirectives().values())
			{
				if (ctx.isCancelationRequested())
				{
					return null;
				}
				
				directiveList.getItems().add(directive);
			}
			
			configTab.setContent(directiveList);
			configTabs.add(configTab);
		}
		
		return configTabs;
	}
	
	private Void saveConfigsTask(BackgroundTaskContext<Void> ctx) throws IOException
	{
		Configurator.MODEL.saveConfigs(ctx, this);
		return null;
	}
	
	private Boolean quitTask(BackgroundTaskContext<Boolean> ctx)
	{
		return Configurator.quit(this);
	}
	
	public void loadConfigs()
	{
		Collection<Tab> configTabs = runTask(Configurator.RESOURCES.LoadingConfigs, Configurator.RESOURCES.LoadingConfigsSuccessful, Configurator.RESOURCES.LoadingConfigsError, this::loadConfigsTask, true);
		if (configTabs != null)
		{
			getController().clearConfigTabs();
			getController().addConfigTabs(configTabs);
		}
	}
	
	public void saveConfigs()
	{
		runTask(Configurator.RESOURCES.SavingConfigs, Configurator.RESOURCES.SavingConfigsSuccessful, Configurator.RESOURCES.SavingConfigsError, this::saveConfigsTask, true);
	}
	
	public void quit()
	{
		Boolean close = runTask(Configurator.RESOURCES.ClosingApp, null, null, this::quitTask, false);
		if ((close != null) && close)
		{
			close();
		}
	}
	
	public void about()
	{
		showMessageBox(null, Configurator.RESOURCES.AppAboutHeadline, Configurator.RESOURCES.AppAbout, null, MsgBoxButton.OK);
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
