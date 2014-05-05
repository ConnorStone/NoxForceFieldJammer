package com.noxpvp.forcefieldjammer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.noxpvp.forcefieldjammer.packet.JamPacket;

public class ForcefieldJammer extends JavaPlugin {
	
	//perm nodes
	public static final String PERM_NODE = "forcefieldjammer";
	public static final String PERM_EXEMPT = PERM_NODE + ".exempt";
	
	public static final int START_ID = 234567891;
	public static WrappedDataWatcher invisPlayerMeta = null;
	
	private static ForcefieldJammer instance;
	
	private JammedPlayerUpdater updater;
	private ForcefieldListener listener;
	private Map<Player, Vector> jammed;
	public Callable<Player[]> getPlayersToUpdateCallable;
	
	public static ForcefieldJammer getInstance() {
		return instance;
	}
	
	@Override
	public void onDisable() {
		return;
	}
	
	@Override
	public void onEnable() {
		if (instance != null) {
			setEnabled(false);
			return;
		}
		
		instance = this;
		jammed = new HashMap<Player, Vector>();
		
		listener = new ForcefieldListener(instance);
		
		updater = new JammedPlayerUpdater(instance);
		updater.runTaskTimerAsynchronously(instance, 20, 20);
		
		getPlayersToUpdateCallable = new Callable<Player[]>() {
			public Player[] call() throws Exception {
				return getPlayersToUpdate();
			}
		};
		
		invisPlayerMeta = new WrappedDataWatcher();
		invisPlayerMeta.setObject(0, (byte) 0x20);
		invisPlayerMeta.setObject(6, (float) 20);
		invisPlayerMeta.setObject(12, (int) 0);
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
			getLogger().log(Level.WARNING, "You can probably ignore this...", e);
		}
		//TODO cool graphs
		
	}
	
	public void removeJam(Player player) {
		for (Player p : jammed.keySet()) {
			if (p != null && p.equals(player))
				jammed.remove(p);
		}
	}
	
	public void addJam(Player player) {
		jammed.put(player, player.getLocation().toVector());
		new JamPacket(player).start(20);
	}

	public Player[] getPlayersToUpdate() {
		Player[] players = new Player[jammed.size()];
		
		int i = 0;
		for (Player p : jammed.keySet()) {
			if (i > players.length)
				break;
			
			Vector playerLoc;
			
			if (p == null || !p.isOnline() || jammed.get(p).equals((playerLoc = p.getLocation().toVector()))) {
				continue;
			}
			
			if (playerLoc.distance(jammed.get(p)) < 2)
				continue;
			
			jammed.remove(p);
			jammed.put(p, playerLoc);
			players[i++] = p;
			
		}
		
		return players;
	}
}
