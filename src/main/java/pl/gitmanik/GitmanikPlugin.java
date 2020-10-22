package pl.gitmanik;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import pl.gitmanik.commands.GPAdmin;
import pl.gitmanik.commands.Homesystem;
import pl.gitmanik.commands.StackPotions;
import pl.gitmanik.commands.Teleportsystem;
import pl.gitmanik.enchants.*;
import pl.gitmanik.events.*;
import pl.gitmanik.helpers.GitmanikDurability;
import pl.gitmanik.nightskip.NightSkipping;
import pl.gitmanik.nightskip.VoteSkipNightHandler;

public class GitmanikPlugin extends JavaPlugin {

    public static GitmanikPlugin gitmanikplugin;

    public static TunnelDigger mruwiaReka;
    public static PrzychylnoscBogow przychylnoscBogow;
    public static FarmersHand rekaFarmera;
    public static DepoEnchant depoEnchant;

    public static ItemStack mruwiKilof, magicznaOrchidea, enderowyDepozyt;

    public static NightSkipping nightskipping = new NightSkipping();

    @Override
    public void onEnable() {
        gitmanikplugin = this;

        RegisterCustomEnchants();

        RegisterCommands();
        GenerateCustomItemStacks();
        try
        {
            GenerateCustomRecipes();
            GenerateChainmailRecipes();
        }
        catch (Exception ignored){}

        getServer().getScheduler().scheduleSyncRepeatingTask(this, nightskipping, 0L, 60L);

        Bukkit.getPluginManager().registerEvents(new OreHandler(), this);
        Bukkit.getPluginManager().registerEvents(new DeathHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlantHandler(), this);
        Bukkit.getPluginManager().registerEvents(new DepositHandler(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilHandler(), this);

        getLogger().info("Running.");

    }

    private void RegisterCommands()
    {
        //Admin
        this.getCommand("gpadmin").setExecutor(new GPAdmin());

        //QoL
        this.getCommand("p").setExecutor(new StackPotions());

        //Home system
        Homesystem homesystem = new Homesystem();
        this.getCommand("dom").setExecutor(homesystem);
        this.getCommand("ustawdom").setExecutor(homesystem);

        Teleportsystem teleportsystem = new Teleportsystem();
        this.getCommand("gtpa").setExecutor(teleportsystem);
        this.getCommand("gtpaccept").setExecutor(teleportsystem);

        //Helper
        this.getCommand("gtmvoteskipnight").setExecutor(new VoteSkipNightHandler());
    }

    private void RegisterCustomEnchants()
    {
        mruwiaReka = new TunnelDigger(new NamespacedKey(this, "tunneldigger"));
        przychylnoscBogow = new PrzychylnoscBogow(new NamespacedKey(this, "przychylnosc"));
        rekaFarmera = new FarmersHand(new NamespacedKey(this, "rekafarmera"));
        depoEnchant = new DepoEnchant(new NamespacedKey(this, "depoenchant"));

        EnchantmentHelper.registerEnchant(this, rekaFarmera);
        EnchantmentHelper.registerEnchant(this, mruwiaReka);
        EnchantmentHelper.registerEnchant(this, przychylnoscBogow);
        EnchantmentHelper.registerEnchant(this, depoEnchant);
    }

    private void GenerateChainmailRecipes()
    {
        //HELM_KOLCZUGA
        ItemStack helmKolczuga = new ItemStack(Material.CHAINMAIL_HELMET);
        ShapedRecipe helmKolczugaRecipe = new ShapedRecipe(new NamespacedKey(this, "helm_kolczuga"), helmKolczuga);

        helmKolczugaRecipe.shape("CCC", "C C", "   ");
        helmKolczugaRecipe.setIngredient('C', Material.CHAIN);

        Bukkit.addRecipe(helmKolczugaRecipe);
        // ------------------------------------

        //KLATA_KOLCZUGA
        ItemStack klataKolczuga = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ShapedRecipe klataKolczugaRecipe = new ShapedRecipe(new NamespacedKey(this, "klata_kolczuga"), klataKolczuga);

        klataKolczugaRecipe.shape("C C", "CCC", "CCC");
        klataKolczugaRecipe.setIngredient('C', Material.CHAIN);

        Bukkit.addRecipe(klataKolczugaRecipe);
        // ------------------------------------

        //SPODNIE_KOLCZUGA
        ItemStack spodnieKolczuga = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ShapedRecipe spodnieKolczugaRecipe = new ShapedRecipe(new NamespacedKey(this, "spodnie_kolczuga"), spodnieKolczuga);

        spodnieKolczugaRecipe.shape("CCC", "C C", "C C");
        spodnieKolczugaRecipe.setIngredient('C', Material.CHAIN);

        Bukkit.addRecipe(spodnieKolczugaRecipe);
        // ------------------------------------

        //BUTY_KOLCZUGA
        ItemStack butyKolczuga = new ItemStack(Material.CHAINMAIL_BOOTS);
        ShapedRecipe butyKolczugaRecipe = new ShapedRecipe(new NamespacedKey(this, "buty_kolczuga"), butyKolczuga);

        butyKolczugaRecipe.shape("   ", "C C", "C C");
        butyKolczugaRecipe.setIngredient('C', Material.CHAIN);

        Bukkit.addRecipe(butyKolczugaRecipe);
        // ------------------------------------
    }

    private void GenerateCustomRecipes()
    {
        //MRUWI KILOF
        ShapedRecipe mruwiRecip = new ShapedRecipe(new NamespacedKey(this, "mruwi_kilof"), mruwiKilof);
        mruwiRecip.shape("GRG", "IKI", "IRI");
        mruwiRecip.setIngredient('G', Material.GOLD_BLOCK);
        mruwiRecip.setIngredient('R', Material.REDSTONE_BLOCK);
        mruwiRecip.setIngredient('I', Material.IRON_BLOCK);
        mruwiRecip.setIngredient('K', Material.DIAMOND_PICKAXE);
        Bukkit.addRecipe(mruwiRecip);

        //MAGICZNA ORCHIDEA
        ShapedRecipe orchidearecipe = new ShapedRecipe(new NamespacedKey(this, "magiczna_orchidea"), magicznaOrchidea);
        orchidearecipe.shape("LHL", "HOH", "LHL");
        orchidearecipe.setIngredient('O', Material.BLUE_ORCHID);
        orchidearecipe.setIngredient('L', Material.LAPIS_BLOCK);
        orchidearecipe.setIngredient('H', Material.IRON_HOE);
        Bukkit.addRecipe(orchidearecipe);

        //ENDEROWY DEPOZYT
        ShapedRecipe depo = new ShapedRecipe(new NamespacedKey(this, "mroczny_depozyt"), enderowyDepozyt);
        depo.shape("PPP", "NGN", "NNN");
        depo.setIngredient('P', Material.ENDER_PEARL);
        depo.setIngredient('G', Material.GLOWSTONE_DUST);
        depo.setIngredient('N', Material.NETHERRACK);
        Bukkit.addRecipe(depo);
    }

    private void GenerateCustomItemStacks()
    {
        //-------------------------------
        ItemStack mruwiKilof = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta mruwiaMeta = mruwiKilof.getItemMeta();
        mruwiaMeta.setDisplayName("§dMruwi Kilof");
        mruwiKilof.setItemMeta(mruwiaMeta);
        EnchantmentHelper.AddEnchantWithLore(mruwiKilof, mruwiaReka, 1);
        GitmanikPlugin.mruwiKilof = mruwiKilof;
        //-------------------------------------------

        //KWIAT
        ItemStack magicznaOrchidea = new ItemStack(Material.BLUE_ORCHID);
        ItemMeta orchideaMeta = magicznaOrchidea.getItemMeta();
        orchideaMeta.setDisplayName("§dMagiczna Orchidea");
        magicznaOrchidea.setItemMeta(orchideaMeta);
        EnchantmentHelper.AddEnchantWithLore(magicznaOrchidea, rekaFarmera, 1);
        GitmanikDurability.SetDurability(magicznaOrchidea, 1000);
        GitmanikPlugin.magicznaOrchidea = magicznaOrchidea;
        // ------------------------------------

        //GRZYB
        ItemStack mrocznyDepozyt = new ItemStack(Material.CRIMSON_FUNGUS);
        ItemMeta epozyt = mrocznyDepozyt.getItemMeta();
        epozyt.setDisplayName("§dEnderowy Depozyt");
        mrocznyDepozyt.setItemMeta(epozyt);
        mrocznyDepozyt.addEnchantment(depoEnchant, 1);
        GitmanikDurability.SetDurability(mrocznyDepozyt, 25);
        GitmanikPlugin.enderowyDepozyt = mrocznyDepozyt;
        // -----------------------------------
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye.");
    }
}