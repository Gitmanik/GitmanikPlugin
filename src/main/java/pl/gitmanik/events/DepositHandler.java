package pl.gitmanik.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.helpers.GitmanikDurability;

public class DepositHandler implements Listener
{
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		ItemStack hand = event.getItem();

		if (hand == null)
			return;

		if (hand.getEnchantmentLevel(GitmanikPlugin.depoEnchant) > 0)
		{
			player.openInventory(player.getEnderChest());
			GitmanikDurability.EditDurability(player, hand, -1);
		}
	}
}
