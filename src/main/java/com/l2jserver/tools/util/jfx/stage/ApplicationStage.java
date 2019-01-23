package com.l2jserver.tools.util.jfx.stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import com.l2jserver.tools.util.JfxUtil;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public abstract class ApplicationStage<T extends Region, V> extends Stage
{
	private final V _controller;
	
	public ApplicationStage(Window owner, Modality modality, StageStyle style, String title, FXMLLoader loader, ClassLoader optFxmlClassLoader)
	{
		initOwner(owner);
		initModality(modality);
		initStyle(style);
		
		setTitle(title);
		
		try
		{
			JfxUtil.loadFxml(loader, optFxmlClassLoader, null, null);
			_controller = loader.getController();
			setScene(new Scene(loader.getRoot()));
		}
		catch (IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	public ApplicationStage(Window owner, Modality modality, StageStyle style, String title, T root, V controller)
	{
		initOwner(owner);
		initModality(modality);
		initStyle(style);
		
		setTitle(title);
		
		_controller = controller;
		setScene(new Scene(root));
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
		double decoWidth = getTotalDecorationWidth();
		double decoHeight = getTotalDecorationHeight();
		
		getRoot().layout();
		
		// TODO: account for max value overflow
		setMinWidth(computeStageSizeUnit(getRoot().getMinWidth(), getRoot().getPrefWidth(), getRoot().getWidth()) + decoWidth);
		setMinHeight(computeStageSizeUnit(getRoot().getMinHeight(), getRoot().getPrefHeight(), getRoot().getHeight()) + decoHeight);
		setMaxWidth(computeStageSizeUnit(getRoot().getMaxWidth(), getRoot().getPrefWidth(), getRoot().getWidth()) + decoWidth);
		setMaxHeight(computeStageSizeUnit(getRoot().getMaxHeight(), getRoot().getPrefHeight(), getRoot().getHeight()) + decoHeight);
	}
	
	public final void layout()
	{
		layoutMinMax();
		setWidth(getRoot().getWidth() + getTotalDecorationWidth());
		setHeight(getRoot().getHeight() + getTotalDecorationHeight());
	}
	
	public final void layout(double width, double height)
	{
		layoutMinMax();
		setWidth(width);
		setHeight(height);
	}
	
	public final void bindRootSizeToSize()
	{
		getRoot().minWidthProperty().unbind();
		getRoot().minHeightProperty().unbind();
		getRoot().prefWidthProperty().unbind();
		getRoot().prefHeightProperty().unbind();
		getRoot().maxWidthProperty().unbind();
		getRoot().maxHeightProperty().unbind();
		
		DoubleBinding minWidthBinding = minWidthProperty().subtract(getTotalDecorationWidth());
		DoubleBinding minHeightBinding = minHeightProperty().subtract(getTotalDecorationHeight());
		DoubleBinding widthBinding = widthProperty().subtract(getTotalDecorationWidth());
		DoubleBinding heightBinding = heightProperty().subtract(getTotalDecorationHeight());
		DoubleBinding maxWidthBinding = minWidthProperty().subtract(getTotalDecorationWidth());
		DoubleBinding maxHeightBinding = minHeightProperty().subtract(getTotalDecorationHeight());
		
		getRoot().minWidthProperty().bind(minWidthBinding);
		getRoot().minHeightProperty().bind(minHeightBinding);
		getRoot().prefWidthProperty().bind(widthBinding);
		getRoot().prefHeightProperty().bind(heightBinding);
		getRoot().maxWidthProperty().bind(maxWidthBinding);
		getRoot().maxHeightProperty().bind(maxHeightBinding);
	}
	
	private static final String BSTRS_LISTENER = "bindSizeToRootSizeListener";
	
	public final void bindSizeToRootSize()
	{
		DoubleBinding widthBinding = getRoot().widthProperty().add(getTotalDecorationWidth());
		DoubleBinding heightBinding = getRoot().heightProperty().add(getTotalDecorationHeight());
		InvalidationListener listener = (InvalidationListener) getProperties().get(BSTRS_LISTENER);
		
		if (listener != null)
		{
			getRoot().widthProperty().removeListener(listener);
			getRoot().heightProperty().removeListener(listener);
		}
		minWidthProperty().unbind();
		minHeightProperty().unbind();
		maxWidthProperty().unbind();
		maxHeightProperty().unbind();
		
		listener = observable ->
		{
			setWidth(widthBinding.get());
			setHeight(heightBinding.get());
		};
		getProperties().put(BSTRS_LISTENER, listener);
		
		minWidthProperty().bind(widthBinding);
		minHeightProperty().bind(heightBinding);
		maxWidthProperty().bind(widthBinding);
		maxHeightProperty().bind(heightBinding);
		getRoot().widthProperty().addListener(listener);
		getRoot().heightProperty().addListener(listener);
	}
	
	public final double getTotalDecorationWidth()
	{
		return getWidth() - getRoot().getWidth();
	}
	
	public final double getTotalDecorationHeight()
	{
		return getHeight() - getRoot().getHeight();
	}
	
	@SuppressWarnings("unchecked")
	public final T getRoot()
	{
		return (T) getScene().getRoot();
	}
	
	public final V getController()
	{
		return _controller;
	}
}
