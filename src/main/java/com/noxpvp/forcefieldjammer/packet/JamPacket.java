package com.noxpvp.forcefieldjammer.packet;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType.Sender;
import com.noxpvp.forcefieldjammer.ForcefieldJammer;
import com.noxpvp.forcefieldjammer.JammingUtils;

public class JamPacket extends BukkitRunnable {
	
	private final Player p;
	
	public JamPacket(Player p) {
		this.p = p;
	}
	
	public void start(int delayTicks) {
		runTaskLaterAsynchronously(ForcefieldJammer.getInstance(), delayTicks);
	}
	
	public void run() {
		
		List<String> names = JammingUtils.getNamesForPlayer(p);
		
		if (names == null || names.size() < 1)
			return;
		
		JammingUtils.sendInvisNet(p, p.getLocation(), ForcefieldJammer.START_ID, names);
	}
}
