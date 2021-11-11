package pl.gitmanik.enchants;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.gitmanik.GitmanikPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GitmanikEnchantment extends Enchantment
{
	private String name;
	public GitmanikEnchantment(String key, String name)
	{
		super(new NamespacedKey(GitmanikPlugin.gp, key));
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

	@Override
	public @NotNull Component displayName(int i)
	{
		return Component.text(getName());
	}

	@Override
	public boolean isTradeable() {return false;}

	@Override
	public boolean isDiscoverable() {return false;}

	@Override
	public @NotNull EnchantmentRarity getRarity() {return EnchantmentRarity.VERY_RARE;}

	@Override
	public float getDamageIncrease(int i, @NotNull EntityCategory entityCategory) {return 0;}

	@Override
	public @NotNull Set<EquipmentSlot> getActiveSlots() {return new HashSet<>(Arrays.asList(EquipmentSlot.OFF_HAND, EquipmentSlot.HAND, EquipmentSlot.HEAD, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST));}

	@Override
	public @NotNull String translationKey()
	{
		return "enchantment.gitmanik.gitmanikenchantmentgeneric";
	}
}
