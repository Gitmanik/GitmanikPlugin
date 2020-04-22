package pl.gitmanik;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;

public class ListAllEntities implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0)
            return false;

        EntityType enttype = getEntityByName(args[0]);
        if (enttype == null)
        {
            sender.sendMessage(ChatColor.RED + "Entity == null");
            return false;
        }

        for (Entity t : player.getWorld().getEntities()) {
            if (t.getType().equals(enttype))
            {
                sender.sendMessage(String.format("%s: X: %.2f, Y: %.2f, Z: %.2f", t.getName(), t.getLocation().getX(),t.getLocation().getY(),t.getLocation().getZ()));
            }
        }

        return true;
    }
    public EntityType getEntityByName(String name) {
        for (EntityType type : EntityType.values()) {
            if(type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}