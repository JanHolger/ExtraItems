package eu.bebendorf.extraitems.command;

import eu.bebendorf.extraitems.ExtraItemsPlugin;
import eu.bebendorf.extraitems.api.ExtraItem;
import eu.bebendorf.extraitems.api.ExtraItemModifier;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ExtraItemsCommand implements CommandExecutor, TabCompleter, Listener {

    private Map<CommandSender, Bluprint> bluprints = new HashMap<>();

    public ExtraItemsCommand(PluginCommand command){
        command.setExecutor(this);
        command.setTabCompleter(this);
        Bukkit.getPluginManager().registerEvents(this, command.getPlugin());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        bluprints.remove(e.getPlayer());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!sender.hasPermission("extraitems.give")) // just to be sure, that the permissions check works
            return true;
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("give")){
                if(args.length == 3 || args.length == 4){
                    Player other = Bukkit.getPlayer(args[1]);
                    if(other == null){
                        sender.sendMessage("§cDer Spieler wurde nicht gefunden!");
                        return true;
                    }
                    ExtraItem item = ExtraItemsPlugin.INSTANCE.getItem(args[2]);
                    if(item == null){
                        sender.sendMessage("§cDas Item wurde nicht gefunden!");
                        return true;
                    }
                    int amount = args.length == 4 ? Integer.parseInt(args[3]) : 1;
                    other.getInventory().addItem(item.create(amount));
                    sender.sendMessage("§aGebe "+other.getName()+" "+amount+"x "+item.getName()+"!");
                    return true;
                }
                sender.sendMessage("§cSyntax§8: §e/extraitems give <spieler> <item> [anzahl]");
                return true;
            }
            if(args[0].equalsIgnoreCase("list")){
                sender.sendMessage("§aItems§8: §7"+ExtraItemsPlugin.INSTANCE.getItems().stream().map(ExtraItem::getId).collect(Collectors.joining("§8, §7")));
                return true;
            }
            if(args[0].equalsIgnoreCase("reload")){
                ExtraItemsPlugin.INSTANCE.reloadItems();
                sender.sendMessage("§aItems neu geladen!");
                return true;
            }
            if(args[0].equalsIgnoreCase("delete")){
                if(args.length == 2){
                    if(!ExtraItemsPlugin.INSTANCE.doesItemExist(args[1])){
                        sender.sendMessage("§cDas Item wurde nicht gefunden!");
                        return true;
                    }
                    ExtraItemsPlugin.INSTANCE.deleteItem(args[1]);
                    sender.sendMessage("§aDas Item wurde gelöscht!");
                    return true;
                }
                sender.sendMessage("§cSyntax§8: §e/extraitems delete <item>");
                return true;
            }
            if(args[0].equalsIgnoreCase("cancel")){
                if(bluprints.containsKey(sender)){
                    bluprints.remove(sender);
                    sender.sendMessage("§aErstellung abgebrochen!");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("custommodel")){
                if(bluprints.containsKey(sender)){
                    if(args.length == 2){
                        bluprints.get(sender).customModelData = Integer.parseInt(args[1]);
                        sender.sendMessage("§aEigenes Modell gesetzt!");
                        return true;
                    }
                    sender.sendMessage("§cSyntax§8: §e/extraitems custommodel <id>");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("leather")){
                if(bluprints.containsKey(sender)){
                    if(args.length == 4){
                        Color color = Color.fromRGB(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        bluprints.get(sender).leatherColor = color.asRGB();
                        sender.sendMessage("§aLederfarbe gesetzt!");
                        return true;
                    }
                    sender.sendMessage("§cSyntax§8: §e/extraitems leather <red> <green> <blue>");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("modifier")){
                if(bluprints.containsKey(sender)){
                    if(args.length == 5){
                        Bluprint bluprint = bluprints.get(sender);
                        if(bluprint.hasModifier(args[1])){
                            sender.sendMessage("§cEs existiert bereits ein Modifier mit diesem Namen!");
                            return true;
                        }
                        ExtraItemModifier.Attribute attribute = ExtraItemModifier.Attribute.byName(args[2]);
                        if(attribute == null){
                            sender.sendMessage("§cDas Attribut existiert nicht!");
                            return true;
                        }
                        ExtraItemModifier.Mode mode = ExtraItemModifier.Mode.byName(args[3]);
                        if(mode == null){
                            sender.sendMessage("§cDer Modus existiert nicht!");
                            return true;
                        }
                        double amount = Double.parseDouble(args[4]);
                        bluprint.modifiers.add(new ExtraItemModifier(args[1], attribute, amount, mode));
                        sender.sendMessage("§aModifier §8'§7"+args[1]+"§8'§a hinzugefügt!");
                        return true;
                    }
                    sender.sendMessage("§cSyntax§8: §e/extraitems modifier <name> <attribute> <mode> <amount>");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("create")){
                if(!bluprints.containsKey(sender)){
                    if(args.length >= 4){
                        if(ExtraItemsPlugin.INSTANCE.doesItemExist(args[1])){
                            sender.sendMessage("§cEs existiert bereits ein Item mit dieser ID!");
                            return true;
                        }
                        Material material = Material.getMaterial(args[2]);
                        if(material == null){
                            sender.sendMessage("§cDas Material existiert nicht!");
                            return true;
                        }
                        Bluprint bluprint = new Bluprint(args[1], material, String.join(" ", Arrays.copyOfRange(args, 3, args.length)));
                        bluprints.put(sender, bluprint);
                        sender.sendMessage("§aErstellung begonnen!");
                        sender.sendMessage("§bBenutze folgende Befehle zur Erstellung§8:");
                        sender.sendMessage("§e/extraitems cancel");
                        sender.sendMessage("§e/extraitems custommodel <id>");
                        sender.sendMessage("§e/extraitems leather <red> <green> <blue>");
                        sender.sendMessage("§e/extraitems modifier <name> <attribute> <mode> <amount>");
                        sender.sendMessage("§e/extraitems create");
                    }else{
                        sender.sendMessage("§cSyntax§8: §e/extraitems create <id> <material> <name>");
                    }
                }else{
                    Bluprint bluprint = bluprints.get(sender);
                    bluprints.remove(sender);
                    ExtraItem item = ExtraItemsPlugin.INSTANCE.registerItem(bluprint.id, bluprint.material, bluprint.name, bluprint.customModelData, bluprint.leatherColor, bluprint.modifiers);
                    sender.sendMessage("§aNeues Item §8'§7"+item.getId()+"§8'§a erstellt!");
                }
                return true;
            }
        }
        sender.sendMessage("§bBefehle§8:");
        sender.sendMessage("§e/extraitems give <spieler> <item> [anzahl]");
        sender.sendMessage("§e/extraitems list");
        sender.sendMessage("§e/extraitems reload");
        sender.sendMessage("§e/extraitems delete <item>");
        if(bluprints.containsKey(sender)){
            sender.sendMessage("§e/extraitems cancel");
            sender.sendMessage("§e/extraitems custommodel <id>");
            sender.sendMessage("§e/extraitems leather <red> <green> <blue>");
            sender.sendMessage("§e/extraitems modifier <name> <attribute> <mode> <amount>");
            sender.sendMessage("§e/extraitems create");
        }else{
            sender.sendMessage("§e/extraitems create <id> <material> <name>");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!sender.hasPermission("extraitems.give"))
            return new ArrayList<>();
        if(args.length < 2){
            if(bluprints.containsKey(sender)){
                return Arrays.asList("give", "list", "reload", "delete", "cancel", "custommodel", "leather", "modifier", "create");
            }else{
                return Arrays.asList("give", "list", "reload", "delete", "create");
            }
        }
        if(args[0].equalsIgnoreCase("give")){
            if(args.length == 2)
                return null;
            if(args.length == 3)
                return StringUtil.copyPartialMatches(args[2], ExtraItemsPlugin.INSTANCE.getItems().stream().map(ExtraItem::getId).collect(Collectors.toList()), new ArrayList<>());
        }
        if(args[0].equalsIgnoreCase("delete")){
            if(args.length == 2)
                return StringUtil.copyPartialMatches(args[1], ExtraItemsPlugin.INSTANCE.getItems().stream().map(ExtraItem::getId).collect(Collectors.toList()), new ArrayList<>());
        }
        if(args[0].equalsIgnoreCase("create")){
            if(bluprints.containsKey(sender))
                return new ArrayList<>();
            if(args.length == 3)
                return StringUtil.copyPartialMatches(args[2], Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList()), new ArrayList<>());
        }
        if(args[0].equalsIgnoreCase("modifier")){
            if(!bluprints.containsKey(sender))
                return new ArrayList<>();
            if(args.length == 3)
                return StringUtil.copyPartialMatches(args[2], Arrays.stream(ExtraItemModifier.Attribute.values()).map(Enum::name).collect(Collectors.toList()), new ArrayList<>());
            if(args.length == 4)
                return StringUtil.copyPartialMatches(args[3], Arrays.stream(ExtraItemModifier.Mode.values()).map(Enum::name).collect(Collectors.toList()), new ArrayList<>());
        }
        return new ArrayList<>();
    }

    private static class Bluprint {
        public String id;
        public Material material;
        public String name;
        public Integer leatherColor;
        public Bluprint(String id, Material material, String name){
            this.id = id;
            this.material = material;
            this.name = name;
        }
        public boolean hasModifier(String name){
            for(ExtraItemModifier modifier : modifiers){
                if(modifier.getName().equalsIgnoreCase(name))
                    return true;
            }
            return false;
        }
        public Integer customModelData = null;
        public List<ExtraItemModifier> modifiers = new ArrayList<>();
    }

}
