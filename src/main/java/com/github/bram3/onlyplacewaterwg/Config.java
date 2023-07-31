package com.github.bram3.onlyplacewaterwg;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class Config {
    private OnlyPlaceWaterWG plugin;
    public YamlDocument messagesDocument;


    public Config(OnlyPlaceWaterWG plugin) {
        this.plugin = plugin;
        try {
            this.messagesDocument = YamlDocument.create(new File(plugin.getDataFolder(), "messages.yml"), Objects.requireNonNull(plugin.getResource("spigot-messages.yml")), GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize a config file! Shutting down...", e);
            Bukkit.shutdown();
        }
    }

    public String getColoredMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', messagesDocument.getString(path));
    }
}
