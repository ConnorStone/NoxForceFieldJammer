package com.noxpvp.forcefieldjammer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.noxpvp.forcefieldjammer.packet.WrapperPlayServerEntityTeleport;
import com.noxpvp.forcefieldjammer.packet.WrapperPlayServerNamedEntitySpawn;

public class JammingUtils {

	public static void sendInvisPlayer(Player p, Location loc, int entityId, String name) {
		WrapperPlayServerNamedEntitySpawn packet = new WrapperPlayServerNamedEntitySpawn();
		
		packet.setEntityID(entityId);
		packet.setPlayerName(name);
		packet.setPlayerUUID(new UUID(10, 0).toString());
		packet.setPosition(loc.toVector());
		packet.setMetadata(ForcefieldJammer.invisPlayerMeta);
		
		packet.sendPacket(p);
	}
	
	public static void updateEntityLoc(Player receiver, Location to, int entityId) {
		WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();
		
		packet.setEntityID(entityId);
		packet.setX(to.getX());
		packet.setY(to.getY());
		packet.setZ(to.getZ());
		
		packet.sendPacket(receiver);
	}
	
	public static void sendInvisNet(Player p, Location pLoc, int startId, List<String> names) {
		double y = pLoc.getY() + 3;
		
		Iterator<String> nextName = names.iterator();
		int id = ForcefieldJammer.START_ID;
		
		for (double x = (int) (pLoc.getX() - 3); x < (pLoc.getX() + 3); x++)
			for (double z = (int) (pLoc.getZ() - 3); z < (pLoc.getZ() + 3); z++) {
				Location cur = new Location(pLoc.getWorld(), x, y, z);
				
				sendInvisPlayer(p, cur, ++id, nextName.hasNext()? nextName.next() : (nextName = names.iterator()).next());
			}
	}
	
	public static void updateInvisNet(Player p, int startId) {
		Location pLoc = p.getLocation();
		double y = pLoc.getY() + 3;
		
		int id = ForcefieldJammer.START_ID;
		
		for (double x = (int) (pLoc.getX() - 3); x < (pLoc.getX() + 3); x++)
			for (double z = (int) (pLoc.getZ() - 3); z < (pLoc.getZ() + 3); z++) {
				Location cur = new Location(pLoc.getWorld(), x, y, z);
				
				updateEntityLoc(p, cur, ++id);
			}
	}
	
	public static List<String> getNamesForPlayer(Player p) {
		Player[] players = Bukkit.getOnlinePlayers();
		List<String> names = new ArrayList<String>();
		
		for (int i = 0; i < players.length; i++){
			if (!p.canSee(players[i]))
				continue;	
			
			names.add(players[i].getName());
		}
		
		if (!names.isEmpty() || names.size() > 0) {
			names.add("FlycoderIsGay");
			return names;
		}
		
		return null;
	}
}
