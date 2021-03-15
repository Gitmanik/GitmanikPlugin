package pl.gitmanik.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import pl.gitmanik.GitmanikPlugin;
import pl.gitmanik.enchants.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;

public class TunneldiggerHandler implements Listener
{
	private final ArrayList<Material> allowedBlocks = new ArrayList<>();
	private final double baseChance = GitmanikPlugin.gp.getConfig().getDouble("diamondsystem.baseDropChance");
	private final double fortuneChance = GitmanikPlugin.gp.getConfig().getDouble("diamondsystem.fortuneDropChance");
	private final double blessingDrop = GitmanikPlugin.gp.getConfig().getDouble("blessingsystem.drop");

	private final Enchantment tunnelDigger = EnchantmentHelper.GetEnchantment("tunneldigger");
	private final Enchantment diamentowaAsceza = EnchantmentHelper.GetEnchantment("diamentowaasceza");
	private final ItemStack bozek = GitmanikPlugin.customItems.get("blogoslawienstwo-nieumarlych");

	public TunneldiggerHandler()
	{
		List<String> allowedBlocksList = GitmanikPlugin.gp.getConfig().getStringList("allowed_TUNNELDIGGER_blockList");
		for (String n : allowedBlocksList)
		{
			allowedBlocks.add(Material.valueOf(n));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		BlockState state = block.getState();
		Player player = event.getPlayer();
		PlayerInventory inv = player.getInventory();
		ItemStack hand = inv.getItemInMainHand();

		if (player.getGameMode() == GameMode.CREATIVE)
			return;

		if (!(allowedBlocks.contains(block.getType())))
			return;

		if (block.getType() == Material.DIAMOND_ORE)
		{
			handleDiamondBlock(player, block);
			event.setCancelled(true);
			block.setType(Material.AIR);
			state.update();
		}

		int mrValue = hand.getEnchantments().getOrDefault(tunnelDigger, 0);

		if (mrValue > 0) {
			int blockOffset = player.getLocation().getBlockY() - block.getY();
			if (blockOffset == 0 || blockOffset == -1) {
				Mine(player, hand, block.getLocation().add(0, blockOffset == 0 ? 1 : -1, 0).getBlock());
			}
		}
	}


	private void Mine(Player player, ItemStack hand, Block b)
	{
		if (allowedBlocks.contains(b.getType())){
			b.breakNaturally(hand);

			if (!b.getDrops(hand).isEmpty())
			{
				int unbValue = hand.getEnchantments().getOrDefault(Enchantment.DURABILITY, 0);
				if (GitmanikPlugin.rand.nextDouble() > unbValue * 0.2)
				{
					ItemMeta itemMeta = hand.getItemMeta();
					if (itemMeta instanceof Damageable)
					{
						Damageable d = (Damageable) itemMeta;
						d.setDamage(d.getDamage() + (GitmanikPlugin.rand.nextInt(4)));
						hand.setItemMeta(itemMeta);
					}
				}

				if (b.getType() == Material.DIAMOND_ORE){
					handleDiamondBlock(player, b);
					b.setType(Material.AIR);
				}
			}
		}
	}

	private void handleDiamondBlock(Player player, Block block)
	{
		if (player.getGameMode() != GameMode.CREATIVE && block.getType() == Material.DIAMOND_ORE) {

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
}
