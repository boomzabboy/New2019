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

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.l2jserver.tools.util.jfx.stringconverter.ExIntegerStringConverter;

import javafx.scene.control.TextFormatter;

/**
 * @author HorridoJoho
 */
public class IpvFourPartTextFormatter extends TextFormatter<Integer>
{
	public IpvFourPartTextFormatter()
	{
		super(new ExIntegerStringConverter(0, 255, 10), 0, new UnaryOperator<Change>()
		{
			private final Pattern r = Pattern.compile("^\\d{1,3}$");
			
			@Override
			public Change apply(Change t)
			{
				String newText = t.getControlNewText();
				if (newText.isEmpty())
				{
					// replace the whole range with 0
					t.setRange(0, t.getControlText().length());
					t.setText("0");
				}
				else if (!r.matcher(newText).matches())
				{
					return null;
				}
				
				return t;
			}
		});
	}
}
