package pl.gitmanik.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.enchants.EnchantmentHelper;

public class DiamondSystem implements Listener
{
	private final double baseChance = GitmanikPlugin.gp.getConfig().getDouble("diamondsystem.baseDropChance");
	private final double fortuneChance = GitmanikPlugin.gp.getConfig().getDouble("diamondsystem.fortuneDropChance");
	private final double blessingDrop = GitmanikPlugin.gp.getConfig().getDouble("blessingsystem.drop");

	private final Enchantment diamentowaAsceza = EnchantmentHelper.GetEnchantment("diamentowaasceza");
	private final ItemStack bozek = GitmanikPlugin.customItems.get("blogoslawienstwo-nieumarlych");

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();

		if (player.getGameMode() == GameMode.CREATIVE)
			return;

		if (block.getType() == Material.DIAMOND_ORE)
		{
			handleDiamondBlock(player, block);
			event.setCancelled(true);
			block.setType(Material.AIR);
			block.getState().update();
		}
	}

	private void handleDiamondBlock(Player player, Block block)
	{
		PlayerInventory inv = player.getInventory();
		ItemStack itemHand = inv.getItemInMainHand();
		World world = player.getWorld();
		int fort = itemHand.getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_BLOCKS, 0);
		int da = itemHand.getEnchantments().getOrDefault(diamentowaAsceza, 0);

		if (da != 1){
			if (Math.random() < baseChance + fortuneChance * fort) {
				Bukkit.broadcastMessage(ChatColor.AQUA + "Właśnie wydobyto diament przez " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + ".");
				player.giveExp(4 + GitmanikPlugin.rand.nextInt(4));
				world.dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND, 1));
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Diament w trakcie kopania się zniszczył.");
				if (Math.random() < fortuneChance) { //3% szans na drop bez fortuny, z fortuną 1 2%, z fortuną 2 1%, z fortuną 3 0%
					Bukkit.broadcastMessage(ChatColor.AQUA + "Bogowie obdarzyli błogosławieństwem " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + "!");
					world.dropItemNaturally(block.getLocation(), bozek);
				}
			}
		}
		else
		{
			if (Math.random() < blessingDrop) //czyli mamy 10% na drop
			{
				player.sendMessage(ChatColor.AQUA + "Bogowie wynagrodzili ascezę " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + "!");
				world.dropItemNaturally(block.getLocation(), bozek);
			}
		}
	}
}
