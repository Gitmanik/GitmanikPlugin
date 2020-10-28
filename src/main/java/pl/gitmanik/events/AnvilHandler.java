package pl.gitmanik.events;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import pl.gitmanik.enchants.EnchantmentHelper;

import java.util.Map;

public class AnvilHandler implements Listener
{
	@EventHandler
	public void onAnvil(PrepareAnvilEvent e) {
		AnvilInventory ai = e.getInventory();
		ItemStack firstItem = ai.getFirstItem();
		ItemStack secondItem = ai.getSecondItem();
		if(firstItem != null) {
			if(firstItem.containsEnchantment(EnchantmentHelper.GetEnchantment("tunneldigger"))) {
				ItemStack item = firstItem.clone();
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§d" + ai.getRenameText());
				if (secondItem != null)
				{
					if (secondItem.getType() == Material.DIAMOND)
					{
						if (meta instanceof Damageable){
							Damageable durability = (Damageable) meta;
							if (durability.hasDamage()){
								durability.setDamage(Math.min(Material.DIAMOND_PICKAXE.getMaxDurability(), durability.getDamage() - (Material.DIAMOND_PICKAXE.getMaxDurability() /3) * secondItem.getAmount()));
							}
						}
					}
					else if (secondItem.getType() == Material.DIAMOND_PICKAXE){
						if (meta instanceof Damageable){
							Damageable durability = (Damageable) meta;
							if (durability.hasDamage()){
								durability.setDamage(0);
							}
						}
					}
					if (secondItem.getItemMeta() instanceof EnchantmentStorageMeta)
					{
						for (Map.Entry<Enchantment, Integer> entry : ((EnchantmentStorageMeta) secondItem.getItemMeta()).getStoredEnchants().entrySet())
						{
							Enchantment enchant = entry.getKey();
							Integer level = entry.getValue();

							if (meta.getEnchantLevel(enchant) == level)
								level++;

							meta.addEnchant(enchant, Math.max(meta.getEnchantLevel(enchant), Math.min(level, enchant.getMaxLevel())), false);
						}
					}
					else
					{
						secondItem.getEnchantments().forEach((key, value) ->
								meta.addEnchant(key, value, false));
					}

				}
				item.setItemMeta(meta);
				e.setResult(item);
			}
		}
	}
}
