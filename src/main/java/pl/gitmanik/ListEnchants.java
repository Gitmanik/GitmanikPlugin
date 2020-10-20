package pl.gitmanik;

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
		p.getInventory().getItemInMainHand().getEnchantments().forEach((key, value) -> p.sendMessage(key.getName() + " " + value));
		return true;
	}
}
