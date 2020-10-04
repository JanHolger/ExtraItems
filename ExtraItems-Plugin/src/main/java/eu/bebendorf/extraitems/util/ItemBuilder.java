package eu.bebendorf.extraitems.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class ItemBuilder {

    private ItemStack item;

    public ItemBuilder(Material material){
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount){
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(ItemStack item){
        this.item = item;
    }

    public ItemBuilder type(Material material){
        item.setType(material);
        return null;
    }

    public ItemBuilder amount(int amount){
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder leatherColor(Color color){
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder name(String name){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... lore){
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder unbreakable(){
        return unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean unbreakable){
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level){
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder bookPages(String... pages){
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setPages(pages);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder bookTitle(String title){
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setTitle(title);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder bookAuthor(String author){
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor(author);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder bookGeneration(BookMeta.Generation generation){
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setGeneration(generation);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder potionType(PotionType type, boolean extended, boolean upgraded){
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionData(new PotionData(type, extended, upgraded));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder potionColor(Color color){
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder flag(ItemFlag... flags){
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flags);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder customModel(Integer customModelData){
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder skullOwner(String owner){
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder skullOwner(UUID owner){
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder modifier(String name, Attribute attribute, double amount){
        return modifier(name, attribute, amount, AttributeModifier.Operation.ADD_NUMBER);
    }

    public ItemBuilder modifier(String name, Attribute attribute, double amount, AttributeModifier.Operation operation){
        ItemMeta meta = item.getItemMeta();
        meta.addAttributeModifier(attribute, new AttributeModifier(name, amount, operation));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder modifier(String name, Attribute attribute, double amount, EquipmentSlot slot){
        return modifier(name, attribute, amount, AttributeModifier.Operation.ADD_NUMBER, slot);
    }

    public ItemBuilder modifier(String name, Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot){
        ItemMeta meta = item.getItemMeta();
        meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), name, amount, operation, slot));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder skullTexture(String url){
        GameProfile profile = new GameProfile(UUID.randomUUID(), "Skull");
        JsonObject object = new JsonObject();
        object.addProperty("timestamp", System.currentTimeMillis());
        object.addProperty("profileId", profile.getId().toString());
        object.addProperty("profileName", profile.getName());
        JsonObject textures = new JsonObject();
        JsonObject skin = new JsonObject();
        skin.addProperty("url", url);
        textures.add("SKIN", skin);
        object.add("textures", textures);
        profile.getProperties().put("textures", new Property("textures", new String(Base64.getEncoder().encode(new GsonBuilder().disableHtmlEscaping().create().toJson(object).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)));
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        Reflection.getCBClass("inventory.CraftMetaSkull").getMethod("setProfile", GameProfile.class).call(meta, profile);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder persistentData(NamespacedKey key, String value){
        return persistentData(key, PersistentDataType.STRING, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, Integer value){
        return persistentData(key, PersistentDataType.INTEGER, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, Long value){
        return persistentData(key, PersistentDataType.LONG, value);
    }

    public ItemBuilder persistentData(NamespacedKey key, Double value){
        return persistentData(key, PersistentDataType.DOUBLE, value);
    }

    public <T> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<T, T> type, T value){
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key, type, value);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack build(){
        return item;
    }

}
