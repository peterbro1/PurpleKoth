package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;

public class CmdPurpleKothReload extends BSubCommand {

    public CmdPurpleKothReload(){
        this.aliases.add("reload");
        this.permission = "purplekoth.reload";
        this.correctUsage = "/purplekoth reload";
        this.senderMustBePlayer = false;
    }
    @Override
    public void execute() {
        PurpleKoth.getInstance().reloadArenaConfig();
        PurpleKoth.getInstance().kothManager.loadArenas();
        PurpleKoth.getInstance().reloadLootConfig();
        PurpleKoth.getInstance().reloadConfig();
        PurpleKoth.getInstance().kothManager.reloadPrevWinner();
        msg(Lang.MSG_CONFIGRELOADED.toMsg());
    }
}
