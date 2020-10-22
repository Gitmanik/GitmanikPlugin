package pl.gitmanik.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.enchants.EnchantmentHelper;
import pl.gitmanik.helpers.GitmanikDurability;

public class GPAdmin implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("This is a player only command!");
			return false;
		}

		Player player = (Player) sender;
		if (args.length == 0)
			return false;

		switch (args[0].toLowerCase())
		{
			case "durability":
				return DurabilityHandler(player, args);
			case "listentity":
				return ListEntity(player,args);
			case "enchant":
				return Enchant(player, args);
			case "listenchants":
				return ListEnchants(player, args);
			case "give":
				return GiveItem(player,args);
			default:
				player.sendMessage("Unknown GPAdmin command.");
				player.sendMessage("Options: durability listentity enchant listenchants give");
				return false;
		}
	}

	private boolean GiveItem(Player player, String[] args)
	{
		if (args.length == 1)
		{
			return false;
		}
		ItemStack s;
		
		switch (args[1].toLowerCase())
		{
			case "mruwikilof":
				s = GitmanikPlugin.mruwiKilof;
				break;
			case "enderowydepozyt":
				s = GitmanikPlugin.enderowyDepozyt;
				break;
			case "magicznaorchidea":
				s = GitmanikPlugin.magicznaOrchidea;
				break;

			default:
				player.sendMessage("Unknown GPAdmin command.");
				player.sendMessage("Options: mruwikilof enderowydepozyt magicznaorchidea");
				return false;
		}

		player.getInventory().addItem(s);

		return true;
	}

	private boolean Enchant(Player player, String[] args)
	{
		Enchantment e;

		if (args.length < 3)
		{
			player.sendMessage("[farmershand tunneldigger przychylnosc depoenchant] [level]");
			return false;
		}

		switch (args[1].toLowerCase())
		{
			case "farmershand":
				e = GitmanikPlugin.rekaFarmera;
				break;
			case "tunneldigger":
				e = GitmanikPlugin.mruwiaReka;
				break;
			case "przychylnosc":
				e = GitmanikPlugin.przychylnoscBogow;
				break;
			case "depoenchant":
				e = GitmanikPlugin.depoEnchant;
				break;

			default:
				player.sendMessage("Unexpected value: " + args[1].toLowerCase());
				player.sendMessage("Options: farmershand tunneldigger przychylnosc depoenchant");
				return true;
		}
		if (!isNumeric(args[2]))
		{
			player.sendMessage(ChatColor.RED + "GPAdmin->durability requires numeric value as first argument.");
			return false;
		}

		EnchantmentHelper.AddEnchantWithLore(player.getInventory().getItemInMainHand(), e, Integer.parseInt(args[2]));
		return true;
	}

	private boolean ListEnchants(Player player, String[] args)
	{
		player.sendMessage("Enchantments:");
		player.getInventory().getItemInMainHand().getEnchantments().forEach((key, value) -> player.sendMessage(key.getName() + " " + value));
		return true;
	}

	private boolean ListEntity(Player player, String[] args)
	{
		EntityType e = null;

		for (EntityType type : EntityType.values()) {
			if(type.name().equalsIgnoreCase(args[1])) {
				e = type;
			}
		}

		if (e == null)
		{
			player.sendMessage(ChatColor.RED + "GPAdmin->listentity requires entity name as first argument.");
			return false;
		}

		for (Entity t : player.getWorld().getEntities()) {
			if (t.getType().equals(e))
			{
				player.sendMessage(String.format("%s: X: %.2f, Y: %.2f, Z: %.2f", t.getName(), t.getLocation().getX(),t.getLocation().getY(),t.getLocation().getZ()));
			}
		}

		return true;
	}

	private boolean DurabilityHandler(Player player, String[] args)
	{
		if (!isNumeric(args[1]))
		{
			player.sendMessage(ChatColor.RED + "GPAdmin->durability requires numeric value as first argument.");
			return false;
		}
		GitmanikDurability.SetDurability(player.getInventory().getItemInMainHand(), Integer.parseInt(args[1]));
		return true;
	}

	private static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
