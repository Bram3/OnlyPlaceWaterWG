package com.github.bram3.onlyplacewaterwg;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.session.SessionManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
        if (event.getItem().getType() == Material.WATER_BUCKET || event.getItem().getType() == Material.BUCKET) return;
        Location location = event.getPlayer().getLocation();
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location wrappedLocation = BukkitAdapter.adapt(location);
        ApplicableRegionSet set = query.getApplicableRegions(wrappedLocation);
        if (!(set.testState(null, plugin.WATER_FLAG))) return;
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (hasBypass(player, world)) return;
        try {
            ApplicableRegionSet regionSet = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).getApplicableRegions(BlockVector3.at(location.getX(), location.getY(), location.getZ()));
            for (ProtectedRegion region : regionSet.getRegions()) {
                if (region.isMember(plugin.worldGuardPlugin.wrapPlayer(player))) return;
            }
        } catch (NullPointerException e) {
            // No regions found, continue
        }
        event.setCancelled(true);
        player.sendMessage(config.getColoredMessage("error_messages.cant_interact_with_block"));

    }

    private boolean hasBypass(Player player, World world) {
        com.sk89q.worldedit.world.World wrappedWorld = BukkitAdapter.adapt(world);
        LocalPlayer wrappedPlayer = plugin.worldGuardPlugin.wrapPlayer(player);
        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        return sessionManager.hasBypass(wrappedPlayer, wrappedWorld);
    }
}