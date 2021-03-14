package pl.gitmanik.chatsystem;

import org.bukkit.entity.Player;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.commands.gpadmin.GPAdminCommand;

import java.util.ArrayList;
import java.util.List;

public class Spy implements GPAdminCommand
{
	@Override
	public void runCommand(Player player, String[] args)
	{
		if (GitmanikPlugin.chathandler == null)
		{
			player.sendMessage("Rangechat is disabled!");
			return;
		}

		boolean n = !ChatSystem.spy.getOrDefault(player, false);
		ChatSystem.spy.put(player, n);

		player.sendMessage("New Spy state: " + n);
	}

	@Override
	public List<String> tabComplete(String[] args)
	{
		return new ArrayList<String>();
	}
}
