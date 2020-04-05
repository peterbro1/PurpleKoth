package me.gmx.purplekoth.hook;

import net.brcdev.gangs.GangsPlugin;
import net.brcdev.gangs.gang.Gang;
import org.bukkit.entity.Player;

public class GangsHook {
    private GangsPlugin gangs;

    public GangsHook(){
        this.gangs = GangsPlugin.getInstance();
    }


    public  boolean sameGang(Player p1, Player p2){
        if (gangs.gangManager.getPlayersGang(p1).equals(gangs.gangManager.getPlayersGang(p2))){
            return true;
        }
        return false;
    }

    public Gang getPlayerGang(Player p){
        if (gangs == null){
            System.out.println("GANGS OBJECT NULL");
            return null;
        }
        if (gangs.gangManager == null){
            System.out.println("GANGMANAGER OBJECT NULL");
            return null;
        }
        return gangs.gangManager.getPlayersGang(p);
    }




}
