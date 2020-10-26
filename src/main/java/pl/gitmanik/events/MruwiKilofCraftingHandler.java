package pl.gitmanik.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;

public class MruwiKilofCraftingHandler implements Listener {

    @EventHandler
    public void onMruwiKilofCraft(CraftItemEvent event){
        CraftingInventory inv = event.getInventory();

        if(inv.getResult().isSimilar(GitmanikPlugin.mruwiKilof)){
            for(ItemStack item : inv.getMatrix()){
                if (item != null && !item.isSimilar(GitmanikPlugin.mruwiKlejnot)){
                    inv.setResult(null);
                    event.setCancelled(true);
                }
            }
        }
    }
}
