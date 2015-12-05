/*
 * Copyright (C) 2004-2015 L2J Server
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
package com.l2jserver.gameserver.model.actor;

import com.l2jserver.gameserver.ai.IWorldCharacterAi;

/**
 * @author HorridoJoho
 */
public interface IWorldCharacter extends IWorldSpawnable, IWorldLiving
{
	/**
	 * Make this world character move to the specified position.
	 * @param x world x position
	 * @param y world y position
	 * @param z world z position
	 */
	void move(int x, int y, int z);
	
	/**
	 * Consume mp of this world character.
	 * @param amount the amount to consume
	 * @return true when mp is enough, false otherwise
	 */
	boolean consumeMp(int amount);
	
	/**
	 * Modify the mp of this world character.
	 * @param mod the modificator
	 */
	void modifyMp(int mod);
	
	/**
	 * @return the current mp of this world character which can range from 0 to<br>
	 *         max mp.
	 */
	int getMp();
	
	/**
	 * @return the max mp of this world character
	 */
	int getMaxMp();
	
	/**
	 * @return the ai of this world character
	 */
	IWorldCharacterAi getAi();
}
