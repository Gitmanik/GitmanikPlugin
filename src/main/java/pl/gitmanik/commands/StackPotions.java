package pl.gitmanik.commands;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StackPotions implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		Player p = (Player) commandSender;
		if (!p.hasPermission(("gitmanik.stackpotions")))
			return false;

		List<ItemStack> allPotions = Arrays.stream(p.getInventory().getContents()).filter(xx -> (xx != null && (xx.getType() == Material.LINGERING_POTION || xx.getType() == Material.POTION || xx.getType() == Material.SPLASH_POTION))).collect(Collectors.toList());
		ArrayList<ItemStack> newPotions = new ArrayList<>();

		for (ItemStack potion : distinct(allPotions))
		{
			int amount = getAmount(allPotions, potion);
			for (int bb = 0; bb < Math.floor( amount/ 4.0); bb++)
			{
				ItemStack newPotion = new ItemStack(potion.getType(), 4);
				newPotion.setItemMeta(potion.getItemMeta());
				newPotions.add(newPotion);
			}

			ItemStack newPotion = new ItemStack(potion.getType(), amount % 4);
			newPotion.setItemMeta(potion.getItemMeta());
			newPotions.add(newPotion);
		}

		allPotions.forEach(abc -> p.getInventory().remove(abc));
		newPotions.forEach(nowa -> p.getInventory().addItem(nowa));

		return true;

	}

	private ArrayList<ItemStack> distinct(List<ItemStack> all)
	{
		ArrayList<ItemStack> tor = new ArrayList<>();
		for (ItemStack item : all)
		{
			if (tor.stream().noneMatch(x -> isSamePotion(x, item)))
			{
				tor.add(item);
			}
		}
		return tor;
	}

	private int getAmount(List<ItemStack> all, ItemStack type)
	{
		int tor = 0;

		List<ItemStack> a = all.stream().filter(predicate -> isSamePotion(predicate, type)).collect(Collectors.toList());

		for (ItemStack item : a)
		{
			tor += item.getAmount();
		}

		return tor;
	}

	private boolean isSamePotion(ItemStack one, ItemStack two)
	{
		String hhhh = one.getItemMeta().toString();
		String b = two.getItemMeta().toString();

		return hhhh.equals(b) && one.getType() == two.getType();
	}
}
