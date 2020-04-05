package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;

public class CmdPurpleKothStopAll extends BSubCommand {

    public CmdPurpleKothStopAll(){
        this.aliases.add("stop");
        this.permission = "purplekoth.stop";
        this.correctUsage = "/purplekoth stop";
        this.senderMustBePlayer = false;
    }

    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }
        if (PurpleKoth.getInstance().kothManager.getActiveKoth() != null){
            PurpleKoth.getInstance().kothManager.silentEnd();
            msg(Lang.PREFIX.toString() + "&3Active KOTH stopped.");
            return;
        }else if (PurpleKoth.getInstance().kothManager.getActiveKoth() == null && PurpleKoth.getInstance().kothManager.getTask() != null){
            PurpleKoth.getInstance().kothManager.saveCountdown();
            PurpleKoth.getInstance().kothManager.tryStopCountdown();
            msg(Lang.PREFIX.toString() + "&3KOTH Countdown stopped.");
            return;
        }else{
            msg(Lang.PREFIX.toString() + "&3There's nothing to stop, silly!");
            return;
        }
        //msg(Lang.MSG_KOTH_STOPPED.toMsg());


    }
}
