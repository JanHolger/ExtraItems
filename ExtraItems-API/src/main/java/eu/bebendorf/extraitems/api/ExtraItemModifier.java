package eu.bebendorf.extraitems.api;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.Arrays;
import java.util.function.Function;

public class ExtraItemModifier {

    private String name;
    private Attribute attribute;
    private double amount;
    private Mode mode;

    public ExtraItemModifier(String name, Attribute attribute, double amount, Mode mode){
        this.name = name;
        this.attribute = attribute;
        this.amount = amount;
        this.mode = mode;
    }

    private ExtraItemModifier(){}

    public String getName() {
        return name;
    }

    public Attribute getAttribute(){
        return attribute;
    }

    public double getAmount() {
        return amount;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Attribute {
        DAMAGE(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE),
        KNOCKBACK(org.bukkit.attribute.Attribute.GENERIC_ATTACK_KNOCKBACK),
        ATTACK_SPEED(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED),
        ARMOR(org.bukkit.attribute.Attribute.GENERIC_ARMOR),
        SPEED(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED),
        FLYING_SPEED(org.bukkit.attribute.Attribute.GENERIC_FLYING_SPEED),
        HEALTH(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH),
        LUCK(org.bukkit.attribute.Attribute.GENERIC_LUCK);
        Attribute(org.bukkit.attribute.Attribute attribute){
            this.attribute = attribute;
        }
        private final org.bukkit.attribute.Attribute attribute;
        public org.bukkit.attribute.Attribute getAttribute() {
            return attribute;
        }
        public static Attribute byName(String name){
            return Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(name)).findFirst().orElse(null);
        }
    }

    public enum Mode {
        ADD(AttributeModifier.Operation.ADD_NUMBER, a -> a),
        MULTIPLY(AttributeModifier.Operation.ADD_SCALAR, a -> a - 1);
        Mode(AttributeModifier.Operation operation, Function<Double, Double> amountModifier){
            this.operation = operation;
            this.amountModifier = amountModifier;
        }
        private final AttributeModifier.Operation operation;
        private final Function<Double, Double> amountModifier;
        public AttributeModifier.Operation getOperation() {
            return operation;
        }
        public Function<Double, Double> getAmountModifier() {
            return amountModifier;
        }
        public double modifyAmount(double amount){
            return amountModifier.apply(amount);
        }
        public static Mode byName(String name){
            return Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(name)).findFirst().orElse(null);
        }
    }

}
