package tfar.gulliversblocks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import tfar.gulliversblocks.GulliversBlocks;

public class ModTags {
    public static class Blocks{
        public static final TagKey<Block> CLIMBABLE_WHEN_SMALL = mod("climbable_when_small");
        public static final TagKey<Block> CLIMBABLE_WITH_SLIME = mod("climbable_with_slime");

        static TagKey<Block> mod(String path) {
            return TagKey.create(Registries.BLOCK, GulliversBlocks.id(path));
        }
    }

    public static class Items {
        public static final TagKey<Item> PREVENTS_DISMOUNT = mod("prevents_dismount");

        static TagKey<Item> mod(String path) {
            return TagKey.create(Registries.ITEM, GulliversBlocks.id(path));
        }
    }

}
