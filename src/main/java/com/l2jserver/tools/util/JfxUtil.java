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
package com.l2jserver.tools.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.l2jserver.tools.util.jfx.BackgroundTask;
import com.l2jserver.tools.util.jfx.BackgroundTaskRunner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.BuilderFactory;
import javafx.util.Callback;

/**
 * @author HorridoJoho
 */
public final class JfxUtil
{
	private static <T> void ifNotNull(T o, Consumer<T> c)
	{
		if (o != null)
		{
			c.accept(o);
		}
	}
	
	public static URL getInternationalizedFxmlLocation(Locale locale, Class<?> clazz, String path)
	{
		Locale originalLocale = locale;
		
		Objects.requireNonNull(locale);
		Objects.requireNonNull(clazz);
		Objects.requireNonNull(path);
		
		while (locale != null)
		{
			String internationalizationString = LocaleUtil.getInternationalizationString(locale);
			String resourceName = path;
			if (!internationalizationString.isEmpty())
			{
				resourceName += "_" + internationalizationString;
			}
			resourceName += ".fxml";
			
			URL location = clazz.getResource(resourceName);
			if (location != null)
			{
				return location;
			}
			
			locale = LocaleUtil.getParentLocale(locale);
		}
		
		throw new RuntimeException("Could not find location for path " + path + ", locale " + originalLocale.toString() + " and the parent locales!");
	}
	
	public static URL getInternationalizedFxmlLocation(Class<?> clazz, String path)
	{
		return getInternationalizedFxmlLocation(Locale.getDefault(), clazz, path);
	}
	
	public static FXMLLoader createFxmlLoader(URL location, BuilderFactory optBuilderFactory, Charset optCharset, ResourceBundle optResources)
	{
		Objects.requireNonNull(location);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(location);
		
		ifNotNull(optBuilderFactory, (o) -> loader.setBuilderFactory(o));
		ifNotNull(optCharset, (o) -> loader.setCharset(o));
		ifNotNull(optResources, (o) -> loader.setResources(o));
		return loader;
	}
	
	public static void loadFxml(FXMLLoader loader, ClassLoader optClassLoader, Callback<Class<?>, Object> optControllerFactory, Object optRoot) throws IOException
	{
		Objects.requireNonNull(loader);
		
		loader.setControllerFactory(null);
		loader.setController(null);
		
		ifNotNull(optControllerFactory, (o) -> loader.setControllerFactory(o));
		loadFxml(loader, optClassLoader, optRoot);
	}
	
	public static void loadFxml(FXMLLoader loader, ClassLoader optClassLoader, Object optController, Object optRoot) throws IOException
	{
		Objects.requireNonNull(loader);
		
		loader.setControllerFactory(null);
		loader.setController(null);
		
		ifNotNull(optController, (o) -> loader.setController(o));
		loadFxml(loader, optClassLoader, optRoot);
	}
	
	private static void loadFxml(FXMLLoader loader, ClassLoader optClassLoader, Object optRoot) throws IOException
	{
		Objects.requireNonNull(loader);
		
		loader.setRoot(null);
		
		ifNotNull(optClassLoader, (o) -> loader.setClassLoader(o));
		ifNotNull(optRoot, (o) -> loader.setRoot(o));
		loader.load();
	}
	
	public static <T> T runBackgroundTaskWithDialog(Window owner, String title, Parent root, BackgroundTask<T> task) throws Throwable
	{
		Stage s = new Stage();
		s.initOwner(owner);
		s.initModality(Modality.WINDOW_MODAL);
		s.initStyle(StageStyle.DECORATED);
		
		s.setTitle(title);
		s.setScene(new Scene(root));
		
		return runBackgroundTaskWithDialog(s, task);
	}
	
	public static <T> T runBackgroundTaskWithDialog(Stage s, BackgroundTask<T> task) throws Throwable
	{
		s.initModality(Modality.WINDOW_MODAL);
		s.initStyle(StageStyle.UTILITY);
		s.setResizable(false);
		
		BackgroundTaskRunner<T> runner = new BackgroundTaskRunner<>(s, task);
		s.close();
		if (runner.getThrown() != null)
		{
			throw runner.getThrown();
		}
		return runner.getResult();
	}
}
