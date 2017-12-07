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
package com.l2jserver.tools.util.jfx.control.inetaddresscontrol;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

import com.l2jserver.tools.util.JfxUtil;
import com.l2jserver.tools.util.jfx.control.ipvfourcontrol.IpvFourControl;
import com.l2jserver.tools.util.jfx.control.ipvsixcontrol.IpvSixControl;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * @author HorridoJoho
 */
public final class InetAddressControl extends HBox
{
	private static final String _FXML_RES_PATH = "/resources/tools/util/jfx/control/inetaddresscontrol/InetAddressControl";
	private static final String _RES_BUNDLE_NAME = "resources.tools.util.jfx.control.inetaddresscontrol.InetAddressControl";
	private static URL _FXML_URL = null;
	private static FXMLLoader _LOADER = null;
	
	static
	{
		initializeLoader();
	}
	
	public static void initializeLoader()
	{
		_FXML_URL = JfxUtil.getInternationalizedFxmlLocation(InetAddressControl.class, _FXML_RES_PATH);
		_LOADER = JfxUtil.createFxmlLoader(_FXML_URL, null, StandardCharsets.UTF_8, ResourceBundle.getBundle(_RES_BUNDLE_NAME));
	}
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private IpvFourControl _ipvFourControl;
	@FXML
	private IpvSixControl _ipvSixControl;
	@FXML
	private TextField _hostnameControl;
	
	@FXML
	private HBox _inTypeChoiceBox;
	@FXML
	private ToggleGroup _inTypeChoiceGroup;
	@FXML
	private RadioButton _inTypeChoiceFour;
	@FXML
	private RadioButton _inTypeChoiceHostname;
	@FXML
	private RadioButton _inTypeChoiceSix;
	
	@FXML
	void initialize()
	{
		_ipvFourControl.visibleProperty().addListener((o, ov, nv) -> _ipvFourControl.setPrefHeight(nv ? Region.USE_COMPUTED_SIZE : 0));
		_ipvSixControl.visibleProperty().addListener((o, ov, nv) -> _ipvSixControl.setPrefHeight(nv ? Region.USE_COMPUTED_SIZE : 0));
		_hostnameControl.visibleProperty().addListener((o, ov, nv) -> _hostnameControl.setPrefHeight(nv ? Region.USE_COMPUTED_SIZE : 0));
		
		_inTypeChoiceFour.visibleProperty().addListener((o, ov, nv) -> _inTypeChoiceFour.setPrefWidth(nv ? Region.USE_COMPUTED_SIZE : 0));
		_inTypeChoiceSix.visibleProperty().addListener((o, ov, nv) -> _inTypeChoiceSix.setPrefWidth(nv ? Region.USE_COMPUTED_SIZE : 0));
		_inTypeChoiceHostname.visibleProperty().addListener((o, ov, nv) -> _inTypeChoiceHostname.setPrefWidth(nv ? Region.USE_COMPUTED_SIZE : 0));
		
		_ipvFourControl.visibleProperty().bind(_inTypeChoiceFour.selectedProperty());
		_ipvSixControl.visibleProperty().bind(_inTypeChoiceSix.selectedProperty());
		_hostnameControl.visibleProperty().bind(_inTypeChoiceHostname.selectedProperty());
		
		// TODO: Remove the choice box when only one mode is enabled
		// _inTypeChoiceBox.visibleProperty().bind(_inTypeChoiceFour.disabledProperty().not().and(_inTypeChoiceSix.disabledProperty().not()).and(_inTypeChoiceHostname.disabledProperty().not()));
		
		hostnameProperty().bindBidirectional(_hostnameControl.textProperty());
		
		_inTypeChoiceFour.selectedProperty().addListener((o, ov, nv) ->
		{
			if (nv)
			{
				maybeUpdateStringValue(_inTypeChoiceFour, getIpvFourAddress().getHostAddress());
			}
		});
		_inTypeChoiceSix.selectedProperty().addListener((o, ov, nv) ->
		{
			if (nv)
			{
				maybeUpdateStringValue(_inTypeChoiceSix, getIpvSixAddress().getHostAddress());
			}
		});
		_inTypeChoiceHostname.selectedProperty().addListener((o, ov, nv) ->
		{
			if (nv)
			{
				maybeUpdateStringValue(_inTypeChoiceHostname, getHostname());
			}
		});
		
		_hostnameControl.focusedProperty().addListener((o, ov, nv) -> maybeUpdateStringValue(_inTypeChoiceHostname, getHostname()));
		
		ipvFourAddressProperty().addListener((o, ov, nv) -> maybeUpdateStringValue(_inTypeChoiceFour, nv.getHostAddress()));
		ipvSixAddressProperty().addListener((o, ov, nv) -> maybeUpdateStringValue(_inTypeChoiceSix, nv.getHostAddress()));
		hostnameProperty().addListener((o, ov, nv) -> maybeUpdateStringValue(_inTypeChoiceHostname, nv));
		
		stringValueProperty().addListener((o, oldValue, newValue) ->
		{
			if (!newValue.isEmpty() && ((newValue.charAt(0) == '[') || (newValue.charAt(0) == ':') || (Character.digit(newValue.charAt(0), 16) != -1)))
			{
				// ip address
				try
				{
					InetAddress address = InetAddress.getByName(newValue);
					if (address instanceof Inet4Address)
					{
						setIpvFourAddress((Inet4Address) address);
					}
					else if (address instanceof Inet6Address)
					{
						setIpvSixAddress((Inet6Address) address);
					}
				}
				catch (Exception e)
				{
					setHostname(newValue);
				}
			}
			else
			{
				// host name, we need this to not resolve hostnames with the name service
				setHostname(newValue);
			}
		});
	}
	
