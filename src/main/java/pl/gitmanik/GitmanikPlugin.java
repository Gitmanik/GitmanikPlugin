package pl.gitmanik;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import pl.gitmanik.enchants.EnchantmentHelper;
import pl.gitmanik.enchants.GitmanikEnchantment;
import pl.gitmanik.events.*;
import pl.gitmanik.helpers.GitmanikDurability;
import pl.gitmanik.nightskip.NightSkipping;
import pl.gitmanik.nightskip.VoteSkipNightHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class GitmanikPlugin extends JavaPlugin {

    public static GitmanikPlugin gitmanikplugin;
    public static ChatHandler chathandler;

    public static ArrayList<GitmanikEnchantment> customEnchantments = new ArrayList<>();
    public static HashMap<String, ItemStack> customItems = new HashMap<>();

    public static HashMap<String, ItemStack> compressedItems = new HashMap<>();


    public static NightSkipping nightskipping = new NightSkipping();

    @Override
    public void onEnable() {
        gitmanikplugin = this;

        RegisterCustomEnchants();

        RegisterCommands();
        GenerateCustomItemStacks();

        GenerateCompressedItem(Material.COBBLESTONE, "§dSkompresowany Cobble", "c_cobble");
        GenerateCompressedItem(Material.DIRT, "§dSkompresowana Ziemia", "c_dirt");
        GenerateCompressedItem(Material.SAND, "§dSkompresowany Piasek", "c_sand");

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
        Bukkit.getPluginManager().registerEvents(new CraftingHandler(), this);

        chathandler = new ChatHandler(this);
        Bukkit.getPluginManager().registerEvents(chathandler, this);


        getLogger().info("Running.");

    }

    private void RegisterCommands()
    {
        //Admin

        GPAdmin a = new GPAdmin();
        this.getCommand("gpadmin").setExecutor(a);
        this.getCommand("gpadmin").setTabCompleter(a);


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
        customEnchantments.add(new GitmanikEnchantment("tunneldigger", ChatColor.GOLD + "Mruwia Reka"));
        customEnchantments.add(new GitmanikEnchantment("przychylnosc", ChatColor.LIGHT_PURPLE + "Przychylność Bogów"));
        customEnchantments.add(new GitmanikEnchantment("rekafarmera", ChatColor.YELLOW + "Ręka Farmera"));
        customEnchantments.add(new GitmanikEnchantment("depoenchant", "DEPO_ENCHANT"));

        customEnchantments.forEach((x) -> EnchantmentHelper.registerEnchant(this, x));
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
        ShapedRecipe mruwiKlejnotRecipe = new ShapedRecipe(new NamespacedKey(this, "mruwi_klejnot"), customItems.get("mruwi_klejnot"));
        mruwiKlejnotRecipe.shape("LLL", "LEL", "LLL");
        mruwiKlejnotRecipe.setIngredient('L', Material.LAPIS_BLOCK);
        mruwiKlejnotRecipe.setIngredient('E', Material.ENDER_EYE);
        Bukkit.addRecipe(mruwiKlejnotRecipe);

        //MRUWI KILOF
        ShapedRecipe mruwiRecip = new ShapedRecipe(new NamespacedKey(this, "mruwi_kilof"), customItems.get("mruwi_kilof"));
        mruwiRecip.shape("LLL", "RKR", "RRR");
        mruwiRecip.setIngredient('L', Material.LAPIS_BLOCK);
        mruwiRecip.setIngredient('R', Material.IRON_BLOCK);
        mruwiRecip.setIngredient('K', Material.DIAMOND_PICKAXE);
        Bukkit.addRecipe(mruwiRecip);

        //MAGICZNA ORCHIDEA
        ShapedRecipe orchidearecipe = new ShapedRecipe(new NamespacedKey(this, "magiczna_orchidea"), customItems.get("magiczna_orchidea"));
        orchidearecipe.shape("LHL", "HOH", "LHL");
        orchidearecipe.setIngredient('O', Material.BLUE_ORCHID);
        orchidearecipe.setIngredient('L', Material.LAPIS_BLOCK);
        orchidearecipe.setIngredient('H', Material.IRON_HOE);
        Bukkit.addRecipe(orchidearecipe);

        //ENDEROWY DEPOZYT
        ShapedRecipe depo = new ShapedRecipe(new NamespacedKey(this, "enderowy_depozyt"), customItems.get("enderowy_depozyt"));
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
        customItems.put("mruwi_klejnot", mruwiKlejnot);
        //------------------------------------

        //-------------------------------
        ItemStack mruwiKilof = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta mruwiaMeta = mruwiKilof.getItemMeta();
        mruwiaMeta.setDisplayName("§dMruwi Kilof");
        mruwiKilof.setItemMeta(mruwiaMeta);
        EnchantmentHelper.AddEnchantWithLore(mruwiKilof, EnchantmentHelper.GetEnchantment("tunneldigger"), 1);
        customItems.put("mruwi_kilof", mruwiKilof);
        //-------------------------------------------

        //KWIAT
        ItemStack magicznaOrchidea = new ItemStack(Material.BLUE_ORCHID);
        ItemMeta orchideaMeta = magicznaOrchidea.getItemMeta();
        orchideaMeta.setDisplayName("§dMagiczna Orchidea");
        magicznaOrchidea.setItemMeta(orchideaMeta);
        EnchantmentHelper.AddEnchantWithLore(magicznaOrchidea, EnchantmentHelper.GetEnchantment("rekafarmera"), 1);
        GitmanikDurability.SetDurability(magicznaOrchidea, 1000);
        customItems.put("magiczna_orchidea", magicznaOrchidea);

        // ------------------------------------

        //GRZYB
        ItemStack mrocznyDepozyt = new ItemStack(Material.CRIMSON_FUNGUS);
        ItemMeta epozyt = mrocznyDepozyt.getItemMeta();
        epozyt.setDisplayName("§dEnderowy Depozyt");
        mrocznyDepozyt.setItemMeta(epozyt);
        mrocznyDepozyt.addEnchantment(EnchantmentHelper.GetEnchantment("depoenchant"), 1);
        GitmanikDurability.SetDurability(mrocznyDepozyt, 25);
        customItems.put("enderowy_depozyt", mrocznyDepozyt);
        // -----------------------------------

    }

    private void GenerateCompressedItem(Material material, String name, String key)
    {
        ItemStack compressed = new ItemStack(material);
        ItemMeta meta = compressed.getItemMeta();
        meta.setDisplayName(name);
        compressed.setItemMeta(meta);
        compressed.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        compressedItems.put(key, compressed);

        ShapedRecipe depo = new ShapedRecipe(new NamespacedKey(this, key + "_to"), compressed);
        depo.shape("   ", " I ", "   ");
        depo.setIngredient('I', material);
        depo.setIngredient(' ', Material.AIR);
        try
        {
            Bukkit.addRecipe(depo);
        }
        catch (Exception ignored) {}
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye.");
    }
}