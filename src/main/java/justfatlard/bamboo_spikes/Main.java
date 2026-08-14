package justfatlard.bamboo_spikes;

import justfatlard.pandorical.api.BlockRegistration;
import justfatlard.pandorical.api.ItemRegistration;
import justfatlard.pandorical.api.PandoricalApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Main implements ModInitializer {
	public static final String MOD_ID = "bamboo-spikes-justfatlard";

	public static final Identifier BAMBOO_SPIKES_ID = Identifier.fromNamespaceAndPath(MOD_ID, "bamboo_spikes");

	public static final ResourceKey<Block> BAMBOO_SPIKES_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, BAMBOO_SPIKES_ID);
	public static final ResourceKey<Item> BAMBOO_SPIKES_ITEM_KEY = ResourceKey.create(Registries.ITEM, BAMBOO_SPIKES_ID);

	public static final BambooSpikesBlock BAMBOO_SPIKES_BLOCK = new BambooSpikesBlock(
		BlockBehaviour.Properties.of()
			.sound(SoundType.BAMBOO)
			.strength(0.6F, 16.0F)
			.setId(BAMBOO_SPIKES_BLOCK_KEY)
			.noCollision()  // Server-side no collision
	);

	public static final BambooSpikesItem BAMBOO_SPIKES_ITEM = new BambooSpikesItem(
		BAMBOO_SPIKES_BLOCK,
		new Item.Properties().setId(BAMBOO_SPIKES_ITEM_KEY).useBlockDescriptionPrefix()
	);

	@Override
	public void onInitialize() {
		if (PandoricalApi.isAvailable()) {
			PandoricalApi.content().registerBlock(MOD_ID + ":bamboo_spikes", new BlockRegistration()
				.model(MOD_ID + ":block/bamboo_spikes_up"));
			PandoricalApi.content().registerItem(MOD_ID + ":bamboo_spikes", new ItemRegistration()
				.model(MOD_ID + ":item/bamboo_spikes"));
			PandoricalApi.content().registerModAssets(MOD_ID);
		}

		Registry.register(BuiltInRegistries.BLOCK, BAMBOO_SPIKES_ID, BAMBOO_SPIKES_BLOCK);
		Registry.register(BuiltInRegistries.ITEM, BAMBOO_SPIKES_ID, BAMBOO_SPIKES_ITEM);

		ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "bamboo_spikes"));
		CreativeModeTab bambooSpikesGroup = FabricCreativeModeTab.builder()
			.title(Component.literal("Bamboo Spikes"))
			.icon(() -> new ItemStack(BAMBOO_SPIKES_ITEM))
			.displayItems((context, entries) -> {
				entries.accept(new ItemStack(BAMBOO_SPIKES_ITEM));
			})
			.build();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, tabKey, bambooSpikesGroup);

		System.out.println("[bamboo-spikes] Loaded bamboo-spikes (server-side with Pandorical)");
	}
}
