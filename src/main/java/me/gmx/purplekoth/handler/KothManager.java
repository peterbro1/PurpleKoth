package me.gmx.purplekoth.handler;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.config.Settings;
import me.gmx.purplekoth.objects.Arena;
import me.gmx.purplekoth.objects.Koth;
import me.gmx.purplekoth.tasks.RandomKoth;
import me.gmx.purplekoth.util.ChatUtils;
import me.gmx.purplekoth.util.MapUtils;
import net.brcdev.gangs.gang.Gang;
import net.md_5.bungee.api.chat.TextComponent;
import net.royawesome.jlibnoise.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;

public class KothManager {

    private PurpleKoth ins;

    private Koth activeKoth;
    private List<Arena> arenaList;
    private int streak = 0;
    private RandomKoth task;
    private Koth prevKoth;
    private String prevWinner;


    public KothManager(PurpleKoth ins){
        this.ins = ins;
        arenaList = new ArrayList<Arena>();
        activeKoth = null;
        prevKoth = null;
        reloadPrevWinner();
    }

    public void tryStopCountdown(){
        try{
                this.task.cancel();
        }catch(Exception e){
        }
        this.task = null;
    }

    public String getPrevWinner(){
        return prevWinner;
    }

    public void reloadPrevWinner(){
        if (Lang.PREVIOUS_WINNER.toString().equals("null")){
            prevWinner="null";
        }else{
            prevWinner = Lang.PREVIOUS_WINNER.toString();
        }
    }
    public boolean reloadPrevCountdown(){
        if (!Settings.PREV_COUNTDOWN.getString().equals("null") && !Settings.PREV_COUNTDOWN.getString().equals("0")){
            long i = 0;
            try {
                i = Long.parseLong(Settings.PREV_COUNTDOWN.getString());
                int l = (int) (i - System.currentTimeMillis())/1000;
                System.out.println("Successfully reloaded previous countdown. I: " + i + "//L: " + l);
                startCountdown(l);
                return true;
            }catch (Exception e){
                startCountdown(ChatUtils.getSecondsFromString(Settings.TIME_BETWEEN_KOTH.getString()));
                ins.log("Unable to reload previous countdown, current version does not auto start!");
                e.printStackTrace();
                return false;
            }

        }
        //startCountdown(ChatUtils.getSecondsFromString(Settings.TIME_BETWEEN_KOTH.getString()));
        return false;
    }

    public void init(){
        loadArenas();
        if (getArenas().isEmpty()){
            ins.log("No arenas found. Stopped timer. To begin, add an arena and then type /pkoth start");
            return;
        }
        //task = new RandomKoth(ins, ChatUtils.getSecondsFromString(Settings.TIME_BETWEEN_KOTH.getString()));
        reloadPrevCountdown();
        //System.out.println("Detected previous countdown, resuming!");
    }

    public void saveCountdown(){

        try{
            Settings.PREV_COUNTDOWN.set(String.valueOf(getTask().getNext()));
        }catch(Exception e){
            System.out.println(Lang.PREFIX.toString() + "Failed to save countdown.");
        }
        if (getTask() != null ){
            if (!getTask().isCancelled()) {
                Settings.PREV_COUNTDOWN.set(String.valueOf(getTask().getNext()));
            }else{
                Settings.PREV_COUNTDOWN.set("null");
            }

        }else{
            Settings.PREV_COUNTDOWN.set("null");
        }
        PurpleKoth.getInstance().saveMain();

    }

    public Koth getActiveKoth(){
        return this.activeKoth;
    }
    public List<Arena> getArenas(){return this.arenaList;}

    private void startKoth(Koth k){
        activeKoth = k;
        k.start();
    }
    public boolean kothActive(){
        return getActiveKoth() != null;
    }

    public RandomKoth getTask(){
        return this.task;
    }
    public void silentEnd(){
        if (getActiveKoth() != null)
        getActiveKoth().end();

        prevKoth = activeKoth;
        activeKoth = null;
    }

    public Koth getPrevKoth(){
        return this.prevKoth;
    }

    public void endKoth(){
        getActiveKoth().end();
        LinkedHashMap<Gang, Integer> o = MapUtils.sortByValue(getActiveKoth().getContestants());
        ArrayList<String> fin = MapUtils.cleanMapText(o, Lang.HOVER_GANG_MESSAGE.toString());
        if (getActiveKoth().winner() == null){
            Bukkit.broadcastMessage(Lang.KOTH_END_NOWINNER.toMsg()
                    .replace("%koth%",getActiveKoth().getArena().getName()));

        }else {
            Bukkit.broadcastMessage(Lang.KOTH_END_ANNOUNCEMENT.toMsg()
                    .replace("%koth%", getActiveKoth().getArena().getName())
                    .replace("%winner%", getActiveKoth().winner().getKey().getName()));

            TextComponent message = ChatUtils.getHoverText(Lang.HOVER_MESSAGE.toMsg(), fin);
                Lang.PREVIOUS_WINNER.set(getActiveKoth().winner().getKey().getName());
                PurpleKoth.getInstance().saveLang();



            for (Player p : Bukkit.getOnlinePlayers()) {
                p.spigot().sendMessage(message);
            }
            activeKoth.spawnChest();
            prevWinner = activeKoth.winner().getKey().getName();
        }

        prevKoth = activeKoth;
        activeKoth = null;
    }

    public void startCountdown(int time){
        System.out.println("startCountdown: " + time);
        this.task = new RandomKoth(ins,time);
        task.start();
    }
    public void startCountdown(boolean after){
        if (!after)
             this.task = new RandomKoth(ins,ChatUtils.getSecondsFromString(Settings.TIME_BETWEEN_KOTH.getString()));
        else
            this.task = new RandomKoth(ins,ChatUtils.getSecondsFromString(Settings.TIME_BETWEEN_KOTH.getString())-Settings.KOTH_DURATION.getNumber());

        task.start();
    }

    public  void tryStartNewKoth(){
        if (kothActive())
            return;
        tryStopCountdown();
        int randomInt = (int)(arenaList.size() * Math.random());
            startKoth(new Koth(this.arenaList.get(randomInt),ChatUtils.getSecondsFromString(Settings.KOTH_DURATION.getString())));

    }

    public void loadArenas(){
        arenaList = new ArrayList<Arena>();
        FileConfiguration config = ins.getArenaConfig();
        List<String>  ar = config.getStringList("arenas");
        for (String s : PurpleKoth.getInstance().getArenaConfig().getKeys(false)){
            try {
                loadArena(s);
            }catch(Exception e){
                ins.getLogger().log(Level.WARNING,"Failed to load arena " + s);
                e.printStackTrace();
            }
            }
    }

    public void loadArena(String id){
        try{
            getArenas().add(Arena.loadFromConfig(id));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
