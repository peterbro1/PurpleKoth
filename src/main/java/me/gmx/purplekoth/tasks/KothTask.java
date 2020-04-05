package me.gmx.purplekoth.tasks;

import com.sk89q.worldedit.blocks.TileEntityBlock;
import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.config.Settings;
import me.gmx.purplekoth.objects.Arena;
import me.gmx.purplekoth.objects.Koth;
import me.gmx.purplekoth.util.ChatUtils;
import net.brcdev.gangs.gang.Gang;
import org.bukkit.Bukkit;
import org.bukkit.block.Beacon;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class KothTask implements Runnable {
    private Koth koth;
    private final PurpleKoth plugin;
    private int duration;
    private int elapsed;
    private int broadcastPeriod;
    private Arena arena;
    private String x,y,z;
    public KothTask(PurpleKoth plugin, Koth koth,int duration){
        this.plugin = plugin;
        this.koth = koth;
        elapsed = 0;
        this.arena = koth.getArena();
        broadcastPeriod = Settings.KOTH_BROADCAST_PERIOD.getNumber();
        this.duration = duration;
        x = String.valueOf(koth.getArena().getMiddle().getBlockX());
        y = String.valueOf(koth.getArena().getMiddle().getBlockY());
        z = String.valueOf(koth.getArena().getMiddle().getBlockZ());
        Bukkit.broadcastMessage(Lang.KOTH_START_ANNOUNCEMENT.toMsg()
                .replace("%koth%",this.koth.getArena().getName())
                .replace("%x%", this.x)
                .replace("%y%", this.y)
                .replace("%z%", this.z));
    }

    public int getTimeLeft(){
        return duration - elapsed;
    }

    @Override
    public void run() {

        elapsed++;
        this.koth.updatePlayers();
        this.koth.updateList();
        this.koth.updateHologram();
        koth.updateTimeLeft(elapsed);
        if (elapsed % broadcastPeriod == 0) {//placeholders: %koth% %x% %y% %z% %time%
            Bukkit.broadcastMessage(Lang.KOTH_DURING_ANNOUNCEMENT.toMsg()
                    .replace("%koth%", this.koth.getArena().getName())
                    .replace("%x%", this.x)
                    .replace("%y%", this.y)
                    .replace("%z%", this.z)
                    .replace("%time%", ChatUtils.getStringFromNumberSecond(this.koth.getTimeLeft())));

        }
        //for (Map.Entry<Gang,Integer> entry : this.koth.getContestants()){
        //    }
        if (elapsed >= duration){
            plugin.kothManager.endKoth();
            try {
                plugin.kothManager.startCountdown(Settings.KOTH_TIME_SUBTRACTION.getBoolean());
            }catch(Exception e){
                plugin.kothManager.startCountdown(false);
            }
        }
    }

}
