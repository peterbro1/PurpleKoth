package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;
import me.gmx.purplekoth.util.ChatUtils;

public class CmdPurpleKothSetTimer extends BSubCommand {

    public CmdPurpleKothSetTimer(){
        this.aliases.add("set");
        this.permission = "purplekoth.set";
        this.correctUsage = "/purplekoth set [xxHxxMxxS]";
        this.senderMustBePlayer = false;
    }
    @Override
    public void execute() {

       /* try{
            Integer.parseInt(args[0]);
        }catch(Exception e){
            msg(Lang.PREFIX + "Please enter a valid number!");
            return;
        }*/
       if (args.length < 1){
           sendCorrectUsage();
           return;
       }
        if (PurpleKoth.getInstance().kothManager.getActiveKoth() != null){
            msg(Lang.PREFIX + "There is currently an active KOTH. Please wait for it to finish.");
            return;
        }
        if (PurpleKoth.getInstance().kothManager.getTask() != null){
                PurpleKoth.getInstance().kothManager.tryStopCountdown();
                PurpleKoth.getInstance().kothManager.startCountdown(ChatUtils.getSecondsFromStringMultiple(args[0]));
                msg(Lang.PREFIX + "Countdown reset and changed!");
        }else{
            PurpleKoth.getInstance().kothManager.startCountdown(ChatUtils.getSecondsFromStringMultiple(args[0]));
            msg(Lang.PREFIX + "Countdown started!");

        }


    }
}
