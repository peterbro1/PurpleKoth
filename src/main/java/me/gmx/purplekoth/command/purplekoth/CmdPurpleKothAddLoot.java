package me.gmx.purplekoth.command.purplekoth;

import com.google.common.collect.ImmutableList;
import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.core.BSubCommand;
import me.gmx.purplekoth.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdPurpleKothAddLoot extends BSubCommand implements Listener {

    public static String prefix = ChatColor.GREEN + "" + ChatColor.BLACK + ChatColor.GREEN + "Loot";
    public CmdPurpleKothAddLoot(){
        this.aliases.add("addloot");
        this.aliases.add("loot");
        this.correctUsage = "/purplekoth loot";
        this.permission = "purplekoth.loot";
        Bukkit.getPluginManager().registerEvents(this,PurpleKoth.getInstance());
    }

    @Override
    public void execute() {

       // msg(Lang.MSG_PKOTH_HELP.toMsg());
        ItemStack stack = player.getInventory().getItemInMainHand();
        Inventory inv = Bukkit.createInventory(null,54, prefix);
        try {
            for (ItemStack i : FileUtils.get()){
                inv.addItem(i);
            }
        }catch (Exception e){

        }
        player.openInventory(inv);
        //FileUtils.storeItemList(ImmutableList.of(stack));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (e.getInventory().getTitle().equals(prefix)){
            List<ItemStack> list = new ArrayList<ItemStack>();
            for (ItemStack stack : e.getInventory().getContents()){
                if (stack != null)
                for (int i = 0;i<stack.getAmount();i++){
                    ItemStack clone = stack.clone();
                    clone.setAmount(1);
                    list.add(clone);
                }
            }
            FileUtils.test(list);
        }

    }
}
