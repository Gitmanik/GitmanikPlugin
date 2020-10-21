package pl.gitmanik.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.gitmanik.helpers.GitmanikDurability;

public class DurabilityAdmin implements CommandExecutor
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

		GitmanikDurability.SetDurability(player.getInventory().getItemInMainHand(), Integer.parseInt(args[0]));
		return true;
	}
}
