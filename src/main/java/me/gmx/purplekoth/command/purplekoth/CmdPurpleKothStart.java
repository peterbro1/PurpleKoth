package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;

public class CmdPurpleKothStart extends BSubCommand {

    public CmdPurpleKothStart(){
        this.aliases.add("start");
        this.permission = "purplekoth.start";
        this.correctUsage = "/purplekoth start";
        this.senderMustBePlayer = false;
    }

    @Override
    public void execute() {
        if (args.length != 0){
            sendCorrectUsage();
            return;
        }
        PurpleKoth.getInstance().kothManager.saveCountdown();
        PurpleKoth.getInstance().kothManager.tryStartNewKoth();
        msg(Lang.MSG_KOTH_STARTED.toMsg());

    }
}
