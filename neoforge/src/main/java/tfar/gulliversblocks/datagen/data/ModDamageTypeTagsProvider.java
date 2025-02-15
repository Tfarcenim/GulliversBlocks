package tfar.gulliversblocks.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.init.ModDamageTypes;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, GulliversBlocks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES).add(ModDamageTypes.ROSE);
        this.tag(DamageTypeTags.NO_KNOCKBACK).add(ModDamageTypes.ROSE);
    }
}
