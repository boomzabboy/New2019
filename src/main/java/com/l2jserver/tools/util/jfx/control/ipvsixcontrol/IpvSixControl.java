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
package com.l2jserver.tools.util.jfx.control.ipvsixcontrol;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
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
public final class IpvSixControl extends HBox
{
	private static final String _FXML_RES_PATH = "/resources/tools/util/jfx/control/ipvsixcontrol/IpvSixControl";
	private static final String _RES_BUNDLE_NAME = "resources.tools.util.jfx.control.ipvsixcontrol.IpvSixControl";
	private static URL _FXML_URL = null;
	private static FXMLLoader _LOADER = null;
	
	public static String DEFAULT_IPVSIX_ADDRESS_STRING = "0:0:0:0:0:0:0:0";
	
	static
	{
		initializeLoader();
	}
	
	public static void initializeLoader()
	{
		_FXML_URL = JfxUtil.getInternationalizedFxmlLocation(IpvSixControl.class, _FXML_RES_PATH);
		_LOADER = JfxUtil.createFxmlLoader(_FXML_URL, null, StandardCharsets.UTF_8, ResourceBundle.getBundle(_RES_BUNDLE_NAME));
	}
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private TextField _ipvSixAddressFirst;
	@FXML
	private TextField _ipvSixAddressSecond;
	@FXML
	private TextField _ipvSixAddressThird;
	@FXML
	private TextField _ipvSixAddressFourth;
	@FXML
	private TextField _ipvSixAddressFifth;
	@FXML
	private TextField _ipvSixAddressSixth;
	@FXML
	private TextField _ipvSixAddressSeventh;
	@FXML
	private TextField _ipvSixAddressEigth;
	@FXML
	private TextField _ipvSixAddressZone;
	
	@FXML
	void initialize()
	{
		_ipvSixAddressFirst.setTextFormatter(new IpvSixPartTextFormatter());
		_ipvSixAddressSecond.setTextFormatter(new IpvSixPartTextFormatter());
		_ipvSixAddressThird.setTextFormatter(new IpvSixPartTextFormatter());
		_ipvSixAddressFourth.setTextFormatter(new IpvSixPartTextFormatter());
		_ipvSixAddressFifth.setTextFormatter(new IpvSixPartTextFormatter());
		_ipvSixAddressSixth.setTextFormatter(new IpvSixPartTextFormatter());
		_ipvSixAddressSeventh.setTextFormatter(new IpvSixPartTextFormatter());
		_ipvSixAddressEigth.setTextFormatter(new IpvSixPartTextFormatter());
		// add a zone text formatter which allows only alphanumeric input
		
		ChangeListener<String> textListener = (o, ov, nv) -> maybeUpdateAddress();
		addTextListeners(textListener);
		
		ChangeListener<Boolean> focusedListener = (o, ov, nv) -> maybeUpdateAddress();
		_ipvSixAddressFirst.focusedProperty().addListener(focusedListener);
		_ipvSixAddressSecond.focusedProperty().addListener(focusedListener);
		_ipvSixAddressThird.focusedProperty().addListener(focusedListener);
		_ipvSixAddressFourth.focusedProperty().addListener(focusedListener);
		_ipvSixAddressFifth.focusedProperty().addListener(focusedListener);
		_ipvSixAddressSixth.focusedProperty().addListener(focusedListener);
		_ipvSixAddressSeventh.focusedProperty().addListener(focusedListener);
		_ipvSixAddressEigth.focusedProperty().addListener(focusedListener);
		_ipvSixAddressZone.focusedProperty().addListener(focusedListener);
		
		addressProperty().addListener((o, ov, nv) ->
		{
			// prevent the change of each text field to trigger another addressProperty() update
			removeTextListeners(textListener);
			try
			{
				byte[] address = nv.getAddress();
				_ipvSixAddressFirst.textProperty().set(String.valueOf(address[0] & 0xFF) + String.valueOf(address[1] & 0xFF));
				_ipvSixAddressSecond.textProperty().set(String.valueOf(address[2] & 0xFF) + String.valueOf(address[3] & 0xFF));
				_ipvSixAddressThird.textProperty().set(String.valueOf(address[4] & 0xFF) + String.valueOf(address[5] & 0xFF));
				_ipvSixAddressFourth.textProperty().set(String.valueOf(address[6] & 0xFF) + String.valueOf(address[7] & 0xFF));
				_ipvSixAddressFifth.textProperty().set(String.valueOf(address[8] & 0xFF) + String.valueOf(address[9] & 0xFF));
				_ipvSixAddressSixth.textProperty().set(String.valueOf(address[10] & 0xFF) + String.valueOf(address[11] & 0xFF));
				_ipvSixAddressSeventh.textProperty().set(String.valueOf(address[12] & 0xFF) + String.valueOf(address[13] & 0xFF));
				_ipvSixAddressEigth.textProperty().set(String.valueOf(address[14] & 0xFF) + String.valueOf(address[15] & 0xFF));
				NetworkInterface nif = nv.getScopedInterface();
				if (nif != null)
				{
					_ipvSixAddressZone.textProperty().set(nif.getName());
				}
				else
				{
					int scopeId = nv.getScopeId();
					if (scopeId == 0)
					{
						_ipvSixAddressZone.textProperty().set("");
					}
					else
					{
						_ipvSixAddressZone.textProperty().set(String.valueOf(scopeId));
					}
				}
			}
			finally
			{
				addTextListeners(textListener);
			}
		});
	}
	
