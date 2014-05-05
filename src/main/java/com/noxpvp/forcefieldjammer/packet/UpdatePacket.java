package com.noxpvp.forcefieldjammer.packet;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.forcefieldjammer.ForcefieldJammer;
import com.noxpvp.forcefieldjammer.JammingUtils;

public class UpdatePacket extends BukkitRunnable {
	
	private int startId;
	private Player p;
	
	public UpdatePacket(Player p, int startingEntityId) {
		this.startId = startingEntityId;
		this.p = p;
	}
	
	public void start(int delayTicks) {
		runTaskLaterAsynchronously(ForcefieldJammer.getInstance(), delayTicks);
	}
	
	public void run() {
		JammingUtils.updateInvisNet(p, startId);
		
	}
}
