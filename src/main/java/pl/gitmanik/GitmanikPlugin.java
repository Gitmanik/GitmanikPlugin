package pl.gitmanik;

import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import pl.gitmanik.enchants.PrzychylnoscBogow;
import pl.gitmanik.enchants.TunnelDigger;
import pl.gitmanik.nightskip.NightSkipping;
import pl.gitmanik.nightskip.VoteSkipNightHandler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GitmanikPlugin extends JavaPlugin {

    public static GitmanikPlugin gitmanikplugin;

    public static TunnelDigger tunneldigger;
    public static PrzychylnoscBogow przychylnoscBogow;

    public static NightSkipping nightskipping = new NightSkipping();

    public void AddEnchantWithLore(ItemStack item, Enchantment ench)
    {
        item.addEnchantment(ench, 1);

        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore();

        if (lore == null)
            lore = new ArrayList<>();

        lore.add(ench.getName());
        im.setLore(lore);
        item.setItemMeta(im);

    }

    @Override
    public void onEnable() {
        gitmanikplugin = this;
        tunneldigger = new TunnelDigger(new NamespacedKey(this, "tunneldigger"));
        przychylnoscBogow = new PrzychylnoscBogow(new NamespacedKey(this, "przychylnosc"));
        registerEnchants(tunneldigger);
        registerEnchants(przychylnoscBogow);
        try
            {
                if (!Files.exists(Paths.get(gitmanikplugin.getDataFolder().getAbsolutePath())))
                    Files.createDirectory(Paths.get(gitmanikplugin.getDataFolder().getAbsolutePath()));

                if (!Files.exists(Paths.get(gitmanikplugin.getDataFolder().getAbsolutePath() + "/afkfarma.log")))
                Files.createFile(Paths.get(gitmanikplugin.getDataFolder().getAbsolutePath() + "/afkfarma.log"));
            } catch (IOException e)
            {
                e.printStackTrace();
            }

//        this.getCommand("gtmlae").setExecutor(new ListAllEntities());
        this.getCommand("p").setExecutor(new StackPotions());
        this.getCommand("le").setExecutor(new ListEnchants());

        this.getCommand("gtmvoteskipnight").setExecutor(new VoteSkipNightHandler());


        getServer().getScheduler().scheduleSyncRepeatingTask(this, nightskipping, 0L, 60L);
        Bukkit.getPluginManager().registerEvents(new OreHandler(), this);
//        Bukkit.getPluginManager().registerEvents(new AntiFishingAFK(), this);
        Bukkit.getPluginManager().registerEvents(new DeathHandler(), this);
        getLogger().info("Running.");

    }
    @Override
    public void onDisable() {
        getLogger().info("Bye.");
    }

    private void registerEnchants(Enchantment ench){
        try{
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Enchantment.registerEnchantment(ench);
                getLogger().log(Level.INFO, "Registered enchantment "+ench.getName()+ " with code "  + ench.getKey().toString());
            } catch (IllegalArgumentException e){
                //if this is thrown it means the id is already taken.
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}