	private final SimpleObjectProperty<Inet6Address> _address = new SimpleObjectProperty<>((Inet6Address) InetAddress.getByName(DEFAULT_IPVSIX_ADDRESS_STRING));
	
	public IpvSixControl() throws IOException
	{
		JfxUtil.loadFxml(_LOADER, null, this, this);
	}
	
	private void addTextListeners(ChangeListener<String> listener)
	{
		_ipvSixAddressFirst.textProperty().addListener(listener);
		_ipvSixAddressSecond.textProperty().addListener(listener);
		_ipvSixAddressThird.textProperty().addListener(listener);
		_ipvSixAddressFourth.textProperty().addListener(listener);
		_ipvSixAddressFifth.textProperty().addListener(listener);
		_ipvSixAddressSixth.textProperty().addListener(listener);
		_ipvSixAddressSeventh.textProperty().addListener(listener);
		_ipvSixAddressEigth.textProperty().addListener(listener);
		_ipvSixAddressZone.textProperty().addListener(listener);
	}
	
	private void removeTextListeners(ChangeListener<String> listener)
	{
		_ipvSixAddressFirst.textProperty().removeListener(listener);
		_ipvSixAddressSecond.textProperty().removeListener(listener);
		_ipvSixAddressThird.textProperty().removeListener(listener);
		_ipvSixAddressFourth.textProperty().removeListener(listener);
		_ipvSixAddressFifth.textProperty().removeListener(listener);
		_ipvSixAddressSixth.textProperty().removeListener(listener);
		_ipvSixAddressSeventh.textProperty().removeListener(listener);
		_ipvSixAddressEigth.textProperty().removeListener(listener);
		_ipvSixAddressZone.textProperty().removeListener(listener);
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
			setAddress((Inet6Address) InetAddress.getByName(
				_ipvSixAddressFirst.getText() + ":" +
				_ipvSixAddressSecond.getText() + ":" +
				_ipvSixAddressThird.getText() + ":" +
				_ipvSixAddressFourth.getText() + ":" +
				_ipvSixAddressFifth.getText() + ":" +
				_ipvSixAddressSixth.getText() + ":" +
				_ipvSixAddressSeventh.getText() + ":" +
				_ipvSixAddressEigth.getText() +
				(_ipvSixAddressZone.getText().isEmpty() ? "" : ("%" + _ipvSixAddressZone.getText()))
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
		if (!(address instanceof Inet6Address))
		{
			throw new IllegalArgumentException();
		}
		setAddress((Inet6Address) address);
	}
	
	public void setAddress(Inet6Address address)
	{
		Objects.requireNonNull(address);
		addressProperty().set(address);
	}
	
	public SimpleObjectProperty<Inet6Address> addressProperty()
	{
		return _address;
	}
	
	public Inet6Address getAddress()
	{
		return addressProperty().get();
	}
	
	public boolean isAnyTextFieldFocused()
	{
		return _ipvSixAddressFirst.isFocused() || _ipvSixAddressSecond.isFocused() || _ipvSixAddressThird.isFocused() || _ipvSixAddressFourth.isFocused() || _ipvSixAddressFifth.isFocused() || _ipvSixAddressSixth.isFocused() || _ipvSixAddressSeventh.isFocused() || _ipvSixAddressEigth.isFocused()
			|| _ipvSixAddressZone.isFocused();
	}
	
	@Override
	public String toString()
	{
		return addressProperty().get().getHostAddress();
	}
}
