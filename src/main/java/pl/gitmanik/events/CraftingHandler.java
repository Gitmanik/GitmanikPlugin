package pl.gitmanik.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import pl.gitmanik.GitmanikPlugin;

public class CraftingHandler implements Listener
{
	ItemStack mruwiKilof = GitmanikPlugin.customItems.get("mruwi_kilof");
	ItemStack mruwiKlejnot = GitmanikPlugin.customItems.get("mruwi_klejnot");

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onCrafted(CraftItemEvent e)
	{
		if (GitmanikPlugin.compressedItems.containsValue(e.getCurrentItem()))
		{
			e.getInventory().getItem(5).setAmount(0);
		}
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onCraft(PrepareItemCraftEvent event)
	{
		CraftingInventory inv = event.getInventory();

		ItemStack result = inv.getResult();

		if (result != null)
		{
			ItemStack middleItem = event.getInventory().getItem(5);
			if (middleItem != null)
			{
				if (result.equals(mruwiKilof))
				{
					for(ItemStack item : inv.getMatrix()){
						if (item.getType() == Material.LAPIS_BLOCK && !item.equals(mruwiKlejnot)){
							inv.setResult(null);
						}
					}


					if (middleItem.getType() == Material.DIAMOND_PICKAXE)
					{
						Damageable d = (Damageable) middleItem.getItemMeta();
						if (d.getDamage() > 0)
						{
							inv.setResult(null);
						}
					}
				}

				if (GitmanikPlugin.compressedItems.containsValue(result))
				{
					if (middleItem.getAmount() != inv.getMaxStackSize())
						inv.setResult(null);
				}

				if (GitmanikPlugin.compressedItems.containsValue(middleItem))
				{
					inv.setResult(new ItemStack(middleItem.getType(), inv.getMaxStackSize()));
				}

			}

		}
	}
}
