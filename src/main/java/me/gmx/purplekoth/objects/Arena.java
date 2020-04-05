package me.gmx.purplekoth.objects;


import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.util.LocationUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private String name;
    private Location botLeft;
    private Location topRight;
    private World world;
    private List<BlockStorage> setEmeralds;



    public Arena(String name, Location botLeft, Location topRight){
        this.name = name;
        this.world = botLeft.getWorld();
        this.botLeft = botLeft;
        this.topRight = topRight;
        this.setEmeralds = new ArrayList<BlockStorage>();
        for (BlockStorage blst : getCorners()){
            setEmeralds.add(new BlockStorage(blst.getLocation().clone().subtract(0,1,0),blst.getLocation().clone().subtract(0,1,0).getBlock().getState()));
            for (BlockStorage b : LocationUtil.getSquareAround(blst.getLocation().clone().subtract(0,1,0),1)){
                setEmeralds.add(b);
            }
        }
    }

    public List<BlockStorage> getSetEmeralds(){
        return setEmeralds;
    }
    /*
     *@param b - Bottom left location
     * @param t - Top right location
     */
    public void setArea(Location b, Location t){
        this.botLeft = b;
        this.topRight = t;
    }

    public Location getBotLeft(){
        return this.botLeft;
    }
    public Location getTopRight(){
        return this.topRight;
    }


    public boolean isInsideArena(Location loc){
        return botLeft.getBlockX() < loc.getBlockX() && topRight.getBlockX() > loc.getBlockX() &&
                        botLeft.getBlockY() <= loc.getBlockY() && topRight.getBlockY() >= loc.getBlockY() &&
                        botLeft.getBlockZ() < loc.getBlockZ() && topRight.getBlockZ() > loc.getBlockZ();
    }

    public boolean isInArea(OfflinePlayer oPlayer) {
        if(oPlayer == null || !oPlayer.isOnline() || oPlayer.getPlayer() == null) {
            return false;
        }
        Player player = oPlayer.getPlayer();

        if (player.isDead()) {
            return false;
        }

        if (player.getWorld() != botLeft.getWorld()) {
            return false;
        }
        return isInsideArena(player.getLocation());
    }

    public static Arena loadFromConfig(String id)throws NullPointerException{
        if (id == null || id.isEmpty() || !me.gmx.purplekoth.util.FileUtils.sectionExists(id)){
            throw new NullPointerException("Could not find arena " + id + " in config!");
        }
        Location botLeft, topRight;
        Material blockType;
        int radius;
        try{
            ConfigurationSection sec = PurpleKoth.getInstance().getArenaConfig().getConfigurationSection(id);
            botLeft = new Location(Bukkit.getWorld(sec.getString("world")),sec.getInt("min-x"),sec.getInt("min-y"),sec.getInt("min-z"));
            topRight = new Location(Bukkit.getWorld(sec.getString("world")),sec.getInt("max-x"),sec.getInt("max-y"),sec.getInt("max-z"));
        }catch (Exception ex){
            throw new NullPointerException("Unable to grab locations from config!");
        }
       // BlockVector bl = new BlockVector(botLeft.getBlockX(),botLeft.getBlockY(),botLeft.getBlockZ());
       // BlockVector tr = new BlockVector(topRight.getBlockX(),topRight.getBlockY(),topRight.getBlockZ());
        return new Arena(id,botLeft,topRight);
    }
    public World getWorld(){
        return world;
    }

    public String getName(){
        return name;
    }

    public Location getMiddle() {
        Location t = LocationUtil.getMiddleBlock(botLeft, topRight);
        return new Location(t.getWorld(),t.getX(),t.getWorld().getHighestBlockAt(t).getLocation().add(0,0,0).getY(),t.getZ());
    }

    public void tick(Particle particle, Sound sound){
        getWorld().playSound(getMiddle(),sound,1,1);
        getWorld().spawnParticle(particle,getMiddle().add(0.5,2,0.5),2);
    }
    public void changeBorderType(Material type){
        for (Block b : LocationUtil.getBorders(getBotLeft(),getTopRight())){
            if (sameXY(b.getLocation(),getBotLeft()) || sameXY(b.getLocation(),getTopRight())
                    || (b.getLocation().getBlockX() == getTopRight().getBlockX() && b.getLocation().getBlockZ() == getBotLeft().getBlockZ())
                    || (b.getLocation().getBlockZ() == getTopRight().getBlockZ() && b.getLocation().getBlockX() == getBotLeft().getBlockX())){ //4 corners
                b.setType(Material.BEACON);
            }else
            b.setType(type);
        }
        for (BlockStorage blst : setEmeralds){
            blst.getLocation().getBlock().setType(type);
        }


    }
    public List<BlockStorage> getCorners(){
        List<BlockStorage> store = new ArrayList<BlockStorage>();
        for (Block b : LocationUtil.getBorders(getBotLeft(),getTopRight())){
            if (sameXY(b.getLocation(),getBotLeft()) || sameXY(b.getLocation(),getTopRight())
                    || (b.getLocation().getBlockX() == getTopRight().getBlockX() && b.getLocation().getBlockZ() == getBotLeft().getBlockZ())
                    || (b.getLocation().getBlockZ() == getTopRight().getBlockZ() && b.getLocation().getBlockX() == getBotLeft().getBlockX())){ //4 corners
                store.add(new BlockStorage(b.getLocation(),b.getState()));
            }
        }
        return store;
    }
    private boolean sameXY(Location loc1, Location loc2){
        if (
                loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockZ() == loc2.getBlockZ()
        )
            return true;
        else
            return false;
    }
}
