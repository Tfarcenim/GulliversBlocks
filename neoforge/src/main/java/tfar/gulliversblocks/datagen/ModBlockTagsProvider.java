package tfar.gulliversblocks.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, GulliversBlocks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        //    - Can climb through leaves, grass, gravel, wool, cactus, cake, cobwebs, etc. by holding shift
        tag(ModTags.Blocks.CLIMBABLE_WHEN_SMALL).addTag(BlockTags.LEAVES).addTag(BlockTags.CANDLE_CAKES).add(Blocks.CAKE,Blocks.COBWEB,Blocks.CACTUS);
    }
}
