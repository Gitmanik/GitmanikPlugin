package pl.gitmanik.enchants;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class TunnelDigger extends Enchantment
{
	public static final String NAZWA_ENCHANTU = ChatColor.ITALIC + "" + ChatColor.GOLD + "Mruwia Reka";
	public TunnelDigger(NamespacedKey key)
	{
		super(key);
	}

	@Override
	public String getName()
	{
		return NAZWA_ENCHANTU;
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
