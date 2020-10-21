package pl.gitmanik.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import pl.gitmanik.GitmanikPlugin;

import java.util.Random;
public class OreHandler implements Listener
{
	private Random rand = new Random();

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		BlockState state = block.getState();
		Player player = event.getPlayer();
		PlayerInventory inv = player.getInventory();
		ItemStack hand = inv.getItemInMainHand();

		if (block.getType() == Material.DIAMOND_ORE)
		{
			handleDiamondBlock(player, block);
			event.setCancelled(true);
			block.setType(Material.AIR);
			state.update();
		}

		int td = hand.getEnchantments().getOrDefault(GitmanikPlugin.tunneldigger, 0);
		if (td == 1)
		{
			Block b = block.getRelative(BlockFace.DOWN);
			Mine(player, hand, b);
		}
	}

	private void Mine(Player player, ItemStack hand, Block b)
	{
		if (!b.getDrops(hand).isEmpty())
		{
			if (hand.getItemMeta() instanceof Damageable)
			{
				((Damageable) hand.getItemMeta()).damage(1);
			}
			if (b.getType() == Material.DIAMOND_ORE){
				handleDiamondBlock(player, b);
				b.setType(Material.AIR);
			}

			else
				b.breakNaturally(hand);
		}
	}

	private void handleDiamondBlock(Player player, Block block)
	{

		PlayerInventory inv = player.getInventory();
		World world = player.getWorld();

		if (block.getType() == Material.DIAMOND_ORE) {

			int fort =  inv.getItemInMainHand().getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_BLOCKS, 0);

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
						meta.addEnchant(GitmanikPlugin.przychylnoscBogow, 1, true);
						meta.setDisplayName(ChatColor.GOLD + "Błogosławieństwo Nieumarłych");
						stack.setItemMeta(meta);
						world.dropItemNaturally(block.getLocation(), stack);
					}
				}
			}
		}
	}
}