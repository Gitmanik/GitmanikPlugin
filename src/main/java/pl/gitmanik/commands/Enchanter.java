package pl.gitmanik.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.enchants.EnchantmentHelper;

public class Enchanter implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("This is a player only command!");
			return false;
		}

		Player player = (Player) sender;
		if (args.length == 0)
			return false;

		Enchantment e;

		switch (args[0].toLowerCase())
		{
			case "farmershand":
				e = GitmanikPlugin.rekaFarmera;
				break;
			case "tunneldigger":
				e = GitmanikPlugin.tunneldigger;
				break;
			case "przychylnosc":
				e = GitmanikPlugin.przychylnoscBogow;
				break;
			case "depoenchant":
				e = GitmanikPlugin.depoEnchant;
				break;

			default:
				player.sendMessage("Unexpected value: " + args[0].toLowerCase());
				return true;
		}

		EnchantmentHelper.AddEnchantWithLore(player.getInventory().getItemInMainHand(), e, Integer.parseInt(args[1]));
		return true;
	}
}
