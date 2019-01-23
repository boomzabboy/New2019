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

import com.l2jserver.gameserver.ai.IPlayerAi;
import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author HorridoJoho
 */
public interface IPlayer extends IWorldCharacter
{
	/**
	 * Sends a packet to this player.
	 * @param packet the packet to be send
	 */
	void sendPacket(L2GameServerPacket packet);
	
	/**
	 * Sends dirty states of this player to this player and marks them as non<br>
	 * dirty.
	 */
	void sendUpdates();
	
	/**
	 * Consume cp of this player.
	 * @param amount the amount to consume
	 * @return true: player had enough cp and it got reduced<br>
	 *         false: not enough cp
	 */
	boolean consumeCp(double amount);
	
	/**
	 * Modify the cp of this player.
	 * @param mod the modificator
	 */
	void modifyCp(double mod);
	
	/**
	 * @return the current cp of this player which can range from 0 to max cp.
	 */
	double getCp();
	
	/**
	 * @return the max cp of this player
	 */
	double getMaxCp();
	
	/**
	 * @return the ai of this player
	 */
	@Override
	IPlayerAi getAi();
}
