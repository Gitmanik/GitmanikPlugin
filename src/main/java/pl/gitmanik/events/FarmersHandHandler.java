package pl.gitmanik.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.enchants.EnchantmentHelper;
import pl.gitmanik.helpers.GitmanikDurability;

public class FarmersHandHandler implements Listener
{
	private final Enchantment rekaFarmera;
	private final Enchantment depoEnchant;

	public FarmersHandHandler()
	{
		rekaFarmera = EnchantmentHelper.GetEnchantment("rekafarmera");
		depoEnchant = EnchantmentHelper.GetEnchantment("depoenchant");
		assert rekaFarmera != null;
		assert depoEnchant != null;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Block placedBlock = event.getBlock();
		ItemStack secHandItem = player.getInventory().getItemInOffHand();
		ItemStack mainHandItem = player.getInventory().getItemInMainHand();

		if (GitmanikPlugin.compressedItems.containsValue(mainHandItem) || mainHandItem.getEnchantmentLevel(rekaFarmera) > 0 || mainHandItem.getEnchantmentLevel(depoEnchant) > 0)
		{
			event.setCancelled(true);
			return;
		}

		if (secHandItem.getEnchantmentLevel(rekaFarmera) == 1 && !(placedBlock.getBlockData() instanceof Ageable))
		{
			event.setCancelled(true);
			return;
		}

		if (secHandItem.getEnchantmentLevel(rekaFarmera) == 1 && placedBlock.getBlockData() instanceof Ageable)
		{
			mainHandItem.setAmount(mainHandItem.getAmount() - 1); // Pierwszy położony seed (wywolal event)
			if (!GitmanikDurability.EditDurability(player, secHandItem, -1))
				return;

			for (int x = -1; x <=1; x++)
			{
				for (int z = -1; z <=1; z++)
				{
					if (mainHandItem.getAmount() <= 0)
						return;

					if (Plant(placedBlock.getLocation().add(x,0,z).getBlock(), placedBlock.getType()))
					{
						boolean cancel = !GitmanikDurability.EditDurability(player, secHandItem, -1);

						if (mainHandItem.getAmount() == 1)
						{
							player.getInventory().removeItem(mainHandItem);
							cancel = true;
						}
						else
						{
							mainHandItem.setAmount(mainHandItem.getAmount() -1);
						}
						if (cancel)
							return;
					}
				}
			}
		}
	}


	public boolean Plant(Block block, Material newMat)
	{
		if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) //???
		{
			if (block.getRelative(BlockFace.DOWN).getType() == Material.FARMLAND)
			{
				block.setType(newMat);
				return true;
			}
		}
		return false;
	}

}
