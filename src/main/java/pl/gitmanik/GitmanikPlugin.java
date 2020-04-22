package pl.gitmanik;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.gitmanik.nightskip.NightSkipping;
import pl.gitmanik.nightskip.VoteSkipNightHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitmanikPlugin extends JavaPlugin {

    public static GitmanikPlugin gitmanikplugin;

    public static NightSkipping nightskipping = new NightSkipping();

    @Override
    public void onEnable() {
        gitmanikplugin = this;

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

        this.getCommand("gtmlae").setExecutor(new ListAllEntities());
        this.getCommand("p").setExecutor(new StackPotions());


        this.getCommand("gtmvoteskipnight").setExecutor(new VoteSkipNightHandler());


        getServer().getScheduler().scheduleSyncRepeatingTask(this, nightskipping, 0L, 60L);
        Bukkit.getPluginManager().registerEvents(new OreHandler(), this);
        Bukkit.getPluginManager().registerEvents(new AntiFishingAFK(), this);
        getLogger().info("Running.");

    }
    @Override
    public void onDisable() {
        getLogger().info("Bye.");
    }
}