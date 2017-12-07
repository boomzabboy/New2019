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
package com.l2jserver.tools.configurator.frontend.jfx.control.configdirectivetable.cells;

import com.l2jserver.tools.configurator.model.ConfigDirective;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

/**
 * @author HorridoJoho
 */
public final class OverwriteCell extends TableCell<ConfigDirective, ConfigDirective>
{
	private final CheckBox _box = new CheckBox();
	private final ChangeListener<String> _valueChangeListener = (observable, oldValue, newValue) ->
	{
		if (!getItem().isDefaultProperty().get())
		{
			_box.selectedProperty().set(true);
		}
	};
	
	public OverwriteCell()
	{
		setAlignment(Pos.CENTER);
	}
	
	@Override
	public void updateItem(ConfigDirective item, boolean empty)
	{
		_box.disableProperty().unbind();
		_box.selectedProperty().unbind();
		
		ConfigDirective oldItem = getItem();
		if (oldItem != null)
		{
			oldItem.overwriteProperty().unbind();
			oldItem.valueProperty().removeListener(_valueChangeListener);
		}
		
		super.updateItem(item, empty);
		
		if (empty || (item == null))
		{
			setText(null);
			setGraphic(null);
		}
		else
		{
			_box.disableProperty().bind(item.isDefaultProperty().not());
			_box.selectedProperty().set(item.overwriteProperty().get());
			
			item.overwriteProperty().bind(_box.selectedProperty());
			item.valueProperty().addListener(_valueChangeListener);
			
			setGraphic(_box);
		}
	}
}
