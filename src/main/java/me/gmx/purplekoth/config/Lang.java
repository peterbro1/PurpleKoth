package me.gmx.purplekoth.config;

import me.gmx.purplekoth.core.BConfig;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;


public enum Lang {

    PREFIX("&6&lKOTH> &r "),
    MSG_ERROR("Error occured, please contact server developer."),
    MSG_PKOTH_USAGE("&4Incorrect usage! See /koth help."),
    MSG_USAGE_SUBCOMMAND("Incorrect usage. Correct usage is %usage%"),
    MSG_PKOTH_HELP("&2Insert generic help here."),
    ARENA_REMOVED("&aArena successfully removed"),
    ARENA_NOTFOUND("&4Arena not found!"),
    MSG_NOACCESS("You don't have access to this command."),
    HOVER_MESSAGE("&4Hover for results!"),
    MSG_PLAYERONLY("This command can be used only in the game."),
    MSG_NEXT_KOTH_FIRST("&4The next koth is in: &2%time% seconds"),
    MSG_NEXT_KOTH_ACTIVE("&4A KOTH is currently active!Time left: &2%time% . Previous Winner: %winner%"),
    MSG_NEXT_KOTH_ACTIVE_FIRST("&4A KOTH (%koth%) is currently active! Time left: &2%time% ."),
    MSG_CAP_NOGANG("&4You need to be in a gang to capture the point!"),
    MSG_CONFIGRELOADED("Config reloaded."),
    HOVER_GANG_MESSAGE("&2%gang% &3with &2%time%&3!"),
    KOTH_END_NOWINNER("&2The Koth: &3%koth% &2has ended with no winner!"),
    LANG_CONSOLE("The console cannot perform this action."),
    MSG_KOTH_ANNOUNCEMENT("&2A Koth is going to start in %time%"),
    MSG_KOTH_STOPPED("&4Koth stopped successfully."),
    MSG_KOTH_STARTED("&aKoth started successfully."),
    KOTH_WINNERONLY("&4Sorry, only the KOTH winners can open this chest."),
    ARENA_CREATE("KOTH arena successfully created!"),
    MSG_NEXT_KOTH("The next Koth will be in: %time%. Previous winner: %winner%"),
    KOTH_START_ANNOUNCEMENT("&4Heads up, a &2KOTH &4is starting: &2%koth%"),
    KOTH_END_ANNOUNCEMENT("&4Koth &2%koth% &4has ended!"),
    KOTH_DURING_ANNOUNCEMENT("&3%koth% &2is currently active! Find it at &1%x%, %y%, %z%"),
    KOTH_RESULTS_HEADER("&6-----&aResults&6-----"),
    KOTH_HOLOGRAM_TOP("&4Current capturing gang: %gang%"),
    KOTH_HOLOGRAM_MID("&2Time left: %timeleft%"),
    KOTH_HOLOGRAM_MID2("&2Gang in the lead: %winner%"),
    KOTH_HOLOGRAM_BOT("&3Gang capture time: %gangtime%"),
    EXPLANATION("Put/Explanation/Here"),
    PREVIOUS_WINNER("null");


    private String defaultValue;
    private static BConfig config;

    Lang(String str){
        defaultValue = str;
    }


    public String getPath() { return name(); }

    public String getDefaultValue() { return this.defaultValue; }

    public String toString() { return fixColors(config.getConfig().getString(getPath())); }

    public static void setConfig(BConfig paramBConfig) {
        config = paramBConfig;
        load();
    }

    public List<String> getStringList(){
        return Arrays.asList(toString().split("/"));
    }

    public String toMsg() {
        boolean bool = true;
        if (bool) {
            return fixColors(config.getConfig().getString(PREFIX.getPath()) + config.getConfig()
                    .getString(getPath()));
        }
        return fixColors(config.getConfig().getString(getPath()));
    }

    public void set( String o){
        config.getConfig().set(getPath(),o);
    }

    private static void load() {
        for (Lang lang : values()) {
            if (config.getConfig().getString(lang.getPath()) == null) {
                config.getConfig().set(lang.getPath(), lang.getDefaultValue());
            }
        }
        config.save();
    }


    private String fixColors(String paramString) {
        if (paramString == null)
            return "";
        return ChatColor.translateAlternateColorCodes('&', paramString);
    }
}
