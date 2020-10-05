package eu.bebendorf.extraitems.api;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ExtraItemsAPI {

    static ExtraItemsAPI getAPI(){
        return (ExtraItemsAPI) Bukkit.getPluginManager().getPlugin("ExtraItems");
    }

    List<ExtraItem> getItems();
    ExtraItem getItem(String id);
    ExtraItem getItem(ItemStack item);
    boolean doesItemExist(String id);
    ExtraItem registerItem(String id, Material material, String name);
    ExtraItem registerItem(String id, Material material, String name, Integer customModelData);
    ExtraItem registerItem(String id, Material material, String name, List<ExtraItemModifier> modifiers);
    ExtraItem registerItem(String id, Material material, String name, Integer customModelData, Integer leatherColor, List<ExtraItemModifier> modifiers);
    void deleteItem(String id);
    void reloadItems();

}
