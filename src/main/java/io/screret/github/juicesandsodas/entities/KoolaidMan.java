package io.screret.github.juicesandsodas.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.screret.github.juicesandsodas.Base;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class KoolaidMan extends MonsterEntity {

    public KoolaidMan(EntityType<? extends MonsterEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.xpReward = 150;
        this.setHealth(150.0f);
        this.invulnerableTime = 2;
        this.setCustomName(new StringTextComponent("The Kool-Aid Man"));
    }

    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal((CreatureEntity) this.getEntity(), 1.2, true));
        this.goalSelector.addGoal(2, new RandomWalkingGoal((CreatureEntity) this.getEntity(), 1));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 32.0F));
        this.goalSelector.addGoal(4, new BreakDoorGoal(this, e -> true));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEFINED;
    }

    @Override
    public net.minecraft.util.SoundEvent getAmbientSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.pillager.ambient"));
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.glass.hit")), 0.2f, 2);
    }

    @Override
    public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.water.splash"));
    }

    @Override
    public net.minecraft.util.SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.glass.break"));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return AttributeModifierMap.builder()
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.5D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.ARMOR, 5.0D);
    }

    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        Random random = this.random;
        Entity entity = this;
        if (true)
            for (int l = 0; l < 4; ++l) {
                double d0 = (x + random.nextFloat());
                double d1 = (y + random.nextFloat());
                double d2 = (z + random.nextFloat());
                int i1 = random.nextInt(2) * 2 - 1;
                double d3 = (random.nextFloat() - 0.5D) * 1D;
                double d4 = (random.nextFloat() - 0.5D) * 1D;
                double d5 = (random.nextFloat() - 0.5D) * 1D;
                this.getCommandSenderWorld().addParticle(ParticleTypes.SPLASH, d0, d1, d2, d3, d4, d5);
            }
    }



    // Made with Blockbench 3.7.5
    // Exported for Minecraft version 1.15
    // Paste this class into your mod and generate all required imports
    public static class Model extends EntityModel<KoolaidMan> {
        private final ModelRenderer body;
        private final ModelRenderer cube_r1;
        private final ModelRenderer rightleg;
        private final ModelRenderer leftleg;
        private final ModelRenderer righthand;
        private final ModelRenderer lefthand;
        public Model() {
            texWidth = 64;
            texHeight = 64;
            body = new ModelRenderer(this);
            body.setPos(0.0F, 24.0F, 0.0F);
            body.texOffs(0, 0).addBox(-7.0F, -23.0F, -7.0F, 14.0F, 14.0F, 14.0F, 0.0F, false);
            body.texOffs(0, 0).addBox(-1.0F, -14.0F, 7.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
            body.texOffs(0, 0).addBox(-1.0F, -19.0F, 7.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
            cube_r1 = new ModelRenderer(this);
            cube_r1.setPos(0.0F, -17.0F, 9.0F);
            body.addChild(cube_r1);
            setRotationAngle(cube_r1, -1.5708F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
            rightleg = new ModelRenderer(this);
            rightleg.setPos(0.0F, 0.0F, 0.0F);
            body.addChild(rightleg);
            rightleg.texOffs(16, 46).addBox(-4.0F, -10.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
            leftleg = new ModelRenderer(this);
            leftleg.setPos(4.0F, 0.0F, 0.0F);
            body.addChild(leftleg);
            leftleg.texOffs(0, 46).addBox(-4.0F, -10.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
            righthand = new ModelRenderer(this);
            righthand.setPos(-1.0F, 0.0F, 0.0F);
            body.addChild(righthand);
            setRotationAngle(righthand, 0.0F, 0.0F, 0.0873F);
            righthand.texOffs(18, 28).addBox(-8.6101F, -17.9734F, -2.0F, 4.0F, 13.0F, 4.0F, 0.0F, false);
            lefthand = new ModelRenderer(this);
            lefthand.setPos(11.0F, -1.0F, 0.0F);
            body.addChild(lefthand);
            setRotationAngle(lefthand, 0.0F, 0.0F, -0.0873F);
            lefthand.texOffs(0, 28).addBox(-5.3937F, -17.8862F, -2.0F, 4.0F, 13.0F, 5.0F, 0.0F, false);
        }

        @Override
        public void setupAnim(KoolaidMan entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.lefthand.xRot = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
            this.rightleg.xRot = MathHelper.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
            this.leftleg.xRot = MathHelper.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
            this.righthand.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        }

        public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.xRot = x;
            modelRenderer.yRot = y;
            modelRenderer.zRot = z;
        }

        @Override
        public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    public static class Renderer extends MobRenderer<KoolaidMan, KoolaidMan.Model> {

        private static final ResourceLocation TEXTURE = new ResourceLocation(Base.MODID, "textures/entity/koolaidman/koolaidman.png");

        public Renderer(EntityRendererManager renderManagerIn) {
            super(renderManagerIn, new Model(), 0.8f);
        }

        @Override
        public ResourceLocation getTextureLocation(KoolaidMan entity) {
            return TEXTURE;
        }
    }
}