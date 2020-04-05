package me.gmx.purplekoth.config;


import me.gmx.purplekoth.core.BConfig;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public enum Settings {

    TIME_BETWEEN_KOTH("20"), //supports
    KOTH_DURATION("120"), //supports
    KOTH_BROADCAST_PERIOD("5"),
    COOLDOWN("20"), //unused
    LOOT_CHEST_TITLE("&2KOTH Loot"),
    LOOT_CHEST_DURATION("15"),
    LOOT_CHEST_ROLLS("4"),
    KOTH_ANNOUNCEMENT_PERIOD("1m/20s/10s"),//supports
    KOTH_CAPTURE_DURATION("5"),
    KOTH_TIME_SUBTRACTION("true"),
    PREV_COUNTDOWN("null");


    private String defaultValue;
    private static BConfig config;
    private String prefix = ChatColor.DARK_RED + "" + ChatColor.BLACK;
    Settings(String str){
        defaultValue = str;
    }


    public String getPath() { return name(); }

    public String getDefaultValue() { return this.defaultValue; }


    public static void setConfig(BConfig paramBConfig) {
        config = paramBConfig;
        load();
    }
    public String getEncodeString(){
        return prefix + ChatColor.translateAlternateColorCodes('&',config.getConfig().getString(getPath()));

    }
    public int getNumber() {
        return Integer.parseInt(config.getConfig().getString(getPath()));
    }

    public List<String> getStringList(){
        return Arrays.asList(getString().split("/"));
    }


    public boolean getBoolean() throws Exception{

        try {
            return Boolean.valueOf(config.getConfig().getString(getPath()));
        }catch(NullPointerException e) {
            throw new Exception("Value could not be converted to a boolean");
        }

    }
    public String getString(){
        return ChatColor.translateAlternateColorCodes('&',config.getConfig().getString(getPath()));
    }
    public void set( String o){
        config.getConfig().set(getPath(),o);
    }

    private static void load() {
        for (Settings lang : values()) {
            if (config.getConfig().getString(lang.getPath()) == null) {
                config.getConfig().set(lang.getPath(), lang.getDefaultValue());
            }
        }
        config.save();
    }
}
