package pl.gitmanik.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.gitmanik.GitmanikPlugin;

import java.util.HashMap;
import java.util.List;

public class ChatHandler implements Listener
{
	private double RADIUS;

	public static HashMap<Player, Boolean> spy = new HashMap<>();

	public ChatHandler()
	{
		Bukkit.getPluginManager().registerEvents(this, GitmanikPlugin.gp);
		RADIUS = GitmanikPlugin.gp.getConfig().getDouble("rangechat.range");
	}

	@EventHandler(ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event)
	{
		Player sender = event.getPlayer();
		List<Player> players = sender.getWorld().getPlayers();
		String message = event.getMessage();
		boolean global = message.startsWith("g ") || message.startsWith("G ");
		if (global)
			message = message.substring(2);

		Location l = sender.getLocation();

		GitmanikPlugin.gp.getLogger().info( String.format("§6%s,%s,%s§r [%s§r] §e%s§r: §f%s", l.getBlockX(), l.getBlockY(), l.getBlockZ(), global ? "§aGlobal" : "§9Local", sender.getDisplayName(), message.replace("%", "%%")));
		if (global)
		{
			String target = ChatColor.GREEN + "[GLOBALNY] " + ChatColor.YELLOW + sender.getDisplayName() + ": " + ChatColor.WHITE + message;
			event.setMessage(message);
			event.setFormat(target.replace("%", "%%"));
		}
		else
		{
			event.setCancelled(true);
			String target = ChatColor.BLUE + "[LOKALNY";
			String target2 = ChatColor.BLUE + "] " + ChatColor.YELLOW + sender.getDisplayName() + ": " + ChatColor.WHITE + message;
			Location baseL = sender.getLocation();
			for (Player player : players)
			{
				Location loc1 = player.getLocation();
				double distance = distanceTo(baseL.getX(), baseL.getZ(), loc1.getX(), loc1.getZ());
				if (distance < RADIUS || spy.getOrDefault(player, false))
				{
					if (player != sender)
						player.sendMessage(target + " " + ChatColor.ITALIC + (int) Math.ceil(distance) + " kratek"+ target2);
					else
						player.sendMessage(target + target2);
				}
			}
		}
	}

	public double distanceTo(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
}
