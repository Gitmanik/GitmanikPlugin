package pl.gitmanik.commands.gpadmin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EntityLister implements GPAdminCommand
{
	@Override
	public void runCommand(Player player, String[] args)
	{
		EntityType e = null;

		if (args.length == 0)
		{
			player.sendMessage(ChatColor.RED + "This subcommand requires argument(s).");
			return;
		}

		for (EntityType type : EntityType.values()) {
			if(type.name().equalsIgnoreCase(args[1])) {
				e = type;
			}
		}

		if (e == null)
		{
			player.sendMessage(ChatColor.RED + "This subcommand requires uppercase entity name as first argument.");
			return;
		}

		for (Entity t : player.getWorld().getEntities()) {
			if (t.getType().equals(e))
			{
				player.sendMessage(String.format("%s: X: %.2f, Y: %.2f, Z: %.2f", t.getName(), t.getLocation().getX(),t.getLocation().getY(),t.getLocation().getZ()));
			}
		}
	}

	@Override
	public List<String> tabComplete(String[] args)
	{
		ArrayList<String> toReturn = new ArrayList<>();
		for (EntityType type : EntityType.values())
		{
			toReturn.add(type.name());
		}
		return toReturn;
	}
}
