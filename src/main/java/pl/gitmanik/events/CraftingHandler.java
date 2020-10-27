package pl.gitmanik.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import pl.gitmanik.GitmanikPlugin;

public class CraftingHandler implements Listener
{
	private final GitmanikPlugin plugin;

	public CraftingHandler(GitmanikPlugin gitmanikPlugin)
	{
		this.plugin = gitmanikPlugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onCraft(PrepareItemCraftEvent event)
	{
		ItemStack mruwiKilof = GitmanikPlugin.customItems.get("mruwi_kilof");
		ItemStack mruwiKlejnot = GitmanikPlugin.customItems.get("mruwi_klejnot");

		CraftingInventory inv = event.getInventory();
		
		if (inv.getResult() != null && event.getInventory().getResult().equals(mruwiKilof))
		{
			for(ItemStack item : inv.getMatrix()){
				if (item != null && item.getType() == Material.LAPIS_BLOCK && !item.equals(mruwiKlejnot)){
					inv.setResult(null);
				}
			}

			ItemStack i = event.getInventory().getItem(5);
			if (i != null && i.getType() == Material.DIAMOND_PICKAXE)
			{
				Damageable d = (Damageable) i.getItemMeta();
				if (d.getDamage() > 0)
				{
					inv.setResult(null);
				}
			}
		}
	}
}
