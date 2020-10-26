package pl.gitmanik;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
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

    public static ChatHandler chathandler;

    public static ItemStack mruwiKilof, magicznaOrchidea, enderowyDepozyt, mruwiKlejnot, skompresowanyCobble, skompresowanyPiasek, skompresowanyDirt;

//    public static DynmapAPI dynmap;



    public static NightSkipping nightskipping = new NightSkipping();

    @Override
    public void onEnable() {
        gitmanikplugin = this;

//        dynmap = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
//
//        if (dynmap != null)
//        {
//            Bukkit.getPluginManager().registerEvents(new ChatHandler(), this);
//            dynmap.getMarkerAPI().getMarkerSets().forEach((x) ->
//            {
//                x.getAreaMarkers().forEach((y) ->
//                {
//                    y.get
//                });
//            });
//        }
//        else
//        {
//            getLogger().info("Dynmap not found - chat range will be disabled");
//        }

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
        Bukkit.getPluginManager().registerEvents(new CraftingHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new MruwiKilofCraftingHandler(), this);


        chathandler = new ChatHandler(this);

        Bukkit.getPluginManager().registerEvents(chathandler, this);

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
        //MRUWI KLEJNOT
        ShapedRecipe mruwiKlejnotRecipe = new ShapedRecipe(new NamespacedKey(this, "mruwi_klejnot"), mruwiKlejnot);
        mruwiKlejnotRecipe.shape("LLL", "LEL", "LLL");
        mruwiKlejnotRecipe.setIngredient('L', Material.LAPIS_BLOCK);
        mruwiKlejnotRecipe.setIngredient('E', Material.ENDER_EYE);
        Bukkit.addRecipe(mruwiKlejnotRecipe);

        //MRUWI KILOF
        ShapedRecipe mruwiRecip = new ShapedRecipe(new NamespacedKey(this, "mruwi_kilof"), mruwiKilof);
        mruwiRecip.shape("LLL", "RKR", "RRR");
        mruwiRecip.setIngredient('L', Material.LAPIS_BLOCK);
        mruwiRecip.setIngredient('R', Material.IRON_BLOCK);
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



        StonecuttingRecipe cobbleToGravel = new StonecuttingRecipe(new NamespacedKey(this, "cobble_gravel"), new ItemStack(Material.GRAVEL, 2), Material.COBBLESTONE);
        Bukkit.addRecipe(cobbleToGravel);


    }

    private void GenerateCustomItemStacks()
    {

        //MRUWI KLEJNOT
        ItemStack mruwiKlejnot = new ItemStack(Material.LAPIS_BLOCK);
        ItemMeta mruwiKlejnotMeta = mruwiKlejnot.getItemMeta();
        mruwiKlejnotMeta.setDisplayName("§6Mruwi Klejnot");
        mruwiKlejnot.setItemMeta(mruwiKlejnotMeta);
        mruwiKlejnot.addUnsafeEnchantment(Enchantment.LUCK, 10);
        GitmanikPlugin.mruwiKlejnot = mruwiKlejnot;
        //------------------------------------

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

//        //SKOMPRESOWANY COBBLE
//        ItemStack skompresowanyCobble = new ItemStack(Material.COBBLESTONE);
//        ItemMeta skompresowanyC = skompresowanyCobble.getItemMeta();
//        skompresowanyC.setDisplayName("§dSkompresowany Cobble");
//        skompresowanyCobble.setItemMeta(skompresowanyC);
//        skompresowanyCobble.addEnchantment(Enchantment.DURABILITY, 10);
//        GitmanikPlugin.skompresowanyCobble = skompresowanyCobble;
//        // -----------------------------------
//
//        //SKOMPRESOWANY COBBLE
//        ItemStack skompresowanyDirt = new ItemStack(Material.DIRT);
//        ItemMeta skompresowanyD = skompresowanyDirt.getItemMeta();
//        skompresowanyD.setDisplayName("§dSkompresowany Dirt");
//        skompresowanyDirt.setItemMeta(skompresowanyD);
//        skompresowanyDirt.addEnchantment(Enchantment.DURABILITY, 10);
//        GitmanikPlugin.skompresowanyDirt = skompresowanyDirt;
//        // -----------------------------------
//
//        //SKOMPRESOWANY PIASEK
//        ItemStack skompresowanyPiasek = new ItemStack(Material.SAND);
//        ItemMeta skompresowanyP = skompresowanyPiasek.getItemMeta();
//        skompresowanyP.setDisplayName("§dSkompresowany Piasek");
//        skompresowanyPiasek.setItemMeta(skompresowanyP);
//        skompresowanyPiasek.addEnchantment(Enchantment.DURABILITY, 10);
//        GitmanikPlugin.skompresowanyPiasek = skompresowanyPiasek;
//        // -----------------------------------

    }

    @Override
    public void onDisable() {
        getLogger().info("Bye.");
    }
}