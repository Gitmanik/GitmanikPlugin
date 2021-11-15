package pl.gitmanik.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

public class Homesystem implements CommandExecutor, Listener
{
	private static int KOSZT, DELAY;
	private static final String sethome = "ustawdom", home = "dom";
	Material itemHomesystem = Material.valueOf(GitmanikPlugin.gp.getConfig().getString("homesystem.material"));

	public HashMap<Player, Integer> homeDelayedTeleportationRunnables = new HashMap<>();

	public Homesystem()
	{
		KOSZT = GitmanikPlugin.gp.getConfig().getInt("homesystem.price");
		DELAY = GitmanikPlugin.gp.getConfig().getInt("homesystem.delay");
		GitmanikPlugin.gp.getCommand("dom").setExecutor(this);
		GitmanikPlugin.gp.getCommand("ustawdom").setExecutor(this);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if (homeDelayedTeleportationRunnables.containsKey(player))
		{
			player.sendMessage(ChatColor.RED + "Ruszył*ś się, teleportacja odwołana!");
			Bukkit.getScheduler().cancelTask(homeDelayedTeleportationRunnables.get(player));
			homeDelayedTeleportationRunnables.remove(player);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] strings)
	{
		Player player = (Player) sender;
		if (label.equalsIgnoreCase(sethome))
		{
			Location tmploc = player.getLocation();
			double tmpX = tmploc.getX();
			double tmpY = tmploc.getY();
			double tmpZ = tmploc.getZ();
			GitmanikPlugin.gp.getConfig().set("homes." + player.getUniqueId(), player.getWorld().getName() + "," + tmpX + "," + tmpY + "," + tmpZ);
			GitmanikPlugin.gp.saveConfig();
			player.sendMessage(ChatColor.GOLD + "Ustawiono dom!");
		}

		if (label.equalsIgnoreCase(home))
		{
			if (GitmanikPlugin.gp.getConfig().contains("homes." + player.getUniqueId()))
			{
				if (!player.getInventory().contains(itemHomesystem, KOSZT))
				{
					player.sendMessage(ChatColor.RED + "Nie stac cie na /" + home +"! Koszt: " + KOSZT + " diament");
					return true;
				}
				player.sendMessage(ChatColor.GOLD + "Teleportowanie w toku.. nie ruszaj się przez " + DELAY + "sekund!");
				int x = Bukkit.getScheduler().scheduleSyncDelayedTask(GitmanikPlugin.gp, () ->
				{
					String[] arg = GitmanikPlugin.gp.getConfig().getString("homes." + player.getUniqueId()).split(",");
					double[] parsed = new double[3];
					for (int a = 0; a < 3; a++)
					{
						parsed[a] = Double.parseDouble(arg[a + 1]);
					}
					Location location = new Location(Bukkit.getServer().getWorld(arg[0]), parsed[0], parsed[1], parsed[2]);
					player.teleport(location);
					player.sendMessage(ChatColor.GOLD + "Teleportowano do domu!");

					player.getInventory().removeItem(new ItemStack(itemHomesystem, KOSZT));
					homeDelayedTeleportationRunnables.remove(player);
				}, 20L *DELAY);
				homeDelayedTeleportationRunnables.put(player, x);
			} else
			{
				player.sendMessage(ChatColor.GOLD + "Najpierw ustaw dom! /" + sethome);
			}
		}
		return true;
	}
}
