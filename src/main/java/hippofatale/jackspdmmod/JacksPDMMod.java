package hippofatale.jackspdmmod;

import com.pixelmonmod.pixelmon.Pixelmon;
import hippofatale.jackspdmmod.block.ModBlocks;
import hippofatale.jackspdmmod.club.Club;
import hippofatale.jackspdmmod.events.AntiCheatEvents;
import hippofatale.jackspdmmod.events.MiniGameRunEvents;
import hippofatale.jackspdmmod.events.ModEvents;
import hippofatale.jackspdmmod.events.UnlockTitleEvents;
import hippofatale.jackspdmmod.home.Home;
import hippofatale.jackspdmmod.item.ModItems;
import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.storage.PlayerStorageInventory;
import hippofatale.jackspdmmod.teleport.PlayerTeleportUnlock;
import hippofatale.jackspdmmod.tileentity.ModTileEntities;
import hippofatale.jackspdmmod.title.PlayerTitle;
import hippofatale.jackspdmmod.util.MiniGameType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JacksPDMMod.MOD_ID)
public class JacksPDMMod
{
    public static final String MOD_ID = "jackspdmmod";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    //server data
    //clubs
    public static final File CLUBS_FILE = new File("clubs.dat");
    public static final Map<String, Club> clubs = new HashMap<>();
    public static final Map<UUID, String> playerClubs = new HashMap<>();
    public static final Map<UUID, List<String>> pendingInvites = new HashMap<>();
    //homes
    public static final File HOMES_FILE = new File("homes.dat");
    public static final Map<UUID, Home> personalHomes = new HashMap<>();
    public static final Map<String, Home> clubNameHomes = new HashMap<>();
    //market
    public static LocalDate marketLastUpdateDate = LocalDate.of(2025, 7, 27);
    public static final File MARKET_FILE = new File("market.dat");
    public static Map<String, Integer> marketPrices = new HashMap<String, Integer>() {{
        put("coal", 1);
        put("lapis_lazuli", 5);
        put("redstone", 3);
        put("iron_ore", 20);
        put("gold_ore", 30);
        put("diamond", 300);
        put("emerald", 3500);

        put("melon_slice", 712);
        put("pumpkin", 1700);
        put("cocoa_beans", 1160);
        put("wheat", 840);
        put("potato", 584);
        put("carrot", 584);
    }};
    public static Map<String, Integer> marketQuantities = new HashMap<String, Integer>(){{
        put("coal", 12);
        put("lapis_lazuli", 12);
        put("redstone", 12);
        put("iron_ore", 1);
        put("gold_ore", 1);
        put("diamond", 1);
        put("emerald", 1);

        put("melon_slice", 32);
        put("pumpkin", 32);
        put("cocoa_beans", 64);
        put("wheat", 32);
        put("potato", 32);
        put("carrot", 32);
    }};
    //mini-game
    public static boolean isMiniGameOpen = false;
    public static boolean isMiniGameRunning = false;
    public static MiniGameType miniGameType = MiniGameType.DICE_OF_FORTUNE;
    public static List<UUID> miniGameApplicants = new ArrayList<>();
    public static Map<Integer, UUID> diceOfFortune = new HashMap<>();
    public static List<Integer> diceNumbers = new ArrayList<>();
    //casino
    public static boolean isCasinoOpen = false;

    public JacksPDMMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModTileEntities.register(eventBus);

        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(ModEvents.class);
        Pixelmon.EVENT_BUS.register(ModEvents.class);
        MinecraftForge.EVENT_BUS.register(UnlockTitleEvents.class);
        Pixelmon.EVENT_BUS.register(UnlockTitleEvents.class);
        Pixelmon.EVENT_BUS.register(MiniGameRunEvents.class);
        Pixelmon.EVENT_BUS.register(AntiCheatEvents.class);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        CapabilityManager.INSTANCE.register(PlayerTeleportUnlock.class, new Capability.IStorage<PlayerTeleportUnlock>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<PlayerTeleportUnlock> capability, PlayerTeleportUnlock instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<PlayerTeleportUnlock> capability, PlayerTeleportUnlock instance, Direction side, INBT nbt) {

            }
        }, PlayerTeleportUnlock::new);

        CapabilityManager.INSTANCE.register(PlayerStorageInventory.class, new Capability.IStorage<PlayerStorageInventory>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<PlayerStorageInventory> capability, PlayerStorageInventory instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<PlayerStorageInventory> capability, PlayerStorageInventory instance, Direction side, INBT nbt) {

            }
        }, PlayerStorageInventory::new);

        CapabilityManager.INSTANCE.register(PlayerTitle.class, new Capability.IStorage<PlayerTitle>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<PlayerTitle> capability, PlayerTitle instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<PlayerTitle> capability, PlayerTitle instance, Direction side, INBT nbt) {

            }
        }, PlayerTitle::new);

        ModMessages.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.PLACARD_TILE.get(), SignTileEntityRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(MOD_ID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");

//        ClubData.loadClubData();
//        HomeData.loadHomeData();
//        MarketData.loadMarketData();
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
