package justfatlard.bamboo_spikes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

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
	);

	public static final BlockItem BAMBOO_SPIKES_ITEM = new BlockItem(
		BAMBOO_SPIKES_BLOCK,
		new Item.Settings().registryKey(BAMBOO_SPIKES_ITEM_KEY).useBlockPrefixedTranslationKey()
	);

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, BAMBOO_SPIKES_ID, BAMBOO_SPIKES_BLOCK);
		Registry.register(Registries.ITEM, BAMBOO_SPIKES_ID, BAMBOO_SPIKES_ITEM);

		// Add to creative inventory tabs
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
			entries.add(BAMBOO_SPIKES_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
			entries.add(BAMBOO_SPIKES_ITEM);
		});
	}
}
