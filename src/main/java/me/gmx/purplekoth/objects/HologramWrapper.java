package me.gmx.purplekoth.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class HologramWrapper {
    private Hologram hologram;
    private Koth koth;
    private TextLine top,mid,mid2,bot;
    private String tops,mids,mid2s,bots;
    public HologramWrapper(Koth k){
        this.koth = k;
        this.hologram = HologramsAPI.createHologram(PurpleKoth.getInstance(),k.getArena().getMiddle().add(0.5,4,0.5));
        hologram.getVisibilityManager().setVisibleByDefault(true);
        tops = Lang.KOTH_HOLOGRAM_TOP.toString();
        mids = Lang.KOTH_HOLOGRAM_MID.toString();
        mid2s = Lang.KOTH_HOLOGRAM_MID2.toString();
        bots = Lang.KOTH_HOLOGRAM_BOT.toString();
         top = hologram.appendTextLine(hologramText(tops));
         mid = hologram.appendTextLine(hologramText(mids));
         mid2 = hologram.appendTextLine(hologramText(mid2s));
         bot = hologram.appendTextLine(hologramText(bots));

    }

    public void init(){
        //hologram.clearLines();

    }

    private String hologramText(String s){
        String str;
        if (koth.getState() == Koth.GameState.CAPTURING){
            try {
                    str = s.replace("%gang%",koth.getActiveGangs().get(0).getName()).
                            replace("%gangtime%", String.valueOf(koth.getContestants().get(koth.getActiveGangs().get(0))));


            }catch (Exception e){
                str = s.replace("%gang%",koth.getActiveGangs().get(0).getName()).
                        replace("%gangtime%","");
            }
        }else if (koth.getState() == Koth.GameState.CONTESTED){
                str = s.replace("%gang%","Contested").
                        replace("%gangtime%", "");
        }else{
            str = s.replace("%gang%","").
                    replace("%gangtime%", "");
        }
        try{
            String stri = str.replace("%timeleft%",String.valueOf(koth.getTimeLeft()))
                    .replace("%winner%",koth.winner().getKey().getName());
            return stri;

        }catch(Exception e){
            String stri = str.replace("%timeleft%",String.valueOf(koth.getTimeLeft()))
                    .replace("%winner%","");
            return stri;

        }
    }

    public void update(){
        top.setText(hologramText(tops));
        mid.setText(hologramText(mids));
        mid2.setText(hologramText(mid2s));
        bot.setText(hologramText(bots));
    }

    public void delete(){
        this.hologram.delete();
    }
}
