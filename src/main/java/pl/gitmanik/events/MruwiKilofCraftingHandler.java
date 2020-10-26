package pl.gitmanik.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import pl.gitmanik.GitmanikPlugin;

public class MruwiKilofCraftingHandler implements Listener {

    @EventHandler
    public void onMruwiKilofCraft(PrepareItemCraftEvent event){
        CraftingInventory inv = event.getInventory();

        if(inv.getResult() != null && inv.getResult().equals(GitmanikPlugin.mruwiKilof)){

            for(ItemStack item : inv.getMatrix()){
                if (item != null && item.getType() == Material.LAPIS_BLOCK && !item.equals(GitmanikPlugin.mruwiKlejnot)){
                    inv.setResult(null);
                }
            }
        }
    }
}
