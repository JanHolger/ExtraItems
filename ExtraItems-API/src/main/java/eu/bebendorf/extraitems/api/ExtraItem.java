package eu.bebendorf.extraitems.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ExtraItem {

    String getId();
    String getName();
    Material getMaterial();
    Integer getCustomModelData();
    Integer getLeatherColor();
    List<ExtraItemModifier> getModifiers();
    ItemStack create(int amount);
    boolean isItem(ItemStack stack);

}
