package me.gmx.purplekoth.util;

import me.gmx.purplekoth.PurpleKoth;
import me.gmx.purplekoth.objects.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static void copy(InputStream input, File file){

        try{
            FileOutputStream output = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int i;
            while ((i = input.read(b)) > 0) {
                output.write(b,0,i);
            }
            output.close();
            input.close();


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void mkDir(File file){
        try{
            file.mkdir();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void test(List<ItemStack> list){
        resetLoot();
        FileConfiguration conf = PurpleKoth.getInstance().getLootConfig();
        for (int i = 0;i < list.size();i++){
            if (list.get(i) != null)
            conf.set(String.valueOf(i),list.get(i).serialize());
        }
        PurpleKoth.getInstance().saveLootConfig();
    }

    private static void resetLoot(){
        for(String key : PurpleKoth.getInstance().getLootConfig().getKeys(false)){
            PurpleKoth.getInstance().getLootConfig().set(key,null);
        }
        PurpleKoth.getInstance().saveLootConfig();


    }


    public static List<ItemStack> get(){
        List<ItemStack> list = new ArrayList<ItemStack>();
        FileConfiguration conf = PurpleKoth.getInstance().getLootConfig();
        PurpleKoth.getInstance().saveLootConfig();
        for (String o : PurpleKoth.getInstance().getLootConfig().getKeys(false)){
            if (conf.get(o) != null){
                try {
                    list.add(ItemStack.deserialize((Map<String, Object>) conf.get(o))); //line 73
                }catch(ClassCastException e) {
                    MemorySection sec = (MemorySection) conf.get(o);
                    list.add(ItemStack.deserialize((Map<String, Object>) sec.getValues(false)));
                }
            }
        }
        return list;
    }

    public static void storeItemList(List<ItemStack> list){
        try (BukkitObjectOutputStream output = new BukkitObjectOutputStream(new FileOutputStream(PurpleKoth.getInstance().lootFile))) {
            output.writeInt(list.size());
            for (ItemStack stack : list){
                output.writeObject(stack);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static List<ItemStack> getItemList(){
        List<ItemStack> list = new ArrayList<ItemStack>();
        try (BukkitObjectInputStream input = new BukkitObjectInputStream(new FileInputStream(PurpleKoth.getInstance().lootFile))) {
            final int size = input.readInt();
            for (int i = 0;i<=size;i++){
                list.add((ItemStack) input.readObject());
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return list;
    }

    public static void writeArena(Arena a){
        if (!sectionExists(a.getName())){
            PurpleKoth.getInstance().getArenaConfig().createSection(a.getName());
        }
        ConfigurationSection as = PurpleKoth.getInstance().getArenaConfig().getConfigurationSection(a.getName());
        as.set("world",a.getWorld().getName());
        as.set("min-x",a.getBotLeft().getBlockX());
        as.set("min-y",a.getBotLeft().getBlockY());
        as.set("min-z",a.getBotLeft().getBlockZ());

        as.set("max-x",a.getTopRight().getBlockX());
        as.set("max-y",a.getTopRight().getBlockY());
        as.set("max-z",a.getTopRight().getBlockZ());
        PurpleKoth.getInstance().saveArenaConfig();


    }
    public static boolean sectionExists(String s){
        if (!PurpleKoth.getInstance().getArenaConfig().isConfigurationSection(s) || PurpleKoth.getInstance().getArenaConfig().getConfigurationSection(s) == null){
            return false;
        }
        return true;
    }
    public static boolean sectionExistsLoot(String s){
        if (!PurpleKoth.getInstance().getLootConfig().isConfigurationSection(s) || PurpleKoth.getInstance().getLootConfig().getConfigurationSection(s) == null){
            return false;
        }
        return true;
    }
    public static void removeArena(Arena a)throws NullPointerException{
        if (sectionExists(a.getName())){
            PurpleKoth.getInstance().getArenaConfig().set(a.getName(),null);


        }else{
            throw new NullPointerException("Arena could not be found in config");
        }
    }





}
