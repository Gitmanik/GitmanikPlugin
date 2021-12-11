package pl.gitmanik.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
	private final Enchantment tunnelDigger = EnchantmentHelper.GetEnchantment("tunneldigger");

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
		Player player = event.getPlayer();
		PlayerInventory inv = player.getInventory();
		ItemStack hand = inv.getItemInMainHand();

		if (player.getGameMode() == GameMode.CREATIVE)
			return;

		int mrValue = hand.getEnchantments().getOrDefault(tunnelDigger, 0);

		if (mrValue > 0) {

			if (!(allowedBlocks.contains(block.getType())))
				return;

			int blockOffset = player.getLocation().getBlockY() - block.getY();
			if (blockOffset == 0 || blockOffset == -1) {
				//TODO: Edge-case z diamentami.
				Mine(hand, block.getLocation().add(0, blockOffset == 0 ? 1 : -1, 0).getBlock());
			}
		}
	}


	private void Mine(ItemStack hand, Block b)
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
			}
		}
	}
}
