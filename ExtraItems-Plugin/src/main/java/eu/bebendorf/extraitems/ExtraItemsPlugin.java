package eu.bebendorf.extraitems;

import eu.bebendorf.extraitems.api.ExtraItem;
import eu.bebendorf.extraitems.api.ExtraItemModifier;
import eu.bebendorf.extraitems.api.ExtraItemsAPI;
import eu.bebendorf.extraitems.command.ExtraItemsCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExtraItemsPlugin extends JavaPlugin implements ExtraItemsAPI {

    public static ExtraItemsPlugin INSTANCE;
    private final Map<String, ExtraItem> items = new HashMap<>();
    private ExtraItemsConfig config;

    public void onEnable(){
        INSTANCE = this;
        reloadItems();
        new ExtraItemsCommand(Objects.requireNonNull(getCommand("extraitems")));
    }

    public List<ExtraItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public ExtraItem getItem(String id) {
        return items.get(id);
    }

    public ExtraItem getItem(ItemStack item) {
        return items.values().stream().filter(i -> i.isItem(item)).findFirst().orElse(null);
    }

    public boolean doesItemExist(String id){
        return items.containsKey(id);
    }

    public ExtraItem registerItem(String id, Material material, String name) {
        return registerItem(id, material, name, null, new ArrayList<>());
    }

    public ExtraItem registerItem(String id, Material material, String name, Integer customModelData) {
        return registerItem(id, material, name, customModelData, new ArrayList<>());
    }

    public ExtraItem registerItem(String id, Material material, String name, List<ExtraItemModifier> modifiers) {
        return registerItem(id, material, name, null, modifiers);
    }

    public ExtraItem registerItem(String id, Material material, String name, Integer customModelData, List<ExtraItemModifier> modifiers) {
        if(items.containsKey(id))
            return items.get(id);
        SimpleExtraItem item = new SimpleExtraItem(id, name, material, customModelData, modifiers);
        SimpleExtraItem[] newItems = new SimpleExtraItem[config.getItems().length+1];
        for(int i=0; i<config.getItems().length; i++)
            newItems[i] = config.getItems()[i];
        newItems[newItems.length-1] = item;
        config.setItems(newItems);
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        items.put(id, item);
        return item;
    }

    public void deleteItem(String id){
        items.remove(id);
        SimpleExtraItem[] items = new SimpleExtraItem[config.getItems().length-1];
        for(int i=0, j=0; i<config.getItems().length; i++){
            if(config.getItems()[i].getId().equalsIgnoreCase(id))
                continue;
            items[j] = config.getItems()[i];
            j++;
        }
        config.setItems(items);
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadItems(){
        if(!getDataFolder().exists())
            getDataFolder().mkdir();
        try {
            config = ExtraItemsConfig.load(new File(getDataFolder(), "items.json"));
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
        for(ExtraItem item : config.getItems())
            items.put(item.getId(), item);
    }

}
