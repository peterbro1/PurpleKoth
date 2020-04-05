package me.gmx.purplekoth.objects;

import com.destroystokyo.paper.loottable.Lootable;
import me.gmx.purplekoth.config.Settings;
import me.gmx.purplekoth.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootChest  {

    private Inventory inv;
    private List<ItemStack> toAdd;
    public LootChest(Chest chest){
        List<ItemStack> loot = FileUtils.get();
        if (loot.isEmpty()){return;}
        toAdd = new ArrayList<ItemStack>();

        for (int i = 0;i<= Settings.LOOT_CHEST_ROLLS.getNumber();i++){
            int randomInt = (int)(loot.size() * Math.random());
            toAdd.add(loot.get(randomInt));
        }
    }

    public void fillChest(Inventory chest){
        for (ItemStack stack : toAdd){
            chest.addItem(stack);
        }

    }
}
