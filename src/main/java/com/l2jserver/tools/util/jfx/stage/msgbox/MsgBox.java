package com.l2jserver.tools.util.jfx.stage.msgbox;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import com.l2jserver.tools.util.JfxUtil;
import com.l2jserver.tools.util.jfx.stage.ApplicationStage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public final class MsgBox extends ApplicationStage<HBox, MsgBoxController>
{
	private static final String _FXML_RES_PATH = "/resources/tools/util/jfx/stage/msgbox/MsgBox";
	private static final String _RES_BUNDLE_NAME = "resources.tools.util.jfx.stage.msgbox.MsgBox";
	private static URL _FXML_URL = null;
	private static FXMLLoader _LOADER = null;
	
	static
	{
		initializeLoader();
	}
	
	public static void initializeLoader()
	{
		_FXML_URL = JfxUtil.getInternationalizedFxmlLocation(MsgBox.class, _FXML_RES_PATH);
		_LOADER = JfxUtil.createFxmlLoader(_FXML_URL, null, StandardCharsets.UTF_8, ResourceBundle.getBundle(_RES_BUNDLE_NAME));
	}
	
	public MsgBox(Window owner, Modality modality, StageStyle style, String title)
	{
		super(owner, modality, style, title, _LOADER, null);
		setResizable(false);
	}
	
	public void setIcon(Image icon)
	{
		getController().setIcon(icon);
	}
	
	public void setHeadline(String headline)
	{
		getController().setHeadline(headline);
	}
	
	public void setDescription(String description)
	{
		getController().setDescription(description);
	}
	
	public void setButtons(MsgBoxButton... mbButtons)
	{
		getController().setButtons(this, mbButtons);
	}
	
	public void setDetails(String details)
	{
		getController().setDetails(details);
	}
	
	public void setButtonHandler(MsgBoxButton button, EventHandler<ActionEvent> handler)
	{
		getController().setButtonHandler(button, handler);
	}
	
	public void setCloseHandler(EventHandler<WindowEvent> handler)
	{
		setOnCloseRequest(handler);
	}
	
	public MsgBoxButton getButton()
	{
		return getController().getButton();
	}
	
	public MsgBoxResult getResult()
	{
		return getController().getResult();
	}
}
