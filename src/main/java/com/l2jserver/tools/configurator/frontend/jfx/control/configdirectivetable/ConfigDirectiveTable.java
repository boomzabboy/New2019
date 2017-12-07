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
package com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells.OverwriteCell;
import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells.ResetCell;
import com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells.ValueCell;
import com.l2jserver.tools.configurator.model.ConfigDirective;
import com.l2jserver.tools.util.JfxUtil;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author HorridoJoho
 */
public class ConfigDirectiveTable extends TableView<ConfigDirective>
{
	private static final String _FXML_RES_PATH = "/resources/tools/configurator/frontend/jfx/control/configdirectivetable/ConfigDirectiveTable";
	private static final URL _FXML_URL = JfxUtil.getInternationalizedFxmlLocation(ConfigDirectiveTable.class, _FXML_RES_PATH);
	private static final String _RES_BUNDLE_NAME = "resources.tools.configurator.frontend.jfx.control.configdirectivetable.ConfigDirectiveTable";
	private static FXMLLoader _LOADER = null;
	
	static
	{
		initializeLoader();
	}
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private TableColumn<ConfigDirective, String> _nameColumn;
	@FXML
	private TableColumn<ConfigDirective, String> _valueColumn;
	@FXML
	private TableColumn<ConfigDirective, ConfigDirective> _valueCurrentColumn;
	@FXML
	private TableColumn<ConfigDirective, ConfigDirective> _valueDefaultColumn;
	@FXML
	private TableColumn<ConfigDirective, ConfigDirective> _overwriteColumn;
	@FXML
	private TableColumn<ConfigDirective, ConfigDirective> _resetColumn;
	
	@FXML
	void initialize()
	{
		_nameColumn.setCellValueFactory(new PropertyValueFactory<ConfigDirective, String>("name"));
		_valueCurrentColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
		_valueCurrentColumn.setCellFactory(col -> new ValueCell(false));
		_valueDefaultColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
		_valueDefaultColumn.setCellFactory(col -> new ValueCell(true));
		_overwriteColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
		_overwriteColumn.setCellFactory(col -> new OverwriteCell());
		_resetColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
		_resetColumn.setCellFactory(col -> new ResetCell(resources));
	}
	
	public static void initializeLoader()
	{
		_LOADER = JfxUtil.createFxmlLoader(_FXML_URL, null, StandardCharsets.UTF_8, ResourceBundle.getBundle(_RES_BUNDLE_NAME));
	}
	
	public static ConfigDirectiveTable create() throws IOException
	{
		ConfigDirectiveTable ctrl = new ConfigDirectiveTable();
		JfxUtil.loadFxml(_LOADER, null, ctrl, ctrl);
		return ctrl;
	}
	
	private ConfigDirectiveTable()
	{
	}
}
