package com.l2jserver.tools.util.jfx.stage.msgbox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class MsgBoxController
{
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	
	@FXML
	private ImageView ivIcon;
	@FXML
	private VBox vbContentArea;
	@FXML
	private Label lbHeadline;
	@FXML
	private Label lbDescription;
	@FXML
	private HBox hbButtons;
	@FXML
	private TextArea taDetails;
	
	@FXML
	void initialize()
	{
		_result = MsgBoxResult.CANCEL;
		ivIcon.setFitWidth(Region.USE_COMPUTED_SIZE);
		ivIcon.setFitHeight(Region.USE_COMPUTED_SIZE);
		updateDetailBox();
		taDetails.textProperty().addListener((o) -> updateDetailBox());
	}
	
	private final Map<MsgBoxButton, EventHandler<ActionEvent>> _buttonHandlers = new HashMap<>();
	private MsgBoxButton _button;
	private MsgBoxResult _result;
	
	private void updateDetailBox()
	{
		if (taDetails.getText().isEmpty())
		{
			vbContentArea.getChildren().remove(taDetails);
		}
		else if (!vbContentArea.getChildren().contains(taDetails))
		{
			vbContentArea.getChildren().add(taDetails);
		}
	}
	
	public void setIcon(Image icon)
	{
		ivIcon.setImage(icon);
	}
	
	public void setHeadline(String headline)
	{
		lbHeadline.setText(headline);
	}
	
	public void setDescription(String description)
	{
		lbDescription.setText(description);
	}
	
	public void setButtons(Stage stage, MsgBoxButton... mbButtons)
	{
		for (MsgBoxButton mbButton : mbButtons)
		{
			Button button = new Button(mbButton.getButtonText(resources));
			button.setOnAction(e ->
			{
				try
				{
					EventHandler<ActionEvent> handler = _buttonHandlers.get(mbButton);
					if (handler != null)
					{
						handler.handle(e);
					}
				}
				finally
				{
					_button = mbButton;
					_result = mbButton.getResult();
					stage.close();
				}
			});
			
			hbButtons.getChildren().add(button);
		}
	}
	
	public void setDetails(String details)
	{
		taDetails.setText(details);
	}
	
	public void setButtonHandler(MsgBoxButton button, EventHandler<ActionEvent> handler)
	{
		Objects.requireNonNull(button);
		_buttonHandlers.put(button, handler);
	}
	
	public MsgBoxButton getButton()
	{
		return _button;
	}
	
	public MsgBoxResult getResult()
	{
		return _result;
	}
}
