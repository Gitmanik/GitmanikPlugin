package pl.gitmanik;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;

public class OreHandler implements Listener
{
	private Random rand = new Random();

	public static final String NAZWA_ENCHANTU = ChatColor.ITALIC + "" + ChatColor.LIGHT_PURPLE + "Przychylność Bogów";

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();

		if (block.getType() == Material.DIAMOND_ORE) {
			BlockState state = block.getState();
			Player player = event.getPlayer();
			PlayerInventory inv = player.getInventory();

			int fort =  inv.getItemInMainHand().getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_BLOCKS, 0);
			World world = player.getWorld();

			if (player.getGameMode() != GameMode.CREATIVE) {

				if (Math.random() < 0.7 + 0.10 * fort) {
					player.sendMessage(ChatColor.BLUE + "Gratulacje : ) Wykopał*ś diament!");
					Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.BLUE + " właśnie wykopał* diament!");
					player.giveExp(4 + rand.nextInt(4));
					world.dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND, 1));
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Zniszczył*ś diament w trakcie kopania : (");
					if (Math.random() < 0.10) {
						player.sendMessage(ChatColor.GOLD + "Bogowie podarowali Ci swoje błogosławieństwo!");
						ItemStack stack = new ItemStack(Material.EMERALD, 1);
						ItemMeta meta = stack.getItemMeta();
						meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 10, true);
						meta.setDisplayName(ChatColor.GOLD + "Błogosławieństwo Nieumarłych");
						stack.setItemMeta(meta);
						world.dropItemNaturally(block.getLocation(), stack);
					}
				}
			}

			event.setCancelled(true);
			block.setType(Material.AIR);
			state.update();
		}
	}
}
