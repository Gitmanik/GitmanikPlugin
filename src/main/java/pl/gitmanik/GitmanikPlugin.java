package pl.gitmanik;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import pl.gitmanik.chatsystem.ChatSystem;
import pl.gitmanik.commands.Homesystem;
import pl.gitmanik.commands.StackPotions;
import pl.gitmanik.commands.Teleportsystem;
import pl.gitmanik.commands.gpadmin.GPAdmin;
import pl.gitmanik.enchants.EnchantmentHelper;
import pl.gitmanik.enchants.GitmanikEnchantment;
import pl.gitmanik.events.*;
import pl.gitmanik.helpers.GitmanikDurability;
import pl.gitmanik.nightskip.NightSkipping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GitmanikPlugin extends JavaPlugin {

    public static GitmanikPlugin gp;
    public static ChatSystem chathandler;
    public GPAdmin GPAdmin;

    public static ArrayList<GitmanikEnchantment> customEnchantments = new ArrayList<>();
    public static HashMap<String, ItemStack> customItems = new HashMap<>();

    public static HashMap<String, ItemStack> compressedItems = new HashMap<>();

    public static NightSkipping nightskipping;

    public static Random rand = new Random();

    private FileConfiguration config;

    @Override
    public void onEnable() {
        gp = this;
        this.saveDefaultConfig();
        config = this.getConfig();

        RegisterCustomEnchants();

        GPAdmin = new GPAdmin();

        if (config.getBoolean("skipsystem.enable"))
        {
            nightskipping = new NightSkipping();
        }
        if (config.getBoolean("chatsystem.enabled"))
        {
            chathandler = new ChatSystem();
        }
        if (config.getBoolean("stackpotions.enabled"))
        {
            StackPotions stackpotions = new StackPotions();
        }
        if (config.getBoolean("homesystem.enabled"))
        {
            Homesystem homesystem = new Homesystem();
            Bukkit.getPluginManager().registerEvents(homesystem, this);
        }
        if (config.getBoolean("teleportsystem.enabled"))
        {
            Teleportsystem teleportsystem = new Teleportsystem();
            Bukkit.getPluginManager().registerEvents(teleportsystem, this);
        }

        if (config.getBoolean("enableCompressedItems"))
        {
            GenerateCompressedItem(Material.COBBLESTONE, "§dSkompresowany Cobble", "c_cobble");
            GenerateCompressedItem(Material.DIRT, "§dSkompresowana Ziemia", "c_dirt");
            GenerateCompressedItem(Material.SAND, "§dSkompresowany Piasek", "c_sand");
            GenerateCompressedItem(Material.COBBLED_DEEPSLATE, "§dSkompresowany Bruk Deepslate", "c_bdeepslate");
            GenerateCompressedItem(Material.NETHERRACK, "§dSkompresowany Netherrack", "c_netherrack");
            GenerateCompressedItem(Material.ANDESITE, "§dSkompresowany Andezyt", "c_andesite");
            GenerateCompressedItem(Material.GRANITE, "§dSkompresowany Granit", "c_granite");
            GenerateCompressedItem(Material.DIORITE, "§dSkompresowany Dioryt", "c_diorite");
            GenerateCompressedItem(Material.GRAVEL, "§dSkompresowany Gravel", "c_gravel");
            GenerateCompressedItem(Material.STONE, "§dSkompresowany Stone", "c_stone");
            GenerateCompressedItem(Material.COAL_BLOCK, "§dSkompresowany Blok Wegla", "c_coalblock");
            GenerateCompressedItem(Material.QUARTZ_BLOCK, "§dSkompresowany Blok Kwarcu", "c_quartzblock");
        }

        try
        {
            if (config.getBoolean("customItems.enable"))
                LoadCustomItems();

            if (config.getBoolean("enableChainmailRecipes"))
                GenerateChainmailRecipes();
        }
        catch (Exception ignored){}

        if (config.getBoolean("diamondsystem.enable"))
            Bukkit.getPluginManager().registerEvents(new DiamondSystem(), this);

        if (config.getBoolean("tunneldigger.enable"))
            Bukkit.getPluginManager().registerEvents(new TunneldiggerHandler(), this);

        Bukkit.getPluginManager().registerEvents(new AnvilHandler(), this);
        Bukkit.getPluginManager().registerEvents(new CraftingHandler(), this);

        getLogger().info("Running.");

    }

    private void RegisterCustomEnchants()
    {
        customEnchantments.add(new GitmanikEnchantment("tunneldigger", ChatColor.GOLD + "Mruwia Reka"));
        customEnchantments.add(new GitmanikEnchantment("przychylnosc", ChatColor.LIGHT_PURPLE + "Przychylność Bogów"));
        customEnchantments.add(new GitmanikEnchantment("rekafarmera", ChatColor.YELLOW + "Ręka Farmera"));
        customEnchantments.add(new GitmanikEnchantment("diamentowaasceza", ChatColor.GOLD + "Diamentowa Asceza"));
        customEnchantments.add(new GitmanikEnchantment("depoenchant", "DEPO_ENCHANT"));
        customEnchantments.add(new GitmanikEnchantment("kompresja", ChatColor.AQUA + "Kompresja"));

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

    private void LoadCustomItems()
    {
        if (config.getBoolean("customItems.enableBlogoslawienstwoNieumarlych"))
        {
            ItemStack blogoslawienstwoNieumarlych = new ItemStack(Material.RAW_COPPER, 1);
            ItemMeta blogoslawienstwoNieumarlychMeta = blogoslawienstwoNieumarlych.getItemMeta();
            blogoslawienstwoNieumarlychMeta.setDisplayName(ChatColor.GOLD + "Błogosławieństwo Nieumarłych");
            blogoslawienstwoNieumarlych.setItemMeta(blogoslawienstwoNieumarlychMeta);
            EnchantmentHelper.AddEnchantWithLore(blogoslawienstwoNieumarlych, EnchantmentHelper.GetEnchantment("przychylnosc"), 1);
            customItems.put("blogoslawienstwo-nieumarlych", blogoslawienstwoNieumarlych);

            Bukkit.getPluginManager().registerEvents(new BozekHandler(), this);
        }


        if (config.getBoolean("customItems.enableMruwiKlejnot"))
        {
            ItemStack mruwiKlejnot = new ItemStack(Material.AMETHYST_BLOCK);
            ItemMeta mruwiKlejnotMeta = mruwiKlejnot.getItemMeta();
            mruwiKlejnotMeta.setDisplayName("§6Mruwi Klejnot");
            mruwiKlejnot.setItemMeta(mruwiKlejnotMeta);
            mruwiKlejnot.addUnsafeEnchantment(Enchantment.LUCK, 10);
            customItems.put("mruwi_klejnot", mruwiKlejnot);
            ShapedRecipe mruwiKlejnotRecipe = new ShapedRecipe(new NamespacedKey(this, "mruwi_klejnot"), customItems.get("mruwi_klejnot"));
            mruwiKlejnotRecipe.shape("LLL", "LEL", "LLL");
            mruwiKlejnotRecipe.setIngredient('L', Material.AMETHYST_SHARD);
            mruwiKlejnotRecipe.setIngredient('E', Material.NAUTILUS_SHELL);
            Bukkit.addRecipe(mruwiKlejnotRecipe);
        }

        if (config.getBoolean("customItems.enableMruwiKilof"))
        {
            ItemStack mruwiKilof = new ItemStack(Material.DIAMOND_PICKAXE);
            ItemMeta mruwiaMeta = mruwiKilof.getItemMeta();
            mruwiaMeta.setDisplayName("§dMruwi Kilof");
            mruwiKilof.setItemMeta(mruwiaMeta);
            EnchantmentHelper.AddEnchantWithLore(mruwiKilof, EnchantmentHelper.GetEnchantment("tunneldigger"), 1);
            customItems.put("mruwi_kilof", mruwiKilof);

            ShapedRecipe mruwiRecip = new ShapedRecipe(new NamespacedKey(this, "mruwi_kilof"), customItems.get("mruwi_kilof"));
            mruwiRecip.shape("AAA", "AKA", "AAA");
            mruwiRecip.setIngredient('A', Material.AMETHYST_BLOCK);
            mruwiRecip.setIngredient('K', Material.DIAMOND_PICKAXE);
            Bukkit.addRecipe(mruwiRecip);
        }

        if (config.getBoolean("customItems.enableMagicznaOrchidea"))
        {
            ItemStack magicznaOrchidea = new ItemStack(Material.BLUE_ORCHID);
            ItemMeta orchideaMeta = magicznaOrchidea.getItemMeta();
            orchideaMeta.setDisplayName("§dMagiczna Orchidea");
            magicznaOrchidea.setItemMeta(orchideaMeta);
            EnchantmentHelper.AddEnchantWithLore(magicznaOrchidea, EnchantmentHelper.GetEnchantment("rekafarmera"), 1);
            GitmanikDurability.SetDurability(magicznaOrchidea, 1000);
            customItems.put("magiczna_orchidea", magicznaOrchidea);

            ShapedRecipe orchidearecipe = new ShapedRecipe(new NamespacedKey(this, "magiczna_orchidea"), customItems.get("magiczna_orchidea"));
            orchidearecipe.shape("LHL", "HOH", "LHL");
            orchidearecipe.setIngredient('O', Material.BLUE_ORCHID);
            orchidearecipe.setIngredient('L', Material.LAPIS_BLOCK);
            orchidearecipe.setIngredient('H', Material.IRON_HOE);
            Bukkit.addRecipe(orchidearecipe);

            Bukkit.getPluginManager().registerEvents(new FarmersHandHandler(), this);
        }

        if (config.getBoolean("customItems.enableEnderowyDepozyt"))
        {
            ItemStack mrocznyDepozyt = new ItemStack(Material.CRIMSON_FUNGUS);
            ItemMeta epozyt = mrocznyDepozyt.getItemMeta();
            epozyt.setDisplayName("§dEnderowy Depozyt");
            mrocznyDepozyt.setItemMeta(epozyt);
            mrocznyDepozyt.addEnchantment(EnchantmentHelper.GetEnchantment("depoenchant"), 1); //dlaczego might be null??? zebys sie pytal smiedzelu
            GitmanikDurability.SetDurability(mrocznyDepozyt, 25);
            customItems.put("enderowy_depozyt", mrocznyDepozyt);

            ShapedRecipe depo = new ShapedRecipe(new NamespacedKey(this, "enderowy_depozyt"), customItems.get("enderowy_depozyt"));
            depo.shape("PPP", "NGN", "NNN");
            depo.setIngredient('P', Material.ENDER_PEARL);
            depo.setIngredient('G', Material.GLOWSTONE_DUST);
            depo.setIngredient('N', Material.NETHERRACK);
            Bukkit.addRecipe(depo);

            Bukkit.getPluginManager().registerEvents(new DepositHandler(), this);
        }
        if (config.getBoolean("enableCobbleToGravelStonecutter"))
        {
            StonecuttingRecipe cobbleToGravel = new StonecuttingRecipe(new NamespacedKey(this, "cobble_gravel"), new ItemStack(Material.GRAVEL, 2), Material.COBBLESTONE);
            Bukkit.addRecipe(cobbleToGravel);
        }
    }

    private void GenerateCompressedItem(Material material, String name, String key)
    {
        ItemStack compressed = new ItemStack(material);
        ItemMeta meta = compressed.getItemMeta();
        meta.setDisplayName(name);
        compressed.setItemMeta(meta);
        EnchantmentHelper.AddEnchantWithLore(compressed, EnchantmentHelper.GetEnchantment("kompresja"), 1);
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

    public boolean isDay(World world) {
        long time = world.getTime();
        return time < 12300 || time > 23850;
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye.");
    }
}