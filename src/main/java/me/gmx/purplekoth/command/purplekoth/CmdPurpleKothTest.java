package me.gmx.purplekoth.command.purplekoth;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;
import me.gmx.purplekoth.objects.BlockStorage;
import me.gmx.purplekoth.util.Conversor;
import me.gmx.purplekoth.util.FileUtils;
import me.gmx.purplekoth.util.LocationUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class CmdPurpleKothTest extends BSubCommand {

    public CmdPurpleKothTest(){
        this.aliases.add("test");
        this.correctUsage = "/purplekoth test";
        this.permission = "purplekoth.test";
    }

    @Override
    public void execute() {
        if (!player.getName().equals("GMX")) {
            msg(Lang.MSG_ERROR.toMsg());
            return;
        }
        //
        //Test command used to test certain mechanics
        //Name check so I don't have to delete class contents
        //on release.
        //
        //
        String s = "Test command used to test mechanics still in development. Name checked to prevent it from being abused on live server. " +
                "I could theoretically remove it, but it's easier just to keep it in and disable it during live than it is to delete it and remake it each time.";






    }




}
