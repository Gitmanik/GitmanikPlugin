package pl.gitmanik.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.enchants.EnchantmentHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeathHandler implements Listener
{

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDeath(PlayerDeathEvent e)
	{
		Player player = e.getEntity();

		List<ItemStack> bozekFinder = Arrays.stream(player.getInventory().getContents()).filter(xx -> (xx != null && xx.getType() == Material.EMERALD)).collect(Collectors.toList());
		for (ItemStack item : bozekFinder)
		{
			if (item.getEnchantmentLevel(EnchantmentHelper.GetEnchantment("przychylnosc")) == 1)
			{
				item.setAmount(item.getAmount() - 1);
				e.setKeepInventory(true);
				e.setKeepLevel(true);
				e.setDroppedExp(0);
				e.getDrops().clear();
				e.setDeathMessage(ChatColor.GOLD + "Gracza " + player.getDisplayName() + "uśmiercono, ale Nieumarli zlitowali się nad nim i przywrócili mu dobytek!"); //nwm czy to bedzie dzialac ale chuj
			}
		}
	}
}
