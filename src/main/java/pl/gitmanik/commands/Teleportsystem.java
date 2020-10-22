package pl.gitmanik.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;

import java.util.HashMap;

public class Teleportsystem implements CommandExecutor
{
	public static final int KOSZT = 5;
	public HashMap<Player, Player> tpa = new HashMap<>();
	public HashMap<Player, Integer> tasks = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("gtpa"))
		{
			if (args.length == 0)
				return false;

			Player target = Bukkit.getPlayerExact(args[0]);

			if (target == null)
			{
				player.sendMessage(ChatColor.RED + "Nie znaleziono gracza o nicku : " + args[0]);
				return true;
			}

			if (target == player)
			{
				player.sendMessage(ChatColor.RED + "Nie możesz teleportować się do samego siebie.");
				return true;
			}

			if (!player.getInventory().contains(Material.DIAMOND, 5))
			{
				player.sendMessage(ChatColor.RED + "Nie stac cie na /gtpa! Koszt: " + KOSZT + " diament (pobierany podczas teleportowania)");
				return true;
			}

			tpa.put(target,player);

			target.sendMessage(ChatColor.GOLD + player.getDisplayName() + ChatColor.WHITE + " chce się do ciebie teleportować.");
			target.sendMessage(ChatColor.WHITE + "Aby zaakceptować wpisz " + ChatColor.GOLD + "/gtpaccept");

			sender.sendMessage(ChatColor.WHITE + "Wysłano prośbę do gracza " + ChatColor.GOLD + target.getDisplayName());

			if (tasks.containsKey(player))
			{
				Bukkit.getScheduler().cancelTask(tasks.get(player));
				tasks.remove(player);
			}

			int x = Bukkit.getScheduler().scheduleSyncDelayedTask(GitmanikPlugin.gitmanikplugin, () ->
			{
				sender.sendMessage("Prośba przedawniona.");
				tpa.remove(target);
				tasks.remove(player);
			}, 20*15);

			tasks.put(player,x);

			return true;

		}

		if (label.equalsIgnoreCase("gtpaccept"))
		{
			if (!tpa.containsKey(player))
			{
				player.sendMessage(ChatColor.RED + "Nie masz żadnych aktywnych próśb o teleport");
				return true;
			}
			Player base = tpa.get(player);
			if (base == null)
			{
				player.sendMessage(ChatColor.RED + "Gracz jest już offline :(");
				return true;
			}
			if (!base.getInventory().contains(Material.DIAMOND, 5))
			{
				base.sendMessage(ChatColor.RED + "Nie stac cie na /gtpa! Koszt: " + KOSZT + " diament (pobierany podczas teleportowania)");
				player.sendMessage(ChatColor.RED + "Gracza nie było stać na teleportowanie się do Ciebie.");

				return true;
			}
			base.teleport(player);
			base.getInventory().removeItem(new ItemStack(Material.DIAMOND, KOSZT));
			player.sendMessage(ChatColor.WHITE + "Przeteleportowano " + ChatColor.GOLD + base.getDisplayName() + ChatColor.WHITE + " do Ciebie.");

			if (tasks.containsKey(base))
			{
				Bukkit.getScheduler().cancelTask(tasks.get(base));
				tasks.remove(base);
			}
			tpa.remove(player);
			return true;
		}

		return false;
	}
}
