package pl.gitmanik.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;

public class Homesystem implements CommandExecutor
{
	//TODO: konfig
	private static int KOSZT = 3;

	private static final String sethome = "ustawdom", home = "dom";

	public Homesystem()
	{
		GitmanikPlugin.gp.getCommand("dom").setExecutor(this);
		GitmanikPlugin.gp.getCommand("ustawdom").setExecutor(this);
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
				if (!player.getInventory().contains(Material.DIAMOND, KOSZT))
				{
					player.sendMessage(ChatColor.RED + "Nie stac cie na /" + home +"! Koszt: " + KOSZT + " diament");
					return true;
				}

				player.getInventory().removeItem(new ItemStack(Material.DIAMOND, KOSZT));

				String[] arg = GitmanikPlugin.gp.getConfig().getString("homes." + player.getUniqueId()).split(",");
				double[] parsed = new double[3];
				for (int a = 0; a < 3; a++)
				{
					parsed[a] = Double.parseDouble(arg[a + 1]);
				}

				Location location = new Location(Bukkit.getServer().getWorld(arg[0]), parsed[0], parsed[1], parsed[2]);
				player.teleport(location);
				player.sendMessage(ChatColor.GOLD + "Teleportowano do domu!");
			} else
			{
				player.sendMessage(ChatColor.GOLD + "Najpierw ustaw dom! /" + sethome);
			}
		}
		return true;
	}
}
