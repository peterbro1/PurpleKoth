package me.gmx.purplekoth.command;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.command.purplekoth.*;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BCommand;
import me.gmx.purplekoth.core.BSubCommand;
import me.gmx.purplekoth.util.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;



public class CmdPurpleKoth extends BCommand implements CommandExecutor {

    public CmdPurpleKoth(PurpleKoth instance) {
        super(instance);
        this.subcommands.add(new CmdPurpleKothCreate());
        this.subcommands.add(new CmdPurpleKothHelp());
        this.subcommands.add(new CmdPurpleKothReload());
        this.subcommands.add(new CmdPurpleKothRemove());
        this.subcommands.add(new CmdPurpleKothStopAll());
        this.subcommands.add(new CmdPurpleKothStart());
        this.subcommands.add(new CmdPurpleKothList());
        this.subcommands.add(new CmdPurpleKothAddLoot());
        this.subcommands.add(new CmdPurpleKothTest());
        this.subcommands.add(new CmdPurpleKothResume());
        this.subcommands.add(new CmdPurpleKothReset());
        this.subcommands.add(new CmdPurpleKothSetTimer());

    }




    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if (arg3.length < 1) {
            if (!arg0.hasPermission("purplekoth.koth")){
                arg0.sendMessage(Lang.MSG_NOACCESS.toMsg());
                return true;
            }



            if (PurpleKoth.getInstance().kothManager.getActiveKoth() == null) {

                //active koth?
                if (Lang.PREVIOUS_WINNER.toString().equals("null")) { //prev winner

                    if (PurpleKoth.getInstance().kothManager.getTask() == null){
                        arg0.sendMessage(Lang.PREFIX.toString() + ChatColor.RED + "There are no scheduled KOTHs.");
                        return false;
                    }

                    arg0.sendMessage(Lang.MSG_NEXT_KOTH_FIRST.toMsg()
                            .replace("%time%", ChatUtils.getStringFromNumberSecond(PurpleKoth.getInstance().kothManager.getTask().getTimeLeft()))); //time until next koth
                }else{

                    if (PurpleKoth.getInstance().kothManager.getTask() == null){
                        arg0.sendMessage(Lang.PREFIX.toString() + ChatColor.RED + "There are no scheduled KOTHs.");
                        return false;
                    }

                        arg0.sendMessage(Lang.MSG_NEXT_KOTH.toMsg()
                                .replace("%time%", ChatUtils.getStringFromNumberSecond(PurpleKoth.getInstance().kothManager.getTask().getTimeLeft()))
                                .replace("%winner%", PurpleKoth.getInstance().kothManager.getPrevWinner()));



                }



            }else{



                if (Lang.PREVIOUS_WINNER.toString().equals("null")) {

                    //prev winner



                    arg0.sendMessage(Lang.MSG_NEXT_KOTH_ACTIVE_FIRST.toMsg()
                            .replace("%time%", ChatUtils.getStringFromNumberSecond(PurpleKoth.getInstance().kothManager.getActiveKoth().getTimeLeft()))
                    .replace("%koth%",PurpleKoth.getInstance().kothManager.getActiveKoth().getArena().getName())); //time remaining current koth
                }else{



                    arg0.sendMessage(Lang.MSG_NEXT_KOTH_ACTIVE.toMsg()
                            .replace("%time%", ChatUtils.getStringFromNumberSecond(PurpleKoth.getInstance().kothManager.getActiveKoth().getTimeLeft()))
                    .replace("%winner%",PurpleKoth.getInstance().kothManager.getPrevWinner())
                            .replace("%koth%",PurpleKoth.getInstance().kothManager.getActiveKoth().getArena().getName())); //time remaining current koth
                }
            }
            return true;
        }



        for (BSubCommand cmd : this.subcommands) {
            if (cmd.aliases.contains(arg3[0])) {
                cmd.execute(arg0,arg3);
                return true;
            }
        }
        arg0.sendMessage(Lang.MSG_PKOTH_USAGE.toMsg());

        return true;
    }

}
