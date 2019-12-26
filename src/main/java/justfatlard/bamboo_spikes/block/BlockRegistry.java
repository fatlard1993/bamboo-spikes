package justfatlard.bamboo_spikes.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {
    public static final BlockBambooSpikes BAMBOO_SPIKES_BLOCK = new BlockBambooSpikes(FabricBlockSettings.of(Material.BAMBOO).sounds(BlockSoundGroup.BAMBOO).strength(2.0F, 3.0F).build());

    public static void registerBlocks(){
        Registry.register(Registry.BLOCK, new Identifier("bamboo-spikes-justfatlard", "bamboo_spikes"), BAMBOO_SPIKES_BLOCK);
    }
}