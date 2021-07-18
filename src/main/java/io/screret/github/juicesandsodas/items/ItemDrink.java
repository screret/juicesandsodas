package io.screret.github.juicesandsodas.items;

import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.init.Registration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DeferredWorkQueue;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

import net.minecraft.item.Item.Properties;

public class ItemDrink extends GlassBottleItem implements IItemColor {

    public ItemDrink(Properties properties, Color color, @Nullable EffectInstance[] effects, @Nullable ResourceLocation shader){
        super(properties);
        this.effects = effects;
        this.shader = shader;
        this.color = color;
        if(shader != null){
            if(shader.toString().equals("minecraft:shaders/post/invert.json")){
                shaderEntity = new EndermanEntity(EntityType.ENDERMAN, Minecraft.getInstance().level);
            } else if(shader.toString().equals("minecraft:shaders/post/spider.json")){
                shaderEntity = new SpiderEntity(EntityType.SPIDER, Minecraft.getInstance().level);
            } else if(shader.toString().equals("minecraft:shaders/post/creeper.json")){
                shaderEntity = new CreeperEntity(EntityType.CREEPER, Minecraft.getInstance().level);
            }
        }
    }
    static int maxFoodLevel = 20;
    static int foodLevelIncrease = 4;
    protected Color color;

    protected EffectInstance[] effects;
    protected Entity shaderEntity;
    protected ResourceLocation shader;


    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

    }

    public EffectInstance effects(){
        if (effects != null){
            for(int i = 0; i < effects.length; i++){
                return effects[i];
            }
        }
        return null;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return DrinkHelper.useDrink(worldIn, playerIn, handIn);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;
        if (playerentity instanceof ServerPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)playerentity, stack);
        }

        if (worldIn.isClientSide) {
            for(EffectInstance effectinstance : PotionUtils.getMobEffects(stack)) {
                if (effectinstance.getEffect().isInstantenous()) {
                    effectinstance.getEffect().applyInstantenousEffect(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
                } else {
                    entityLiving.addEffect(new EffectInstance(effectinstance));
                }
            }
        }

        if (playerentity != null) {
            playerentity.awardStat(Stats.ITEM_USED.get(this));
            if (!playerentity.isCreative()) {
                stack.shrink(1);
            }
        }

        if (playerentity == null || !playerentity.isCreative()) {
            if (stack.isEmpty()) {
                return new ItemStack(Registration.EMPTY_JUICE_BOTTLE.get());
            }

            if (playerentity != null) {
                playerentity.inventory.add(new ItemStack(Registration.EMPTY_JUICE_BOTTLE.get()));
            }
        }

        loadCustomShader();

        Base.SHADER = shader;

        return stack;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getColor(ItemStack stack, int tintIndex) {
        return color.getRGB();
    }

    public void loadCustomShader() {
        if (Minecraft.getInstance().level != null) {
            GameRenderer renderer = Minecraft.getInstance().gameRenderer;
            DeferredWorkQueue.runLater(() -> {
                if (shaderEntity != null) {
                    renderer.checkEntityPostEffect(shaderEntity);
                } else if (shader != null) {
                    renderer.loadEffect(shader);
                }
            });
        }
    }
}