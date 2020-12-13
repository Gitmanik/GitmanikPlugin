package pl.gitmanik.nightskip;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.gitmanik.GitmanikPlugin;

public class VoteSkipNightHandler implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("This is a player only command!");
			return false;
		}
		Player player = (Player) sender;
		World world = player.getWorld();

		if (GitmanikPlugin.gitmanikplugin.isDay(world) && GitmanikPlugin.nightskipping.allowSkip.getOrDefault(world, false))
		{
			int neededplayers = (int) Math.ceil(world.getPlayers().size() / 2.0);
			if (GitmanikPlugin.nightskipping.nightSkipCount.get(world).contains(player))
				return true;

			GitmanikPlugin.nightskipping.nightSkipCount.computeIfPresent(world, (w, list) ->
			{
				list.add(player);
				return list;
			});

			int players = GitmanikPlugin.nightskipping.nightSkipCount.get(world).size();


			for (Player p : world.getPlayers())
			{
				p.sendMessage(ChatColor.AQUA + player.getName() + ChatColor.BLUE + " zagłosował* za pominięciem nocy [" + players + "/" + neededplayers + "]");
			}
			if (players >= neededplayers)
			{
				world.setFullTime(0);
				for (Player p : world.getPlayers())
				{
					p.sendMessage(ChatColor.AQUA + "Bogowie posłuchali głosu ludu i pominęli noc");
				}
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Nie ma aktualnie nocy..");
		}
		return true;
	}
}
