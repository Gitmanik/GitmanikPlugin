package pl.gitmanik.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;

import java.util.HashMap;

public class Teleportsystem implements CommandExecutor, Listener
{
	private static int KOSZT, DELAY;
	private static final String requestTelepost = "gtpa", acceptTeleport = "gtpaccept";
	Material itemTeleportsystem = Material.valueOf(GitmanikPlugin.gp.getConfig().getString("teleportsystem.material"));

	public HashMap<Player, Player> tpa = new HashMap<>();
	public HashMap<Player, Integer> tpaExpiredRunnables = new HashMap<>();
	public HashMap<Player, Integer> tpaDelayedTeleportationRunnables = new HashMap<>();

	public Teleportsystem()
	{
		KOSZT = GitmanikPlugin.gp.getConfig().getInt("teleportsystem.price");
		DELAY = GitmanikPlugin.gp.getConfig().getInt("teleportsystem.delay");
		GitmanikPlugin.gp.getCommand("gtpa").setExecutor(this);
		GitmanikPlugin.gp.getCommand("gtpaccept").setExecutor(this);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if (tpaDelayedTeleportationRunnables.containsKey(player))
		{
			player.sendMessage(ChatColor.RED + "Ruszył*ś się, teleportacja odwołana!");
			Bukkit.getScheduler().cancelTask(tpaDelayedTeleportationRunnables.get(player));
			tpaDelayedTeleportationRunnables.remove(player);
			Bukkit.getScheduler().cancelTask(tpaExpiredRunnables.get(player));
			tpaExpiredRunnables.remove(player);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player player = (Player) sender;
		if (label.equalsIgnoreCase(requestTelepost))
		{
			if (args.length == 0)
				return false;

			Player target = Bukkit.getPlayerExact(args[0]);

			if (target == null)
			{
				player.sendMessage(String.format("%sNie znaleziono gracza o nicku: %s", ChatColor.RED, args[0]));
				return true;
			}

			if (target == player)
			{
				player.sendMessage(ChatColor.RED + "Nie możesz teleportować się do samego siebie.");
				return true;
			}

			if (!player.getInventory().contains(itemTeleportsystem, KOSZT))
			{
				player.sendMessage(String.format("%s Nie stac cie na /%s! Koszt: %s diament(ów) (pobierany podczas teleportowania)", ChatColor.RED, requestTelepost, KOSZT));
				return true;
			}

			tpa.put(target,player);

			target.sendMessage(ChatColor.GOLD + player.getDisplayName() + ChatColor.WHITE + " chce się do ciebie teleportować.");
			target.sendMessage(ChatColor.WHITE + "Aby zaakceptować wpisz " + ChatColor.GOLD + "/" + acceptTeleport);

			sender.sendMessage(ChatColor.WHITE + "Wysłano prośbę do gracza " + ChatColor.GOLD + target.getDisplayName());

			if (tpaExpiredRunnables.containsKey(player))
			{
				Bukkit.getScheduler().cancelTask(tpaExpiredRunnables.get(player));
				tpaExpiredRunnables.remove(player);
			}

			int x = Bukkit.getScheduler().scheduleSyncDelayedTask(GitmanikPlugin.gp, () ->
			{
				sender.sendMessage("Prośba przedawniona.");
				tpa.remove(target);
				tpaExpiredRunnables.remove(player);
			}, 20*15);

			tpaExpiredRunnables.put(player,x);

			return true;
		}

		if (label.equalsIgnoreCase(acceptTeleport))
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
			if (!base.getInventory().contains(itemTeleportsystem, KOSZT))
			{
				base.sendMessage(ChatColor.RED + "Nie stac cie na /" + requestTelepost +"! Koszt: " + KOSZT + " diament (pobierany podczas teleportowania)");
				player.sendMessage(ChatColor.RED + "Gracza nie było stać na teleportowanie się do Ciebie.");

				return true;
			}

			if (tpaExpiredRunnables.containsKey(base))
			{
				Bukkit.getScheduler().cancelTask(tpaExpiredRunnables.get(base));
				tpaExpiredRunnables.remove(base);
			}
			base.sendMessage(ChatColor.GOLD + "Teleportowanie w toku.. nie ruszaj się przez " + DELAY + "sekund!");

			int x = Bukkit.getScheduler().scheduleSyncDelayedTask(GitmanikPlugin.gp, () ->
			{
				base.teleport(player);
				base.getInventory().removeItem(new ItemStack(itemTeleportsystem, KOSZT));
				player.sendMessage(ChatColor.GOLD + "Przeteleportowano " + ChatColor.GOLD + base.getDisplayName() + ChatColor.WHITE + " do Ciebie.");

				tpaDelayedTeleportationRunnables.remove(base);
				tpa.remove(player);

			}, 20L *DELAY);
			tpaDelayedTeleportationRunnables.put(base, x);
			return true;
		}

		return false;
	}
}
