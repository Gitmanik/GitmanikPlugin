package pl.gitmanik.events;

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
		Player p = e.getEntity();

		List<ItemStack> allPotions = Arrays.stream(p.getInventory().getContents()).filter(xx -> (xx != null && xx.getType() == Material.EMERALD)).collect(Collectors.toList());
		for (ItemStack x : allPotions)
		{
			if (x.getEnchantmentLevel(EnchantmentHelper.GetEnchantment("przychylnosc")) == 1)
			{
				x.setAmount(x.getAmount() - 1);
				e.setKeepInventory(true);
				e.setKeepLevel(true);
				e.setDroppedExp(0);
				e.getDrops().clear();
				e.setDeathMessage(p.getDisplayName() + " zginął/a, ale Nieumarli zlitowali się nad nim i przywrócili rzeczy!");
			}
		}
	}
}
