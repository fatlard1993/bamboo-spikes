package justfatlard.bamboo_spikes;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class Main implements ModInitializer {
	public static final String MOD_ID = "bamboo-spikes-justfatlard";

	public static final Identifier BAMBOO_SPIKES_ID = Identifier.of(MOD_ID, "bamboo_spikes");

	public static final RegistryKey<net.minecraft.block.Block> BAMBOO_SPIKES_BLOCK_KEY = RegistryKey.of(RegistryKeys.BLOCK, BAMBOO_SPIKES_ID);
	public static final RegistryKey<Item> BAMBOO_SPIKES_ITEM_KEY = RegistryKey.of(RegistryKeys.ITEM, BAMBOO_SPIKES_ID);

	public static final BambooSpikesBlock BAMBOO_SPIKES_BLOCK = new BambooSpikesBlock(
		AbstractBlock.Settings.create()
			.sounds(BlockSoundGroup.BAMBOO)
			.strength(0.6F, 16.0F)
			.registryKey(BAMBOO_SPIKES_BLOCK_KEY)
			.noCollision()  // Server-side no collision
	);

	public static final BambooSpikesItem BAMBOO_SPIKES_ITEM = new BambooSpikesItem(
		BAMBOO_SPIKES_BLOCK,
		new Item.Settings().registryKey(BAMBOO_SPIKES_ITEM_KEY).useBlockPrefixedTranslationKey()
	);

	private static void setupPolymerModel(Direction facing, String modelSuffix) {
		Identifier modelId = Identifier.of(MOD_ID, "block/bamboo_spikes_" + modelSuffix);
		BlockState polymerState = PolymerBlockResourceUtils.requestBlock(
			BlockModelType.PLANT_BLOCK,  // No collision, transparent
			PolymerBlockModel.of(modelId)
		);

		if (polymerState != null) {
			BAMBOO_SPIKES_BLOCK.setPolymerBlockState(facing, polymerState);
		} else {
			System.err.println("[bamboo-spikes] Failed to request polymer model for " + modelSuffix);
		}
	}

	@Override
	public void onInitialize() {
		// Register mod assets with Polymer resource pack system
		PolymerResourcePackUtils.addModAssets(MOD_ID);
		PolymerResourcePackUtils.markAsRequired();

		// Register block and item
		Registry.register(Registries.BLOCK, BAMBOO_SPIKES_ID, BAMBOO_SPIKES_BLOCK);
		Registry.register(Registries.ITEM, BAMBOO_SPIKES_ID, BAMBOO_SPIKES_ITEM);

		// Setup Polymer models for each facing direction
		setupPolymerModel(Direction.UP, "up");
		setupPolymerModel(Direction.DOWN, "down");
		setupPolymerModel(Direction.NORTH, "north");
		setupPolymerModel(Direction.SOUTH, "south");
		setupPolymerModel(Direction.EAST, "east");
		setupPolymerModel(Direction.WEST, "west");

		// Create Polymer item group (access via /polymer creative)
		ItemGroup bambooSpikesGroup = PolymerItemGroupUtils.builder()
			.displayName(Text.literal("Bamboo Spikes"))
			.icon(() -> new ItemStack(BAMBOO_SPIKES_ITEM))
			.entries((context, entries) -> {
				entries.add(new ItemStack(BAMBOO_SPIKES_ITEM));
			})
			.build();
		PolymerItemGroupUtils.registerPolymerItemGroup(Identifier.of(MOD_ID, "bamboo_spikes"), bambooSpikesGroup);

		System.out.println("[bamboo-spikes] Loaded bamboo-spikes (server-side with Polymer)");
	}
}
