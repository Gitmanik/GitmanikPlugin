package pl.gitmanik.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.enchants.EnchantmentHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BozekHandler implements Listener
{
	private Enchantment przychylnosc = EnchantmentHelper.GetEnchantment("przychylnosc");
	private final double blessingDrop = GitmanikPlugin.gp.getConfig().getDouble("blessingsystem.drop");
	private final Enchantment diamentowaAsceza = EnchantmentHelper.GetEnchantment("diamentowaasceza");
	private final ItemStack bozek = GitmanikPlugin.customItems.get("blogoslawienstwo-nieumarlych");

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		Player player = e.getEntity();

		List<ItemStack> bozekFinder = Arrays.stream(player.getInventory().getContents()).filter(candidate -> (candidate != null && candidate.getType() == Material.EMERALD)).collect(Collectors.toList());
		for (ItemStack item : bozekFinder)
		{
			if (item.getEnchantmentLevel(przychylnosc) == 1 || item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) == 10)
			{
				item.setAmount(item.getAmount() - 1);
				e.setKeepInventory(true);
				e.setKeepLevel(true);
				e.setDroppedExp(0);
				e.getDrops().clear();
				e.setDeathMessage(ChatColor.GOLD + "Gracza " + player.getDisplayName() + " uśmiercono, ale Nieumarli zlitowali się nad nim i przywrócili mu dobytek!");
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		PlayerInventory inv = player.getInventory();
		ItemStack itemHand = inv.getItemInMainHand();
		World world = player.getWorld();
		int da = itemHand.getEnchantments().getOrDefault(diamentowaAsceza, 0);

		if (player.getGameMode() == GameMode.CREATIVE)
			return;

		if (block.getType() == Material.DIAMOND_ORE)
		{
			if (da == 1){
				event.setCancelled(true);
				block.setType(Material.AIR);
				block.getState().update();

				if (Math.random() < blessingDrop) //czyli mamy 10% na drop
				{
					player.sendMessage(ChatColor.AQUA + "Bogowie wynagrodzili ascezę " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + "!");
					world.dropItemNaturally(block.getLocation(), bozek);
				}
			}
		}
	}
}
