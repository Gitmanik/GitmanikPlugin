package pl.gitmanik.nightskip;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pl.gitmanik.GitmanikPlugin;

import java.util.HashMap;
import java.util.HashSet;

public class NightSkipping implements Runnable
{
	public HashMap<World, HashSet<Player>> nightSkipCount = new HashMap<>();
	public HashMap<World, Boolean> allowSkip = new HashMap<>();
	public HashMap<World, Boolean> asked = new HashMap<>();

	@Override
	public void run()
	{
		for (World w : Bukkit.getWorlds())
		{
			if (!asked.containsKey(w))
				asked.put(w, false);

			if (!GitmanikPlugin.gitmanikplugin.isDay(w))
			{
				if (!asked.get(w))
				{
					asked.put(w, true);
					if (Math.random() < 0.7)
					{
						nightSkipCount.put(w, new HashSet<>());

						allowSkip.put(w, true);
						for (Player p : w.getPlayers())
						{
							TextComponent clickablex = new TextComponent("Nastała noc. Bogowie dali Ci możliwość zagłosowania za ");
							clickablex.setColor(ChatColor.BLUE);

							TextComponent clickable = new TextComponent("[pominięciem jej]");

							clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gtmvoteskipnight"));
							clickable.setColor(ChatColor.GREEN);

							p.sendMessage(clickablex, clickable);
						}
					}
					else
					{
						allowSkip.put(w, false);
					}
				}
			}
			else
			{
				asked.put(w, false);
			}
		}
	}
}
