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

public class PlantHandler implements Listener
{
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		ItemStack sechand = player.getInventory().getItemInOffHand();
		ItemStack hand = player.getInventory().getItemInMainHand();

		Enchantment rekaFarmera = EnchantmentHelper.GetEnchantment("rekafarmera");
		Enchantment depoEnchant = EnchantmentHelper.GetEnchantment("depoenchant");

		assert rekaFarmera != null;
		assert depoEnchant != null;

		if (GitmanikPlugin.compressedItems.containsValue(event.getItemInHand()) ||
//				GitmanikPlugin.customItems.containsValue(event.getItemInHand() ||
				event.getItemInHand().getEnchantmentLevel(rekaFarmera) > 0 ||
				event.getItemInHand().getEnchantmentLevel(depoEnchant) > 0)
		{
			event.setCancelled(true);
			return;
		}

		if (sechand.getEnchantmentLevel(rekaFarmera) == 1 && block.getBlockData() instanceof Ageable)
		{
			hand.setAmount(hand.getAmount() - 1);
			for (int x = -1; x <=1; x++)
			{
				for (int z = -1; z <=1; z++)
				{
					if (hand.getAmount() <= 0)
						return;

					if (Plant(player.getWorld().getBlockAt(block.getX() + x,block.getY(), block.getZ() + z), block.getType()))
						{
							if (hand.getAmount() == 1)
							{
								player.getInventory().removeItem(hand);
								return;
							}
							else
							{
								hand.setAmount(hand.getAmount() -1);
								if (!GitmanikDurability.EditDurability(player, sechand, -1))
									return;
							}
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
