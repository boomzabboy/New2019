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

import com.l2jserver.gameserver.model.skills.Skill;

/**
 * @author HorridoJoho
 */
public interface IWorldLiving extends IWorldObject
{
	/**
	 * Inflict damage to this world living.
	 * @param inflictor the world living inflicting the damage
	 * @param damage the damage
	 * @param skill the skill used to incflict the damage
	 */
	void inflictDamage(IWorldCharacter inflictor, int damage, Skill skill);
	
	/**
	 * Consume hp of this world living.
	 * @param amount the amount to consume
	 * @return true when hp is enough, false otherwise
	 */
	boolean consumeHp(int amount);
	
	/**
	 * Modify the hp of this world living.
	 * @param mod the modificator
	 */
	void modifyHp(int mod);
	
	/**
	 * @return whatever this world living is dead
	 */
	boolean isDead();
	
	/**
	 * @return the current hp of this world living which can range from 0 to<br>
	 *         max hp.
	 */
	int getHp();
	
	/**
	 * @return the max hp of this world living
	 */
	int getMaxHp();
}
