package tfar.gulliversblocks;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import tfar.gulliversblocks.config.GulliversBlocksConfig;
import tfar.gulliversblocks.duck.LivingEntityDuck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModCommands {

    public static void dispatcher(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(GulliversBlocks.MOD_ID)
                .then(Commands.literal("debug").executes(ModCommands::getInfo))
                .then(Commands.literal("scale").requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.literal("up")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("scales", IntegerArgumentType.integer())
                                                .executes(ModCommands::scaleUp))
                                )
                        )
                        .then(Commands.literal("down")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("scales", IntegerArgumentType.integer())
                                                .executes(ModCommands::scaleDown))
                                )
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("scales", IntegerArgumentType.integer())
                                                .executes(ModCommands::scaleSet))
                                )
                        )
                        .then(Commands.literal("reset")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .executes(ModCommands::scaleReset))
                        )
                )
        );
    }

    static int scaleUp(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "targets");
        int scale = IntegerArgumentType.getInteger(ctx, "scales");
        int i = scale(entities, scale);
        return i;
    }

    static int scaleDown(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "targets");
        int scale = IntegerArgumentType.getInteger(ctx, "scales");
        int i = scale(entities, -scale);
        return i;
    }

    static int scaleSet(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "targets");
        int scale = IntegerArgumentType.getInteger(ctx, "scales");
        int i = setScale(entities, scale);
        return i;
    }

    static int scaleReset(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "targets");
        int i = setScale(entities, 0);
        return i;
    }

    static int scale(Collection<? extends Entity> entities, int scale) {
        int i = 0;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                int originalScale = LivingEntityDuck.of(living).gulliversBlocks$getGulliverScale();
                int newScale = originalScale + scale;
                if (GulliverScales.valid(newScale)) {
                    LivingEntityDuck.of(living).gulliversBlocks$setGulliverScale(newScale);
                    i++;
                }
            }
        }
        return i;
    }

    static int setScale(Collection<? extends Entity> entities, int scale) {
        int i = 0;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                if (GulliverScales.valid(scale)) {
                    LivingEntityDuck.of(living).gulliversBlocks$setGulliverScale(scale);
                    i++;
                }
            }
        }
        return i;
    }

    private static int getInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        List<Component> components = createInfo(player);
        components.forEach(component -> source.sendSuccess(() -> component, false));
        return 1;
    }

    static List<Component> createInfo(LivingEntity entity) {
        List<Component> list = new ArrayList<>();
        list.add(Component.empty().append("Gulliver Stats for ").append(entity.getDisplayName()));
        int scale = LivingEntityDuck.of(entity).gulliversBlocks$getGulliverScale();
        list.add(Component.literal("Gulliver Scale: " + scale));
        double absoluteScale = GulliversBlocksConfig.Server.SCALES.get().getOrDefault(scale,1d);
        list.add(Component.literal("Absolute Scale: " + absoluteScale));
        if (entity instanceof Player player) {
            list.add(Component.literal("Block reach: " + player.blockInteractionRange()));
        }
        if (scale != 0) {
            list.add(Component.empty());
            list.add(Component.literal("Gulliver's Blocks Modifiers"));

            AttributeMap attributeMap = entity.getAttributes();

            for (Map.Entry<Holder<Attribute>, AttributeInstance> entry : attributeMap.attributes.entrySet()) {
                Holder<Attribute> attributeHolder = entry.getKey();
                AttributeInstance instance = entry.getValue();
                if (instance.hasModifier(GulliversBlocks.MODIFIER_ID)) {
                    addModifier(list::add,entity,attributeHolder);
                }
            }
        }
        return list;
    }

    static void addModifier(Consumer<Component> pTooltipAdder, LivingEntity pPlayer, Holder<Attribute> pAttribute) {
        addModifierTooltip(pTooltipAdder, pPlayer, pAttribute, pPlayer.getAttribute(pAttribute).getModifier(GulliversBlocks.MODIFIER_ID));
    }

    private static void addModifierTooltip(Consumer<Component> pTooltipAdder, LivingEntity pPlayer, Holder<Attribute> pAttribute, AttributeModifier pModfier) {
        double d0 = pModfier.amount();
        boolean flag = false;
        if (pPlayer != null) {
            if (pModfier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
                d0 += pPlayer.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                flag = true;
            } else if (pModfier.is(Item.BASE_ATTACK_SPEED_ID)) {
                d0 += pPlayer.getAttributeBaseValue(Attributes.ATTACK_SPEED);
                flag = true;
            }
        }

        double d1;
        if (pModfier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                || pModfier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            d1 = d0 * 100.0;
        } else if (pAttribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
            d1 = d0 * 10.0;
        } else {
            d1 = d0;
        }

        if (flag) {
            pTooltipAdder.accept(
                    CommonComponents.space()
                            .append(
                                    Component.translatable(
                                            "attribute.modifier.equals." + pModfier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
                                            Component.translatable(pAttribute.value().getDescriptionId())
                                    )
                            )
                            .withStyle(ChatFormatting.DARK_GREEN)
            );
        } else if (d0 > 0.0) {
            pTooltipAdder.accept(
                    Component.translatable(
                                    "attribute.modifier.plus." + pModfier.operation().id(),
                                    ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
                                    Component.translatable(pAttribute.value().getDescriptionId())
                            )
                            .withStyle(pAttribute.value().getStyle(true))
            );
        } else if (d0 < 0.0) {
            pTooltipAdder.accept(
                    Component.translatable(
                                    "attribute.modifier.take." + pModfier.operation().id(),
                                    ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-d1),
                                    Component.translatable(pAttribute.value().getDescriptionId())
                            )
                            .withStyle(pAttribute.value().getStyle(false))
            );
        }
    }
}
