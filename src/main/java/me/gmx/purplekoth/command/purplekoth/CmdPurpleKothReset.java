package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;
import me.gmx.purplekoth.objects.Arena;
import org.bukkit.ChatColor;

public class CmdPurpleKothReset extends BSubCommand {

    public CmdPurpleKothReset(){
        this.aliases.add("reset");
        this.permission = "purplekoth.reset";
        this.correctUsage = "/purplekoth reset";
        this.senderMustBePlayer = false;
    }
    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }

        if (PurpleKoth.getInstance().kothManager.kothActive()){
            msg(Lang.PREFIX.toString() + " There's currently an active KOTH, please use /koth stop!");
            return;
        }

        if (PurpleKoth.getInstance().kothManager.getTask() != null){
            PurpleKoth.getInstance().kothManager.tryStopCountdown();
        }
        PurpleKoth.getInstance().kothManager.startCountdown(false);
        msg(Lang.PREFIX.toString() + " Timer reset!");
    }
}
