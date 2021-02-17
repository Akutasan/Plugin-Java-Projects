package com.akutasan.creative_utilities.manager;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager
{
    private File config = new File("plugins/Creative_Utilities/config.yml");
    private FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);

    public void init()
    {
        if (!this.config.exists()) {
            try {
                this.config.createNewFile();
                this.cfg.set("NV-Disabled", "§6§l(!) §aDisabled your nightvision!");
                this.cfg.set("NV-Enabled", "§6§l(!) §aEnabled your nightvision!");
                this.cfg.set("NV-NoCommand", "§6§l(!) §cThis is not a valid command!");
                this.cfg.save(this.config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getFile()
    {
        return this.config;
    }

    public FileConfiguration getConfiguration()
    {
        return this.cfg;
    }
}
