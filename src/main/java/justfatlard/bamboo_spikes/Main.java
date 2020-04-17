package justfatlard.bamboo_spikes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {
	public static final BambooSpikesBlock BAMBOO_SPIKES_BLOCK = new BambooSpikesBlock(FabricBlockSettings.of(Material.BAMBOO).sounds(BlockSoundGroup.BAMBOO).strength(0.6F, 16.0F).build());

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier("bamboo-spikes-justfatlard", "bamboo_spikes"), BAMBOO_SPIKES_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("bamboo-spikes-justfatlard", "bamboo_spikes"), new BlockItem(BAMBOO_SPIKES_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
	}
}