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
package com.l2jserver.tools.util.jfx.control.ipvfourcontrol;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

import com.l2jserver.tools.util.JfxUtil;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * @author HorridoJoho
 */
public final class IpvFourControl extends HBox
{
	private static final String _FXML_RES_PATH = "/resources/tools/util/jfx/control/ipvfourcontrol/IpvFourControl";
	private static URL _FXML_URL = null;
	private static FXMLLoader _LOADER = null;
	
	public static String DEFAULT_IPVFOUR_ADDRESS_STRING = "0.0.0.0";
	
	static
	{
		initializeLoader();
	}
	
	public static void initializeLoader()
	{
		_FXML_URL = JfxUtil.getInternationalizedFxmlLocation(IpvFourControl.class, _FXML_RES_PATH);
		_LOADER = JfxUtil.createFxmlLoader(_FXML_URL, null, StandardCharsets.UTF_8, null);
	}
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private TextField _ipvFourAddressFirst;
	@FXML
	private TextField _ipvFourAddressSecond;
	@FXML
	private TextField _ipvFourAddressThird;
	@FXML
	private TextField _ipvFourAddressFourth;
	
	@FXML
	void initialize()
	{
		_ipvFourAddressFirst.setTextFormatter(new IpvFourPartTextFormatter());
		_ipvFourAddressSecond.setTextFormatter(new IpvFourPartTextFormatter());
		_ipvFourAddressThird.setTextFormatter(new IpvFourPartTextFormatter());
		_ipvFourAddressFourth.setTextFormatter(new IpvFourPartTextFormatter());
		
		ChangeListener<String> textListener = (o, ov, nv) -> maybeUpdateAddress();
		addTextListeners(textListener);
		
		ChangeListener<Boolean> focusedListener = (o, ov, nv) -> maybeUpdateAddress();
		_ipvFourAddressFirst.focusedProperty().addListener(focusedListener);
		_ipvFourAddressSecond.focusedProperty().addListener(focusedListener);
		_ipvFourAddressThird.focusedProperty().addListener(focusedListener);
		_ipvFourAddressFourth.focusedProperty().addListener(focusedListener);
		
		addressProperty().addListener((o, ov, nv) ->
		{
			// prevent the change of each text field to trigger another addressProperty() update
			removeTextListeners(textListener);
			try
			{
				byte[] address = nv.getAddress();
				_ipvFourAddressFirst.textProperty().set(String.valueOf(address[0] & 0xFF));
				_ipvFourAddressSecond.textProperty().set(String.valueOf(address[1] & 0xFF));
				_ipvFourAddressThird.textProperty().set(String.valueOf(address[2] & 0xFF));
				_ipvFourAddressFourth.textProperty().set(String.valueOf(address[3] & 0xFF));
			}
			finally
			{
				addTextListeners(textListener);
			}
		});
	}
	
	private final SimpleObjectProperty<Inet4Address> _address = new SimpleObjectProperty<>((Inet4Address) InetAddress.getByName(DEFAULT_IPVFOUR_ADDRESS_STRING));
	
	public IpvFourControl() throws IOException
	{
		JfxUtil.loadFxml(_LOADER, null, this, this);
	}
	
	private void addTextListeners(ChangeListener<String> listener)
	{
		_ipvFourAddressFirst.textProperty().addListener(listener);
		_ipvFourAddressSecond.textProperty().addListener(listener);
		_ipvFourAddressThird.textProperty().addListener(listener);
		_ipvFourAddressFourth.textProperty().addListener(listener);
	}
	
	private void removeTextListeners(ChangeListener<String> listener)
	{
		_ipvFourAddressFirst.textProperty().removeListener(listener);
		_ipvFourAddressSecond.textProperty().removeListener(listener);
		_ipvFourAddressThird.textProperty().removeListener(listener);
		_ipvFourAddressFourth.textProperty().removeListener(listener);
	}
	
	private void maybeUpdateAddress()
	{
		// only update the address when the editing area is left
		if (isAnyTextFieldFocused())
		{
			return;
		}
		
		try
		{
			//@formatter:off
			setAddress((Inet4Address) InetAddress.getByName(
				_ipvFourAddressFirst.getText() + "." +
				_ipvFourAddressSecond.getText() + "." +
				_ipvFourAddressThird.getText() + "." +
				_ipvFourAddressFourth.getText()
			));
			//@formatter:on
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setAddress(InetAddress address)
	{
		if (!(address instanceof Inet4Address))
		{
			throw new IllegalArgumentException();
		}
		setAddress((Inet4Address) address);
	}
	
	public void setAddress(Inet4Address address)
	{
		Objects.requireNonNull(address);
		addressProperty().set(address);
	}
	
	public SimpleObjectProperty<Inet4Address> addressProperty()
	{
		return _address;
	}
	
	public Inet4Address getAddress()
	{
		return addressProperty().get();
	}
	
	public boolean isAnyTextFieldFocused()
	{
		return _ipvFourAddressFirst.isFocused() || _ipvFourAddressSecond.isFocused() || _ipvFourAddressThird.isFocused() || _ipvFourAddressFourth.isFocused();
	}
	
	@Override
	public String toString()
	{
		return addressProperty().get().getHostAddress();
	}
}
