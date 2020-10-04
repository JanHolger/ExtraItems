package eu.bebendorf.extraitems.util;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Locale;

public class ItemHelper {

    public static boolean isArmor(Material material){
        return isHelmet(material) || isChestplate(material) || isLeggings(material) || isBoots(material);
    }

    public static boolean isHelmet(Material material){
        String name = material.name().toLowerCase(Locale.ROOT);
        if(name.endsWith("HELMET"))
            return true;
        if(name.endsWith("SKULL"))
            return true;
        if(name.endsWith("HEAD"))
            return true;
        if(material == Material.JACK_O_LANTERN)
            return true;
        return false;
    }

    public static boolean isBoots(Material material){
        String name = material.name().toLowerCase(Locale.ROOT);
        if(name.endsWith("BOOTS"))
            return true;
        return false;
    }

    public static boolean isLeggings(Material material){
        String name = material.name().toLowerCase(Locale.ROOT);
        if(name.endsWith("LEGGINGS"))
            return true;
        return false;
    }

    public static boolean isChestplate(Material material){
        String name = material.name().toLowerCase(Locale.ROOT);
        if(name.endsWith("CHESTPLATE"))
            return true;
        if(material == Material.ELYTRA)
            return true;
        return false;
    }

    public static EquipmentSlot getEffectiveSlot(Material material){
        if(isHelmet(material))
            return EquipmentSlot.HEAD;
        if(isChestplate(material))
            return EquipmentSlot.CHEST;
        if(isLeggings(material))
            return EquipmentSlot.LEGS;
        if(isBoots(material))
            return EquipmentSlot.FEET;
        return EquipmentSlot.HAND;
    }

}
