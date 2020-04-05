package me.gmx.purplekoth.tasks;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.config.Settings;
import me.gmx.purplekoth.objects.Koth;
import me.gmx.purplekoth.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class RandomKoth extends BukkitRunnable {
    private PurpleKoth ins;
    private int counter;
    private int duration;
    private long next;
    List<Integer> per;
    private BukkitTask taskk;
    public RandomKoth(PurpleKoth plugin, int duration){
        this.ins = plugin;
        per = ChatUtils.getTimeFromString(Settings.KOTH_ANNOUNCEMENT_PERIOD.getString());
        next = System.currentTimeMillis() + duration*1000;
    }
    @Override
    public void run(){
    counter++;
        for (int i : per){
            if (getTimeLeft() - i == 0){
                Bukkit.broadcastMessage(Lang.MSG_KOTH_ANNOUNCEMENT.toMsg().replace("%time%", ChatUtils.getStringFromNumberSecond(getTimeLeft())));
            }
        }

        if (getTimeLeft() <= 0){
            if (!this.isCancelled()) {
                this.cancel();
                this.ins.kothManager.tryStartNewKoth();

            }
        }

    }

    public long getNext(){
        return this.next;
    }
    public long getTimeLeft(){
        return (next - System.currentTimeMillis())/1000;
    }


    public void start(){
        counter = 0;
         taskk = this.runTaskTimer(ins,5,20);
    }
    public BukkitTask getTask(){
        return taskk;
    }
}
