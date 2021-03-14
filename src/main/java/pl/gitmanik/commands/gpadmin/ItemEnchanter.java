package pl.gitmanik.commands.gpadmin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.enchants.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemEnchanter implements GPAdminCommand
{
	@Override
	public void runCommand(Player player, String[] args)
	{
		if (args.length != 2)
		{
			player.sendMessage(ChatColor.RED + "This subcommand requires two arguments!");
			return;
		}

		String key = args[0].toLowerCase();

		Enchantment e = EnchantmentHelper.GetEnchantment(key);

		if (e == null)
		{
			player.sendMessage(ChatColor.RED + "Enchantment " + key + " not found. Options: " + String.join(", ", GitmanikPlugin.customItems.keySet()));
			return;
		}

		if (!GPAdmin.isNumeric(args[1]))
		{
			player.sendMessage(ChatColor.RED + "This subcommand requires numeric value as second argument.");
			return;
		}

		EnchantmentHelper.AddEnchantWithLore(player.getInventory().getItemInMainHand(), e, Integer.parseInt(args[1]));

	}

	@Override
	public List<String> tabComplete(String[] args)
	{
		ArrayList<String> toReturn = new ArrayList<>();
		if (args.length == 0)
		{
			for (Enchantment e : GitmanikPlugin.customEnchantments)
			{
				toReturn.add(e.getKey().getKey());
			}
		}
		if (args.length == 1)
		{
			if (EnchantmentHelper.GetEnchantment(args[0]) != null)
			{
				for (Enchantment e : GitmanikPlugin.customEnchantments)
				{
					toReturn.add("[level]");
				}
			}
		}
		return toReturn;
	}
}
