package pl.gitmanik.commands.gpadmin;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EnchantLister implements GPAdminCommand
{
	@Override
	public void runCommand(Player player, String[] args)
	{
		player.sendMessage("Enchantments:");
		player.getInventory().getItemInMainHand().getEnchantments().forEach((key, value) -> player.sendMessage(key.getName() + " " + value));
	}

	@Override
	public List<String> tabComplete(String[] args)
	{
		return new ArrayList<String>();
	}
}
