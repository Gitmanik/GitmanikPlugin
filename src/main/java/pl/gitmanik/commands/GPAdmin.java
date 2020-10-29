package pl.gitmanik.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.enchants.EnchantmentHelper;
import pl.gitmanik.events.ChatHandler;
import pl.gitmanik.helpers.GitmanikDurability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GPAdmin implements CommandExecutor, TabCompleter
{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		List<String> toret = new ArrayList<>();

		String command = args[0];
		if (command.equals(""))
		{
			toret.add("spy"); //NO ARGS
			toret.add("listenchants"); //NO ARGS
			toret.add("durability"); //1 ARG
			toret.add("listentity"); //1 ARG
			toret.add("give"); //1 ARG
			toret.add("damage"); //1
			toret.add("enchant"); //2
			return toret;
		}

		if (args.length == 1)
		{
			switch (command)
			{
				case "durability":
				case "damage":
					toret.add("[amount]");
					break;

				case "listentity":
					for (EntityType type : EntityType.values())
					{
						toret.add(type.name());
					}
					break;
			}
		}
		if (args.length == 2)
		{
			if (command.equals("give"))
			{
				toret.addAll(GitmanikPlugin.compressedItems.keySet());
				toret.addAll(GitmanikPlugin.customItems.keySet());
			}

			if (command.equals("enchant"))
			{
				for (Enchantment e : GitmanikPlugin.customEnchantments)
				{
					toret.add(e.getKey().getKey());
				}
			}
		}
		if (args.length == 3)
		{
			if (command.equals("enchant"))
			{
				if (EnchantmentHelper.GetEnchantment(args[1]) != null)
					for (Enchantment e : GitmanikPlugin.customEnchantments)
					{
						toret.add("[level]");
					}
			}
		}

		return toret;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{

		//TODO NAPRAWIC LISTENTITY
		//TODO DODAC SZUKANIE NAJBLISZEGO(YCH) ENTITY
		if (!(sender instanceof Player)) {
			sender.sendMessage("This is a player only command!");
			return false;
		}

		Player player = (Player) sender;
		if (args.length == 0)
			return false;

		switch (args[0].toLowerCase())
		{
			case "spy":
				return ToggleSpy(player, args);
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
			case "damage":
				return DamageItem(player,args);
			default:
				player.sendMessage("Unknown GPAdmin command.");
				player.sendMessage("Options: spy durability listentity enchant listenchants give");
				return false;
		}
	}

	private boolean DamageItem(Player player, String[] args)
	{
		if (args.length == 1)
		{
			return false;
		}

		if (!isNumeric(args[1]))
		{
			player.sendMessage(ChatColor.RED + "GPAdmin->damage requires numeric value as first argument.");
			return true;
		}

		ItemStack heldItem = player.getInventory().getItemInMainHand();

		if (!(heldItem.getItemMeta() instanceof Damageable))
		{
			player.sendMessage(ChatColor.RED + "You must hold an damageable item to damage.");
			return true;
		}

		Damageable d = (Damageable) heldItem.getItemMeta();

		d.setDamage(Integer.parseInt(args[1]));

		heldItem.setItemMeta((ItemMeta) d);

		player.sendMessage("Set damage of " + heldItem.getItemMeta().getDisplayName() + ChatColor.WHITE + " to " + args[1]);

		return true;
	}

	private boolean ToggleSpy(Player player, String[] args)
	{
		boolean n = !ChatHandler.spy.getOrDefault(player, false);
		ChatHandler.spy.put(player, n);

		player.sendMessage("New Spy state: " + n);

		return true;
	}

	private boolean GiveItem(Player player, String[] args)
	{
		if (args.length == 1)
		{
			return false;
		}

		String key = args[1].toLowerCase();

		HashMap<String, ItemStack> allItems = GitmanikPlugin.customItems;
		allItems.putAll(GitmanikPlugin.compressedItems);

		if (!allItems.containsKey(key))
		{
			player.sendMessage(ChatColor.RED + "Item " + key + " not found. Options: " + String.join(", ", allItems.keySet()));
			return true;
		}

		player.getInventory().addItem(allItems.get(key));

		return true;
	}

	private boolean Enchant(Player player, String[] args)
	{

		if (args.length < 3)
		{
			player.sendMessage("/gpadmin [key] [level]");
			return false;
		}

		String key = args[1].toLowerCase();

		Enchantment e = EnchantmentHelper.GetEnchantment(key);

		if (e == null)
		{
			player.sendMessage(ChatColor.RED + "Enchantment " + key + " not found. Options: " + String.join(", ", GitmanikPlugin.customItems.keySet()));
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
