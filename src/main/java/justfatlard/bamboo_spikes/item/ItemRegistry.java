package justfatlard.bamboo_spikes.item;

import justfatlard.bamboo_spikes.block.BlockRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static void registerItems(){
        Registry.register(Registry.ITEM, new Identifier("bamboo-spikes-justfatlard", "bamboo_spikes"), new BlockItem(BlockRegistry.BAMBOO_SPIKES_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    }
}