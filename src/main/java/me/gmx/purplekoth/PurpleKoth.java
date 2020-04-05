package me.gmx.purplekoth;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.gmx.purplekoth.command.CmdPurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.config.Settings;
import me.gmx.purplekoth.core.BConfig;
import me.gmx.purplekoth.handler.KothManager;
import me.gmx.purplekoth.handler.PlayerListener;
import me.gmx.purplekoth.hook.GangsHook;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import sun.security.krb5.Config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurpleKoth extends JavaPlugin {

    Logger logger;
    private static PurpleKoth ins;
    public File arenaFile;
    public FileConfiguration arenaConfig;
    public File lootFile;
    public FileConfiguration lootConfig;
    public WorldGuardPlugin wgp;
    public BConfig langConfig;
    public BConfig mainConfig;
    public KothManager kothManager;
    public GangsHook gangHook = null;

    @Override
    public void onEnable(){
        ins = this;
        logger = getLogger();


        initConfig();
        logger.log(Level.INFO, String.format("[%s] Successfully enabled version %s!", new Object[] { getDescription().getName(), getDescription().getVersion() }));

        if (getServer().getPluginManager().getPlugin("Worldguard") == null){
            getServer().getPluginManager().disablePlugin(this);
        }else{
            this.wgp = WorldGuardPlugin.inst();
        }
        this.langConfig = new BConfig(this,"Lang.yml");
        this.mainConfig = new BConfig(this,"Settings.yml");
        Lang.setConfig(this.langConfig);
        Settings.setConfig(this.mainConfig);

        registerCommands();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),getInstance());

        if (getServer().getPluginManager().getPlugin("GangsPlus") != null){
            gangHook = new GangsHook();

            log("GangsPlus located! Creating hook.");
        }

        kothManager = new KothManager(getInstance());
        kothManager.init();

    }
    @Override
    public void onDisable(){
        kothManager.saveCountdown();
        Bukkit.getScheduler().cancelTasks(this);
        saveArenaConfig();
        saveLootConfig();
        kothManager.tryStopCountdown();
        kothManager.silentEnd();
        langConfig.save();
        mainConfig.save();
    }
    public void reloadConfig() {
        this.langConfig = new BConfig(this,"Lang.yml");
        Lang.setConfig(this.langConfig);
        this.mainConfig = new BConfig(this,"Settings.yml");
        Settings.setConfig(this.mainConfig);
    }

    public void log(String message){
        logger.log(Level.INFO,"["+getDescription().getName()+"] " + message);
    }

    public FileConfiguration getArenaConfig(){
        return this.arenaConfig;
    }
    private void registerCommands(){
        getCommand("purplekoth").setExecutor(new CmdPurpleKoth(getInstance()));
    }
    public FileConfiguration getLootConfig(){
        return this.lootConfig;
    }

    public void saveArenaConfig(){
        try{
            getArenaConfig().save(arenaFile);
        }catch(Exception e){
          e.printStackTrace();
        }
    }
    public void saveLootConfig(){
        try{
            getLootConfig().save(lootFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void reloadArenaConfig(){
        try {
            arenaConfig.load(new File(getDataFolder(), "arenas.yml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void reloadLootConfig(){
        try {
            lootConfig.load(new File(getDataFolder(), "loot.yml"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initConfig() {

        try {
        arenaFile = new File(getDataFolder(), "arenas.yml");
        if (!arenaFile.exists()) {
            arenaFile.getParentFile().mkdirs();
            saveResource("arenas.yml", false);
        }
        arenaConfig= new YamlConfiguration();

            arenaConfig.load(arenaFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            lootFile = new File(getDataFolder(), "loot.yml");
            if (!lootFile.exists()) {
                lootFile.getParentFile().mkdirs();
                saveResource("loot.yml", false);
            }
            lootConfig= new YamlConfiguration();

            lootConfig.load(lootFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveLang(){
        this.langConfig.save();
    }
    public void saveMain(){
        this.mainConfig.save();
    }
    public BConfig getMain(){
        return this.mainConfig;
    }

    public static PurpleKoth getInstance(){
        return ins;
    }
}
