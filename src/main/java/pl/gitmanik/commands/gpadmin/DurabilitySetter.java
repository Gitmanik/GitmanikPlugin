package pl.gitmanik.commands.gpadmin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import pl.gitmanik.helpers.GitmanikDurability;

import java.util.ArrayList;
import java.util.List;

class DurabilitySetter implements GPAdminCommand
{
	@Override
	public void runCommand(Player player, String[] args)
	{
		if (args.length == 0)
		{
			player.sendMessage(ChatColor.RED + "This subcommand requires argument(s).");
			return;
		}

		if (!GPAdmin.isNumeric(args[0]))
		{
			player.sendMessage(ChatColor.RED + "This subcommand requires numeric value as first argument.");
			return;
		}
		GitmanikDurability.SetDurability(player.getInventory().getItemInMainHand(), Integer.parseInt(args[0]));
	}

	@Override
	public List<String> tabComplete(String[] args)
	{
		List<String> toReturn = new ArrayList<>();
		if (args.length > 1)
			toReturn.add("Too many arguments!");
		else
			toReturn.add("[Value]");

		return toReturn;
	}
}
