package eu.bebendorf.extraitems;

import eu.bebendorf.extraitems.api.ExtraItem;
import eu.bebendorf.extraitems.api.ExtraItemModifier;
import eu.bebendorf.extraitems.util.ItemBuilder;
import eu.bebendorf.extraitems.util.ItemHelper;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SimpleExtraItem implements ExtraItem {

    private static final NamespacedKey TYPE_ID_KEY = new NamespacedKey("extraitems", "type_id");

    private String id;
    private String name;
    private Material material;
    private Integer customModelData;
    private Integer leatherColor;
    private List<ExtraItemModifier> modifiers;

    public SimpleExtraItem(String id, String name, Material material, Integer customModelData, Integer leatherColor, List<ExtraItemModifier> modifiers){
        this.id = id;
        this.name = name;
        this.material = material;
        this.customModelData = customModelData;
        this.leatherColor = leatherColor;
        this.modifiers = modifiers;
    }

    private SimpleExtraItem(){}

    public Integer getLeatherColor() {
        return leatherColor;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public List<ExtraItemModifier> getModifiers() {
        return modifiers;
    }

    public ItemStack create(int amount) {
        ItemBuilder builder = new ItemBuilder(material, amount)
                .customModel(customModelData)
                .flag(ItemFlag.HIDE_ATTRIBUTES)
                .name("§r"+name);
        if(leatherColor != null)
            builder.leatherColor(Color.fromRGB(leatherColor));
        modifiers.forEach(m -> builder.modifier(m.getName(), m.getAttribute().getAttribute(), m.getMode().modifyAmount(m.getAmount()), m.getMode().getOperation(), ItemHelper.getEffectiveSlot(material)));
        return builder.build();
    }

    public boolean isItem(ItemStack stack) {
        if(stack == null)
            return false;
        if(stack.getType() != material)
            return false;
        if(!stack.hasItemMeta())
            return false;
        if(!stack.getItemMeta().getPersistentDataContainer().has(TYPE_ID_KEY, PersistentDataType.STRING))
            return false;
        return stack.getItemMeta().getPersistentDataContainer().get(TYPE_ID_KEY, PersistentDataType.STRING).equals(id);
    }

}
