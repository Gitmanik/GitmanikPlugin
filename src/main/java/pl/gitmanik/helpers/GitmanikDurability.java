package pl.gitmanik.helpers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GitmanikDurability
{
	private static final String DURABILITY = "Wytrzymałość: ";

	public static int GetDurability(ItemStack stack)
	{
		ItemMeta im = stack.getItemMeta();
		List<String> lore = im.getLore();

		AtomicInteger toret = new AtomicInteger(1);

		if (lore == null)
			lore = new ArrayList<>();

		lore.forEach((a) -> {
			if (a.startsWith(DURABILITY))
			{
				toret.set(Integer.parseInt(a.substring(a.indexOf(" ") + 1)));
			}
		});
		return toret.get();
	}

	public static void SetDurability(ItemStack stack, int newDurability)
	{
		ItemMeta im = stack.getItemMeta();
		List<String> lore = im.getLore();

		if (lore == null)
			lore = new ArrayList<>();

		AtomicBoolean saved = new AtomicBoolean(false);
		for (int i = 0; i < lore.size(); i++)
		{
			if (lore.get(i).startsWith(DURABILITY))
			{
				lore.set(i, DURABILITY + newDurability);
				saved.set(true);
			}
		};

		if (!saved.get())
			lore.add(DURABILITY + newDurability);

		im.setLore(lore);
		stack.setItemMeta(im);
	}

	public static void EditDurability(Player player, ItemStack hand, int i)
	{
		int dur = GetDurability(hand) + i;
		if (dur <= 0)
		{
			player.sendMessage(ChatColor.RED + "Twój przedmiot " + hand.getI18NDisplayName() + " uległ zniszczeniu!");
			player.getInventory().removeItemAnySlot(hand);
			return;
		}
		SetDurability(hand, dur);
	}
}
