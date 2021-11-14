package pl.gitmanik.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

public class BozekHandler implements Listener
{
	private Enchantment przychylnosc = EnchantmentHelper.GetEnchantment("przychylnosc");

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		Player player = e.getEntity();

		List<ItemStack> bozekFinder = Arrays.stream(player.getInventory().getContents()).filter(candidate -> (candidate != null && candidate.getType() == Material.EMERALD)).collect(Collectors.toList());
		for (ItemStack item : bozekFinder)
		{
			if (item.getEnchantmentLevel(przychylnosc) == 1 || item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) == 10)
			{
				item.setAmount(item.getAmount() - 1);
				e.setKeepInventory(true);
				e.setKeepLevel(true);
				e.setDroppedExp(0);
				e.getDrops().clear();
				e.setDeathMessage(ChatColor.GOLD + "Gracza " + player.getDisplayName() + " uśmiercono, ale Nieumarli zlitowali się nad nim i przywrócili mu dobytek!");
			}
		}
	}
}
