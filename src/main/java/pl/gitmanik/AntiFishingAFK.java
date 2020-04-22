package pl.gitmanik;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

import static pl.gitmanik.GitmanikPlugin.gitmanikplugin;

public class AntiFishingAFK implements Listener
{
	private HashMap<Player, Long> lastUsed = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event){
		if (event.getAction() == Action.LEFT_CLICK_AIR) return;
		Player p = event.getPlayer();
		if(event.getItem() != null && event.getItem().getType() == Material.FISHING_ROD){
			long time = Instant.now().toEpochMilli();
			long used = lastUsed.getOrDefault(p, (long) 0);
			lastUsed.put(p, time);
			if (time - used < 500)
			{
				event.setCancelled(true);
				p.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Ekhm, nie AFK farmimy :(");

				StringBuilder sb = new StringBuilder();
				sb.append("Gracz: ");
				sb.append(p.getName());
				sb.append(" X:");
				sb.append((int) p.getLocation().getX());
				sb.append(" Y:");
				sb.append((int) p.getLocation().getY());
				sb.append(" Z:");
				sb.append((int) p.getLocation().getZ());
				sb.append('\r');

				try
				{
					Files.write(Paths.get(gitmanikplugin.getDataFolder().getAbsolutePath() + "/afkfarma.log"), sb.toString().getBytes(), StandardOpenOption.APPEND);

			} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
	}
	}

}
