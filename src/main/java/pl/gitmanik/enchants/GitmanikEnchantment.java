package pl.gitmanik.enchants;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;

public class GitmanikEnchantment extends Enchantment
{
	private String name;
	public GitmanikEnchantment(String key, String name)
	{
		super(new NamespacedKey(GitmanikPlugin.gitmanikplugin, key));
		this.name = name;
	}

	@Override
	public String getName()
	{
		return ChatColor.ITALIC + name;
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}

	@Override
	public int getStartLevel()
	{
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget()
	{
		return EnchantmentTarget.ALL;
	}

	@Override
	public boolean isTreasure()
	{
		return false;
	}

	@Override
	public boolean isCursed()
	{
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchantment)
	{
		return false;
	}

	@Override
	public boolean canEnchantItem(ItemStack itemStack)
	{
		return true;
	}
}