	private final SimpleStringProperty _hostname = new SimpleStringProperty("");
	private final SimpleStringProperty _stringValue = new SimpleStringProperty(IpvFourControl.DEFAULT_IPVFOUR_ADDRESS_STRING);
	
	public InetAddressControl(boolean ipvFour, boolean ipvSix, boolean hostname) throws IOException
	{
		JfxUtil.loadFxml(_LOADER, null, this, this);
		setModes(ipvFour, ipvSix, hostname);
	}
	
	private void maybeUpdateStringValue(RadioButton choice, String value)
	{
		choice.setSelected(true);
		
		if (_ipvFourControl.isAnyTextFieldFocused() || _ipvSixControl.isAnyTextFieldFocused() || _hostnameControl.isFocused())
		{
			return;
		}
		
		setStringValue(value);
	}
	
	public void setModes(boolean ipvFour, boolean ipvSix, boolean hostname)
	{
		_inTypeChoiceFour.setVisible(ipvFour);
		_inTypeChoiceSix.setVisible(ipvSix);
		_inTypeChoiceHostname.setVisible(hostname);
	}
	
	public void setIpvFourAddress(InetAddress address)
	{
		_ipvFourControl.setAddress(address);
	}
	
	public void setIpvFourAddress(Inet4Address address)
	{
		_ipvFourControl.setAddress(address);
	}
	
	public void setIpvSixAddress(InetAddress address)
	{
		_ipvSixControl.setAddress(address);
	}
	
	public void setIpvSixAddress(Inet6Address address)
	{
		_ipvSixControl.setAddress(address);
	}
	
	public void setHostname(String host)
	{
		Objects.requireNonNull(host);
		hostnameProperty().set(host);
	}
	
	public void setStringValue(String host)
	{
		stringValueProperty().set(host);
	}
	
	private SimpleObjectProperty<Inet4Address> ipvFourAddressProperty()
	{
		return _ipvFourControl.addressProperty();
	}
	
	private SimpleObjectProperty<Inet6Address> ipvSixAddressProperty()
	{
		return _ipvSixControl.addressProperty();
	}
	
	private SimpleStringProperty hostnameProperty()
	{
		return _hostname;
	}
	
	public SimpleStringProperty stringValueProperty()
	{
		return _stringValue;
	}
	
	public Inet4Address getIpvFourAddress()
	{
		return ipvFourAddressProperty().get();
	}
	
	public Inet6Address getIpvSixAddress()
	{
		return ipvSixAddressProperty().get();
	}
	
	public String getHostname()
	{
		return hostnameProperty().get();
	}
	
	public String getStringValue()
	{
		return stringValueProperty().get();
	}
}
