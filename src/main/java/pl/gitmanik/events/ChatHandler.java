package pl.gitmanik.events;

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
	private final GitmanikPlugin plugin;
	private final double RADIUS = 200.0;

	public static HashMap<Player, Boolean> spy = new HashMap<>();

	public ChatHandler(GitmanikPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event)
	{

		Player sender = event.getPlayer();
		List<Player> players = sender.getWorld().getPlayers();
		String message = event.getMessage();
		boolean global = message.startsWith("g ") || message.startsWith("G ");

		plugin.getLogger().info(((global) ? "Global" : "Local") + " Message by " + sender.getDisplayName() + ": " + message);

		if (global)
		{
			String target = ChatColor.GREEN + "[GLOBALNY] " + ChatColor.YELLOW + sender.getDisplayName() + ": " + ChatColor.WHITE + message.substring(2);
			event.setMessage(message.substring(2));
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
