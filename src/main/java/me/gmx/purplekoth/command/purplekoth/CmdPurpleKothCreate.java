package me.gmx.purplekoth.command.purplekoth;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.listener.WorldGuardWeatherListener;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;
import me.gmx.purplekoth.handler.KothManager;
import me.gmx.purplekoth.objects.Arena;
import me.gmx.purplekoth.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BlockVector;

public class CmdPurpleKothCreate extends BSubCommand {

    public CmdPurpleKothCreate(){
        this.aliases.add("createarena");
        this.aliases.add("addarena");
        this.permission = "purplekoth.create";
        this.correctUsage = "/purplekoth createarena [arena name] [worldguard region]";

    }

    @Override
    public void execute(){
        if (this.args.length != 2){
            sendCorrectUsage();
            return;
        }

        if (!PurpleKoth.getInstance().wgp.getRegionManager(player.getWorld()).hasRegion(args[1])){
            sender.sendMessage(Lang.PREFIX + "Region " + args[1] + " could not be found");
            return;
        }

        ProtectedRegion r = PurpleKoth.getInstance().wgp.getRegionManager(player.getWorld()).getRegion(args[1]);
        com.sk89q.worldedit.BlockVector bv = r.getMinimumPoint();
        PurpleKoth k = PurpleKoth.getInstance();
        Location bl, tr;
        bl = new Location(player.getWorld(),r.getMinimumPoint().getBlockX(),r.getMinimumPoint().getBlockY(),r.getMinimumPoint().getBlockZ());
        tr = new Location(player.getWorld(),r.getMaximumPoint().getBlockX(),r.getMaximumPoint().getBlockY(),r.getMaximumPoint().getBlockZ());

        Arena a = new Arena(args[0],bl,tr);
        FileUtils.writeArena(a);
        PurpleKoth.getInstance().kothManager.loadArenas();
        msg(Lang.ARENA_CREATE.toMsg());

    }
}
