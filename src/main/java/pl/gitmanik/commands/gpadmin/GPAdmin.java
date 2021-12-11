package pl.gitmanik.commands.gpadmin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pl.gitmanik.GitmanikPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GPAdmin implements CommandExecutor, TabCompleter
{
	//TODO: konfig
	private static final String gpadmin = "gpadmin";

	public HashMap<String, GPAdminCommand> commands;

	public GPAdmin()
	{
		//TODO DODAC SZUKANIE NAJBLISZEGO(YCH) ENTITY

		GitmanikPlugin.gp.getCommand(gpadmin).setExecutor(this);
		GitmanikPlugin.gp.getCommand(gpadmin).setTabCompleter(this);

		commands = new HashMap<>();
		commands.put("durability", new DurabilitySetter());
		commands.put("damage", new ItemDamager());
		commands.put("listentity", new EntityLister());
		commands.put("give", new ItemGiver());
		commands.put("enchant", new ItemEnchanter());
		commands.put("listenchants", new EnchantLister());

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		String command = args[0];
		if (command.isEmpty())
		{
			return new ArrayList<>(commands.keySet());
		}

		if (commands.containsKey(command))
		{
			return commands.get(command).tabComplete(Arrays.copyOfRange(args, 1, args.length));
		}
		return new ArrayList<>();
	}

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

		if (commands.containsKey(args[0]))
		{
			commands.get(args[0]).runCommand(player, Arrays.copyOfRange(args, 1, args.length));
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Unknown GPAdmin command. Available options:");
			player.sendMessage(Arrays.toString(commands.keySet().toArray()));
		}

		return true;
	}

	public static boolean isNumeric(String strNum) {
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
