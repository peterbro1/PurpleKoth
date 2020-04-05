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

public class CmdPurpleKothRemove extends BSubCommand {

    public CmdPurpleKothRemove(){
        this.aliases.add("remove");
        this.aliases.add("removearena");
        this.permission = "purplekoth.remove";
        this.correctUsage = "/purplekoth remove [arena name]";

    }

    @Override
    public void execute(){
        if (this.args.length != 1){
            sendCorrectUsage();
            return;
        }
        try{
            Arena a = Arena.loadFromConfig(args[0]);
            FileUtils.removeArena(a);
            PurpleKoth.getInstance().kothManager.getArenas().remove(a);
            PurpleKoth.getInstance().kothManager.loadArenas();
            msg(Lang.ARENA_REMOVED.toMsg());
        }catch(Exception e){
            msg(Lang.ARENA_NOTFOUND.toMsg());
        }

    }
}
