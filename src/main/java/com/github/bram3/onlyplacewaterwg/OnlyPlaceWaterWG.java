package com.github.bram3.onlyplacewaterwg;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class OnlyPlaceWaterWG extends JavaPlugin {
    public StateFlag WATER_FLAG;
    public WorldGuardPlugin worldGuardPlugin;

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("only-place-water", false);
            registry.register(flag);
            WATER_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("only-place-water");
            if (existing instanceof StateFlag) {
                WATER_FLAG = (StateFlag) existing;
            } else {
                getLogger().log(Level.SEVERE, "The flag is already registered by another plugin. Exiting...");
                Bukkit.shutdown();
            }
        }
    }

    @Override
    public void onEnable() {
        Config config = new Config(this);
        worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        getServer().getPluginManager().registerEvents(new EventListener(this, config), this);
    }


}
