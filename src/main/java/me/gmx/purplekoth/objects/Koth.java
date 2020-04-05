package me.gmx.purplekoth.objects;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.command.purplekoth.CmdPurpleKothAddLoot;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.config.Settings;
import me.gmx.purplekoth.tasks.KothTask;
import me.gmx.purplekoth.util.LocationUtil;
import net.brcdev.gangs.gang.Gang;
import net.brcdev.gangs.util.LocationUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Koth {

    private Arena arena;
    private int duration;
    private int elapsed;
    private List<Player> activePlayers;
    List<Gang> activeGangs;
    private Runnable task;
    private int step;
    private List<BlockStorage> _border;

    private int taskId;
    private Gang winner;
    private HologramWrapper holo;
    private GameState state;
    public LinkedHashMap<Gang,Integer> contestants;
    public Koth(Arena a, int duration){
        state = GameState.OFF;
        this.arena = a;
        this.duration = duration;
        this.elapsed = 0;
        activePlayers = new ArrayList<Player>();
        _border = new ArrayList<BlockStorage>();
        activeGangs = new ArrayList<Gang>();
        task = new KothTask(PurpleKoth.getInstance(),this,duration);
        contestants = new LinkedHashMap<Gang,Integer>();
        step = 20;
        winner = null;

    }


    public void startTask(){
       this.taskId = Bukkit.getScheduler().runTaskTimer(PurpleKoth.getInstance(),this.task,5,step).getTaskId();

    }
    public void endTask(){
        holo.delete();
        Bukkit.getScheduler().cancelTask(this.taskId);
    }
    public LinkedHashMap<Gang,Integer> getContestants(){return this.contestants;}
    public List<Player> getActivePlayers(){
        return this.activePlayers;
    }
    public int getDuration(){return this.duration;}
    public int getElapsed(){return this.elapsed;}
    public Arena getArena(){return this.arena;}

    public void start(){
        holo = new HologramWrapper(this);
        startTask();
        for (Block b : LocationUtil.getBorders(getArena().getBotLeft(),getArena().getTopRight())){
            _border.add(new BlockStorage(b.getLocation(),b.getState()));
        }
        /*for (BlockStorage blst : getArena().getCorners()){
            _border.add(new BlockStorage(blst.getLocation().clone().subtract(0,1,0),blst.getLocation().clone().subtract(0,1,0).getBlock().getState()));
            for (BlockStorage b : LocationUtil.getSquareAround(blst.getLocation().clone().subtract(0,1,0),1)){
                _border.add(b);
            }
        }*/
        _border.addAll(getArena().getSetEmeralds());
    }
    public void end(){
        endTask();
        PurpleKoth.getInstance().kothManager.tryStopCountdown();
        revertBorder();
    }


    public void revertBorder(){
        for (BlockStorage bs : _border){
            bs.restore();
        }
        getArena().getMiddle().getBlock().setType(Material.AIR);

    }

    public void spawnChest(){
        Block b = getArena().getMiddle().getBlock();
        if (b.getType().equals(Material.AIR)){
            b.setType(Material.CHEST);
            Chest chest = (Chest) b.getState();
            chest.setCustomName(Settings.LOOT_CHEST_TITLE.getEncodeString()); //<-- not working
            LootChest lootchest = new LootChest(chest);
            lootchest.fillChest(chest.getSnapshotInventory());

           chest.update();
        }

        new BukkitRunnable(){
            private int counter = 0;
            public void run(){
                getArena().getWorld().spawnParticle(Particle.FIREWORKS_SPARK,getArena().getMiddle().add(0.5,1,0.5),3);
                getArena().getWorld().playEffect(getArena().getMiddle(),Effect.MOBSPAWNER_FLAMES,0);
                if ((counter/2) >= Settings.LOOT_CHEST_DURATION.getNumber()){
                    getArena().getMiddle().getBlock().setType(Material.AIR);
                    this.cancel();
                }
                counter++;
            }
        }.runTaskTimer(PurpleKoth.getInstance(),1,10);
    }

    public void updateTimeLeft(int elapsed){
        this.elapsed = elapsed;
    }
    public List<Gang> getActiveGangs(){
        return this.activeGangs;
    }

    public GameState getState(){
        return this.state;
    }

    public int getTimeLeft(){
        return this.duration - this.elapsed;
    }

    public Map.Entry<Gang,Integer> winner(){
        Map.Entry<Gang, Integer> maxEntry = null;

        for (Map.Entry<Gang, Integer> entry : getContestants().entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    public void updatePlayers(){
        activePlayers.clear();
        activeGangs.clear();
        for (Player p : getArena().getWorld().getPlayers()){
            if (getArena().isInsideArena(p.getLocation())){
                activePlayers.add(p);
                if (PurpleKoth.getInstance().gangHook.getPlayerGang(p) != null ) {
                    if (!activeGangs.contains(PurpleKoth.getInstance().gangHook.getPlayerGang(p))) {
                        activeGangs.add(PurpleKoth.getInstance().gangHook.getPlayerGang(p));
                    }
                }else {
                    p.sendMessage(Lang.MSG_CAP_NOGANG.toMsg());
                }
            }

        }
        updateState();
    }

    public void updateList(){
        if (activeGangs.size() == 1){
            if (contestants.containsKey(activeGangs.get(0))){
                contestants.put(activeGangs.get(0),contestants.get(activeGangs.get(0))+1);
                if (contestants.get(activeGangs.get(0)) > Settings.KOTH_CAPTURE_DURATION.getNumber()){

                }
            }else{
                contestants.put(activeGangs.get(0),1);
            }
        }
    }
    public void updateHologram(){
        holo.update();
    }

    public void updateState(){
        int s = activeGangs.size();
        if (s == 0 ){
            this.state = GameState.EMPTY;
        }else if (s == 1){
            this.state = GameState.CAPTURING;
        }else if (s > 1){
            this.state = GameState.CONTESTED;
        }
        updateBlocks();
        getArena().tick(Particle.HEART, Sound.BLOCK_WOOD_BREAK);

    }


    private void updateBlocks(){
        Material type;
        switch(this.state){
            case EMPTY:
                type = Material.DIAMOND_BLOCK;
                getArena().changeBorderType(type);
                break;
            case CAPTURING:
                type = Material.EMERALD_BLOCK;
                getArena().changeBorderType(type);
                break;
            case CONTESTED:
                type = Material.GOLD_BLOCK;
                getArena().changeBorderType(type);
                break;
            case OFF:
                type = Material.REDSTONE_BLOCK;
                getArena().changeBorderType(type);
                break;
        }

    }

    public enum GameState{
        CONTESTED,
        CAPTURING,
        EMPTY,
        OFF;
    }


}
