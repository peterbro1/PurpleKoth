package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;
import me.gmx.purplekoth.objects.Arena;
import org.bukkit.ChatColor;

public class CmdPurpleKothList extends BSubCommand {

    public CmdPurpleKothList(){
        this.aliases.add("list");
        this.aliases.add("listarenas");
        this.permission = "purplekoth.list";
        this.correctUsage = "/purplekoth list";
        this.senderMustBePlayer = false;
    }
    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }
        if (PurpleKoth.getInstance().kothManager.getArenas().isEmpty()){
            msg(Lang.PREFIX.toString() + "&4There are currently no loaded arenas.");
            return;
        }
        msg(Lang.PREFIX.toString() + "&2Current Arenas:");
        for (Arena a : PurpleKoth.getInstance().kothManager.getArenas()){
            msg(Lang.PREFIX.toString() + ChatColor.GREEN + a.getName());
        }
    }
}
