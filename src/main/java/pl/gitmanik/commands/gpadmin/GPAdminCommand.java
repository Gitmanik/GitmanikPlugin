package pl.gitmanik.commands.gpadmin;

import org.bukkit.entity.Player;

import java.util.List;

public interface GPAdminCommand
{
	void runCommand(Player player, String[] args);
	List<String> tabComplete(String[] args);
}
