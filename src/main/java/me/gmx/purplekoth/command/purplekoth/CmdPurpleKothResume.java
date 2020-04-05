package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;

public class CmdPurpleKothResume extends BSubCommand {

    public CmdPurpleKothResume(){
        this.aliases.add("resume");
        this.permission = "purplekoth.resume";
        this.correctUsage = "/purplekoth resume";
        this.senderMustBePlayer = false;
    }
    @Override
    public void execute() {
        if (PurpleKoth.getInstance().kothManager.getActiveKoth() != null){
            msg(Lang.PREFIX.toString() +"There's currently an active KOTH!");
            return;
        }
        if (PurpleKoth.getInstance().kothManager.getTask() != null){
            msg(Lang.PREFIX.toString() +"The countdown is currently running!");
            return;
        }else{
            PurpleKoth.getInstance().kothManager.reloadPrevCountdown();
            msg(Lang.PREFIX.toString() + "Countdown started!");
            return;
        }
    }
}
