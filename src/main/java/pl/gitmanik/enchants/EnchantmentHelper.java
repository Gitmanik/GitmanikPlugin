package pl.gitmanik.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EnchantmentHelper
{
	public static void AddEnchantWithLore(ItemStack item, Enchantment ench, int level)
	{
		item.addEnchantment(ench, level);

		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();

		if (lore == null)
			lore = new ArrayList<>();

		lore.add(ench.getName() + " " + level);
		im.setLore(lore);
		item.setItemMeta(im);
	}

	public static void registerEnchant(Plugin p, Enchantment ench){
		try{
			try {
				Field f = Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f.set(null, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Enchantment.registerEnchantment(ench);
				p.getLogger().log(Level.INFO, "Registered enchantment "+ench.getName()+ " with code "  + ench.getKey().toString());
			} catch (IllegalArgumentException e){
				//if this is thrown it means the id is already taken.
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
