package com.github.bram3.onlyplacewaterwg;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class EventListener implements Listener {
    private final OnlyPlaceWaterWG plugin;
    private final Config config;

    public EventListener(OnlyPlaceWaterWG plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.WATER_BUCKET) return;
        Location location = event.getPlayer().getLocation();

        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location wrappedLocation = BukkitAdapter.adapt(location);
        ApplicableRegionSet set = query.getApplicableRegions(wrappedLocation);

        System.out.println("1");
        if (!(set.testState(null, plugin.WATER_FLAG))) return;
        System.out.println("2");

        event.setCancelled(true);
        Player player = event.getPlayer();
        player.sendMessage(config.getColoredMessage("error_messages.cant_place_block"));

    }

}
