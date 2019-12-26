package justfatlard.bamboo_spikes;

import net.fabricmc.api.ModInitializer;
import justfatlard.bamboo_spikes.block.BlockRegistry;
import justfatlard.bamboo_spikes.item.ItemRegistry;

public class BambooSpikes implements ModInitializer {
    @Override
    public void onInitialize() {
        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();
    }
}