package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CmdPurpleKothHelp extends BSubCommand {

    public CmdPurpleKothHelp(){
        this.aliases.add("help");
        this.correctUsage = "/purplekoth help";
        this.permission = "purplekoth.help";
    }

    @Override
    public void execute() {
        msg(Lang.PREFIX.toString() + "&6&l----Purple Koth Commands----");
        msg(Lang.PREFIX.toString() + "&6/pkoth start " + "&9- Attempts to start a random KOTH.");
        msg(Lang.PREFIX.toString() + "&6/pkoth resume " + "&9- Starts the countdown to next KOTH");
        msg(Lang.PREFIX.toString() + "&6/pkoth stop " + "&9- Attempts to stop the current KOTH (silently).");
        msg(Lang.PREFIX.toString() + "&6/pkoth create [arena2] [region2] " + "&9- Creates a KOTH arena with the given name from the given Worldguard region.");
        msg(Lang.PREFIX.toString() + "&6/pkoth list " + "&9- Lists all loaded KOTH arenas.");
        msg(Lang.PREFIX.toString() + "&6/pkoth reload " + "&9- Reloads config values.");
        msg(Lang.PREFIX.toString() + "&6/pkoth remove [arena2] " + "&9- Removes a given KOTH arena.");
        msg(Lang.PREFIX.toString() + "&6/pkoth loot " + "&9- Opens the loot menu.");
        msg(Lang.PREFIX.toString() + "&6/pkoth reset " + "&9- Sets time until next KOTH to default.");
        msg(Lang.PREFIX.toString() + "&6/pkoth set [5h32m55s]" + "&9- Sets time to next koth");

        msg(Lang.PREFIX.toString() + "&4&lVersion: " + ChatColor.AQUA + PurpleKoth.getInstance().getDescription().getVersion());

        for (String s : Lang.EXPLANATION.getStringList()){
            msg(Lang.PREFIX.toString() + s);
        }


    }
}
