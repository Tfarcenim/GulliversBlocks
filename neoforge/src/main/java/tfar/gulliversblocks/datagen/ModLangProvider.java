package tfar.gulliversblocks.datagen;

import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.TextComponents;
import tfar.gulliversblocks.init.ModMobEffects;
import tfar.gulliversblocks.init.ModPotions;

import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, GulliversBlocks.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addDefaultMobEffect(ModMobEffects.UPSIZE::value);
        addDefaultMobEffect(ModMobEffects.DOWNSIZE::value);
        addDefaultMobEffect(ModMobEffects.RENEWAL::value);

        addPotion(ModPotions.UPSIZE_I,"Potion of Upsizing");
        addPotion(ModPotions.UPSIZE_II,"Potion of Upsizing II");
        addPotion(ModPotions.UPSIZE_III,"Potion of Upsizing III");
        addPotion(ModPotions.UPSIZE_IV,"Potion of Upsizing IV");
        addPotion(ModPotions.UPSIZE_V,"Potion of Upsizing V");

        addPotion(ModPotions.DOWNSIZE_I,"Potion of Downsizing");
        addPotion(ModPotions.DOWNSIZE_II,"Potion of Downsizing II");
        addPotion(ModPotions.DOWNSIZE_III,"Potion of Downsizing III");
        addPotion(ModPotions.DOWNSIZE_IV,"Potion of Downsizing IV");
        addPotion(ModPotions.DOWNSIZE_V,"Potion of Downsizing V");

        addPotion(ModPotions.RENEWAL,"Potion of Renewal");
        add("death.attack.rose",  "%1$s was pricked to death");
        add( "death.attack.rose.player", "%1$s walked into a rose bush while trying to escape %2$s");
        add("sleep.too_big","Too big to sleep in this bed");

        addTextComponent(TextComponents.MIXOLOGIST_EXTRAORDINAIRE,"Mixologist Extraordinaire");
        addTextComponent(TextComponents.MIXOLOGIST_EXTRAORDINAIRE_DESC,"Brew all 3 new potions");

        addTextComponent(TextComponents.BIGGER_AND_BETTER,"Bigger and Better");
        addTextComponent(TextComponents.BIGGER_AND_BETTER_DESC,"Drink an Embiggening Potion");

        addTextComponent(TextComponents.DOWN_TO_SIZE,"Down to Size");
        addTextComponent(TextComponents.DOWN_TO_SIZE_DESC,"Drink an Ensmallening Potion");

        addTextComponent(TextComponents.ADVANCEMENT_ROOT,"Gulliver's Blocks");

//“Bigger and Better”
//Drinking an Embiggening potion.
//“Down to Size”
//Drinking an Ensmallening potion.
    }

    protected void addPotion(Holder<Potion> potion, String name) {
        ItemStack stack = PotionContents.createItemStack(Items.POTION,potion);
        add(stack.getDescriptionId(), name);
        
        ItemStack splashStack = PotionContents.createItemStack(Items.SPLASH_POTION,potion);
        add(splashStack.getDescriptionId(), "Splash "+name);

        ItemStack lingeringStack = PotionContents.createItemStack(Items.LINGERING_POTION,potion);
        add(lingeringStack.getDescriptionId(), "Lingering "+name);
    }

    protected void addDefaultMobEffect(Supplier<? extends MobEffect> supplier) {
        addEffect(supplier,getNameFromEffect(supplier.get()));
    }

    protected void addDefaultItem(Supplier<? extends Item> supplier) {
        addItem(supplier,getNameFromItem(supplier.get()));
    }

    protected void addDefaultBlock(Supplier<? extends Block> supplier) {
        addBlock(supplier,getNameFromBlock(supplier.get()));
    }

    protected void addDefaultEntityType(Supplier<EntityType<?>> supplier) {
        addEntityType(supplier,getNameFromEntity(supplier.get()));
    }

    public static String getNameFromItem(Item item) {
        return StringUtils.capitaliseAllWords(item.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromBlock(Block block) {
        return StringUtils.capitaliseAllWords(block.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEffect(MobEffect effect) {
        return StringUtils.capitaliseAllWords(effect.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEntity(EntityType<?> entity) {
        return StringUtils.capitaliseAllWords(entity.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    protected void addTextComponent(MutableComponent component, String text) {
        ComponentContents contents = component.getContents();
        if (contents instanceof TranslatableContents translatableContents) {
            add(translatableContents.getKey(),text);
        } else {
            throw new UnsupportedOperationException(component +" is not translatable");
        }
    }

}
