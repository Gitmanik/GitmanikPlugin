package pl.gitmanik.commands.gpadmin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemDamager implements GPAdminCommand
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
			player.sendMessage(ChatColor.RED + "GPAdmin->damage requires numeric value as first argument.");
			return;
		}

		ItemStack heldItem = player.getInventory().getItemInMainHand();

		if (!(heldItem.getItemMeta() instanceof Damageable))
		{
			player.sendMessage(ChatColor.RED + "You must hold an damageable item to damage.");
			return;
		}

		Damageable d = (Damageable) heldItem.getItemMeta();

		d.setDamage(Integer.parseInt(args[0]));

		heldItem.setItemMeta((ItemMeta) d);

		player.sendMessage("Set damage of " + heldItem.getItemMeta().getDisplayName() + ChatColor.WHITE + " to " + args[0]);

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
