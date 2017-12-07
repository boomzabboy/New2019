package com.l2jserver.tools.util.jfx.stage.msgbox;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import com.l2jserver.tools.util.JfxUtil;
import com.l2jserver.tools.util.jfx.stage.ApplicationStage;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class MsgBox extends ApplicationStage<GridPane, MsgBoxController>
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
	
	public MsgBox(Window owner, Modality modality, Image image, String title, String message, MsgBoxButton... buttons)
	{
		super(owner, modality, StageStyle.DECORATED, title, _LOADER, null);
		getController().setImage(image);
		getController().setMessage(message);
		getController().setButtons(this, buttons);
	}
	
	public MsgBoxResult getResult()
	{
		return getController().getResult();
	}
}
