package com.l2jserver.tools.util.jfx.stage.msgbox;

import java.util.ResourceBundle;

import com.l2jserver.tools.util.jfx.StageController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public final class MsgBoxController implements StageController
{
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private HBox hboxButtons;
	@FXML
	private ImageView ivImage;
	@FXML
	private Label lblText;
	
	private MsgBoxResult _result;
	
	@FXML
	void initialize()
	{
		_result = MsgBoxResult.CANCEL;
		ivImage.setFitWidth(Region.USE_COMPUTED_SIZE);
		ivImage.setFitHeight(Region.USE_COMPUTED_SIZE);
	}
	
	public void setImage(Image image)
	{
		ivImage.setImage(image);
	}
	
	public void setMessage(String message)
	{
		lblText.setText(message);
	}
	
	public void setButtons(Stage stage, MsgBoxButton... buttons)
	{
		for (MsgBoxButton button : buttons)
		{
			Button btn = new Button(button.getButtonText(resources));
			
			switch (button)
			{
				case OK:
				case YES:
					btn.addEventHandler(ActionEvent.ACTION, e ->
					{
						_result = MsgBoxResult.OK;
						stage.close();
					});
					break;
				case RETRY:
				case TRY_AGAIN:
					btn.addEventHandler(ActionEvent.ACTION, e ->
					{
						_result = MsgBoxResult.RETRY;
						stage.close();
					});
					break;
				case CONTINUE:
				case IGNORE:
					btn.addEventHandler(ActionEvent.ACTION, e ->
					{
						_result = MsgBoxResult.CONTINUE;
						stage.close();
					});
					break;
				case CANCEL:
				case NO:
				case ABORT:
					btn.addEventHandler(ActionEvent.ACTION, (ActionEvent event) ->
					{
						_result = MsgBoxResult.CANCEL;
						stage.close();
					});
					break;
			}
			
			btn.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
			btn.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
			btn.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
			hboxButtons.getChildren().add(btn);
		}
	}
	
	@Override
	public boolean onRequestClose()
	{
		return true;
	}
	
	public MsgBoxResult getResult()
	{
		return _result;
	}
}
