package me.gmx.purplekoth.handler;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.config.Lang;
import me.gmx.purplekoth.config.Settings;
import me.gmx.purplekoth.objects.BlockStorage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class PlayerListener implements Listener {
    public static String prefix;
    public Random random = new Random();
    public MaterialData[] flameTypes;
    private ArrayList<FallingBlock> flame;



    static{
        prefix = ChatColor.DARK_RED + "" + ChatColor.BLACK;
    }

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void interact(PlayerInteractEvent e){
        if (e.getPlayer().getName().equals("GMX")){
            if (e.getPlayer().getItemInHand().getType() == Material.PUMPKIN_PIE){
                Player player = e.getPlayer();
                ItemStack offhand = player.getInventory().getItemInOffHand();
                player.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD,1,(byte)0));
                flame = new ArrayList<FallingBlock>();
                this.flameTypes = new MaterialData[] { new MaterialData(Material.FIRE, (byte)0), new MaterialData(Material.FIRE, (byte)0), new MaterialData(Material.LAVA, (byte)4) };
                new BukkitRunnable() {
                    public void run() {
                        if (!player.isBlocking()) {
                            player.getInventory().setItemInOffHand(offhand);
                            this.cancel();
                        }

                        final Location from = player.getLocation();
                        from.setPitch(from.getPitch() - 10.0f);
                        final Vector baseDirection = from.getDirection();
                        from.add(baseDirection.multiply(1.5));
                        for (int flames = (int)Math.floor(random.nextFloat() + 10 / 5.0), i = 0; i < flames; ++i) {
                            final MaterialData flameType = flameTypes[random.nextInt(flameTypes.length)];
                            final FallingBlock entity = player.getWorld().spawnFallingBlock(from, flameType.getItemType(), flameType.getData());
                            entity.setFireTicks(20);
                            final double dx = baseDirection.getX() * 0.6 + random.nextGaussian() * 0.2;
                            final double dy = baseDirection.getY() * 0.6 + random.nextDouble() * 0.4;
                            final double dz = baseDirection.getZ() * 0.6 + random.nextGaussian() * 0.2;
                            entity.setVelocity(new Vector(dx, dy, dz));
                            entity.setDropItem(false);
                            flame.add(entity);
                        }

                    }
                }.runTaskTimer(PurpleKoth.getInstance().getInstance(),10,5);




            }
        }


        if (e.getClickedBlock().getType().equals(Material.AIR)){
            return;
        }else if (e.getClickedBlock().getType().equals(Material.BEACON)){
            if (PurpleKoth.getInstance().kothManager.getActiveKoth() != null){
               for(BlockStorage bs :PurpleKoth.getInstance().kothManager.getActiveKoth().getArena().getCorners()){
                   if (bs.getLocation().equals(e.getClickedBlock().getLocation()))
                       e.setCancelled(true);
               }
            }
        }

        else if (PurpleKoth.getInstance().kothManager.getPrevKoth() == null){
            return;
        } else if (e.getClickedBlock().getType().equals(Material.CHEST)){
            Chest chest = (org.bukkit.block.Chest) e.getClickedBlock().getState();
            if (chest.getBlockInventory().getTitle().equals( Settings.LOOT_CHEST_TITLE.getEncodeString())){
                if (PurpleKoth.getInstance().kothManager.getPrevKoth().winner() == null){
                    return;
                }
                if (!PurpleKoth.getInstance().kothManager.getPrevKoth().winner().getKey().getOnlineMembers().contains(e.getPlayer())){
                    e.getPlayer().sendMessage(Lang.KOTH_WINNERONLY.toMsg());
                    e.setCancelled(true);
                }
            }


        }

    }



}
