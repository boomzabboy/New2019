package com.l2jserver.tools.util.jfx.stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import com.l2jserver.tools.util.JfxUtil;
import com.l2jserver.tools.util.jfx.StageController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public abstract class ApplicationStage<T extends Region, V extends StageController> extends Stage
{
	private final T _root;
	private final V _controller;
	
	public ApplicationStage(Window owner, Modality modality, StageStyle style, String title, FXMLLoader loader, ClassLoader optFxmlClassLoader)
	{
		initOwner(owner);
		initModality(modality);
		initStyle(style);
		
		try
		{
			JfxUtil.loadFxml(loader, optFxmlClassLoader, null, null);
		}
		catch (IOException e)
		{
			throw new UncheckedIOException(e);
		}
		
		_root = loader.getRoot();
		_controller = loader.getController();
		setTitle(title);
		setScene(new Scene(_root));
	}
	
	public ApplicationStage(Window owner, Modality modality, StageStyle style, String title, URL fxmlLocation, ResourceBundle fxmlOptResources, ClassLoader optFxmlClassLoader)
	{
		this(owner, modality, style, title, JfxUtil.createFxmlLoader(fxmlLocation, null, StandardCharsets.UTF_8, fxmlOptResources), optFxmlClassLoader);
	}
	
	private double computeStageSizeUnit(double value, double pref, double cur)
	{
		if (value == Region.USE_COMPUTED_SIZE)
		{
			return cur;
		}
		
		if (value == Region.USE_PREF_SIZE)
		{
			value = pref;
		}
		
		if (value == Region.USE_COMPUTED_SIZE)
		{
			return cur;
		}
		
		return value;
	}
	
	private final void layoutMinMax()
	{
		double decoWidth = getWidth() - _root.getWidth();
		double decoHeight = getHeight() - _root.getHeight();
		_root.layout();
		// TODO: account for max value overflow
		setMinWidth(computeStageSizeUnit(_root.getMinWidth(), _root.getPrefWidth(), _root.getWidth()) + decoWidth);
		setMinHeight(computeStageSizeUnit(_root.getMinHeight(), _root.getPrefHeight(), _root.getHeight()) + decoHeight);
		setMaxWidth(computeStageSizeUnit(_root.getMaxWidth(), _root.getPrefWidth(), _root.getWidth()) + decoWidth);
		setMaxHeight(computeStageSizeUnit(_root.getMaxHeight(), _root.getPrefHeight(), _root.getHeight()) + decoHeight);
	}
	
	public final void layout()
	{
		layoutMinMax();
		setWidth(getMinWidth());
		setHeight(getMinHeight());
	}
	
	public final void layout(double width, double height)
	{
		layoutMinMax();
		setWidth(width);
		setHeight(height);
	}
	
	public final T getRoot()
	{
		return _root;
	}
	
	public final V getController()
	{
		return _controller;
	}
}
