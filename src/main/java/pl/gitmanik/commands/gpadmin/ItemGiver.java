package pl.gitmanik.commands.gpadmin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemGiver implements GPAdminCommand
{
	@Override
	public void runCommand(Player player, String[] args)
	{
		if (args.length == 0)
		{
			player.sendMessage(ChatColor.RED + "This subcommand requires argument(s).");
			return;
		}

		String key = args[0].toLowerCase();

		HashMap<String, ItemStack> allItems = GitmanikPlugin.customItems;
		allItems.putAll(GitmanikPlugin.compressedItems);

		if (!allItems.containsKey(key))
		{
			player.sendMessage(ChatColor.RED + "Item " + key + " not found. Options: " + String.join(", ", allItems.keySet()));
			return;
		}

		player.getInventory().addItem(allItems.get(key));

	}

	@Override
	public List<String> tabComplete(String[] args)
	{
		ArrayList<String> allItems = new ArrayList<>();
		allItems.addAll(GitmanikPlugin.customItems.keySet());
		allItems.addAll(GitmanikPlugin.compressedItems.keySet());

		return allItems;
	}
}
