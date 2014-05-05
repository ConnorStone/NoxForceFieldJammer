package com.noxpvp.forcefieldjammer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;

public class ForcefieldListener extends PacketAdapter implements Listener {
	
	private ForcefieldJammer plugin;
	
	public ForcefieldListener(ForcefieldJammer plugin) {
		super(plugin, PacketType.Play.Server.SPAWN_ENTITY);
		
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onLogin(PlayerLoginEvent event) {
		if (event.getPlayer() == null || event.getPlayer().hasPermission(ForcefieldJammer.PERM_EXEMPT))
			return;
		
		this.plugin.addJam(event.getPlayer());
		
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onLeave(PlayerQuitEvent event) {
		if (event.getPlayer() != null)
			this.plugin.removeJam(event.getPlayer());
		
	}
}
