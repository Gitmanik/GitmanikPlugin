package pl.gitmanik.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
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

	@EventHandler
	public void onCraft(PrepareItemCraftEvent event)
	{
		if (event.getInventory().getResult() == GitmanikPlugin.mruwiKilof)
		{
			ItemStack i = event.getInventory().getMatrix()[4];
			if (i != null && i.getType() == Material.DIAMOND_PICKAXE)
			{
				Damageable d = (Damageable) i.getItemMeta();
				if (d.getDamage() > 0)
					event.getInventory().setResult(null);
			}


		}
	}
}
