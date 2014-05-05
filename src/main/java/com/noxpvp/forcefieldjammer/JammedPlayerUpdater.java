package com.noxpvp.forcefieldjammer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.forcefieldjammer.packet.UpdatePacket;

public class JammedPlayerUpdater extends BukkitRunnable {
	
	private ForcefieldJammer plugin;
	
	public JammedPlayerUpdater(ForcefieldJammer instance) {
		
		this.plugin = instance;
	}

	public void run() {
		try {
			Future<Player[]> players = Bukkit.getScheduler().callSyncMethod(plugin, plugin.getPlayersToUpdateCallable);
			
			if (players.get() == null)
				return;
			
			for (Player p : players.get()) {
				if (p == null || !p.isOnline())
					continue;
				
				new UpdatePacket(p, ForcefieldJammer.START_ID).runTaskAsynchronously(plugin);
			}
		}
		catch (InterruptedException e) {
			
		} catch (ExecutionException e) {
			
		}
	}

}
