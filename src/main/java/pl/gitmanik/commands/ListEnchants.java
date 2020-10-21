package pl.gitmanik.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListEnchants implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{

		Player p = (Player) commandSender;
		if (!p.hasPermission(("gitmanik.listenchants")))
			return false;
		p.sendMessage("Enchanty:");
		p.getInventory().getItemInMainHand().getEnchantments().forEach((key, value) -> p.sendMessage(key.getName() + " " + value));
		return true;
	}
}
