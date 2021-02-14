
package io.screret.github.juicesandsodas.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class KoolaidMan extends MobEntity {

    public KoolaidMan(EntityType<?> entityTypeIn, World worldIn) {
        super((EntityType<? extends MobEntity>) entityTypeIn, worldIn);
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void readAdditional(CompoundNBT compound) {

    }

    @Override
    public void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal((CreatureEntity) this.getEntity(), 1.2, true));
        this.goalSelector.addGoal(2, new RandomWalkingGoal((CreatureEntity) this.getEntity(), 1));
        this.targetSelector.addGoal(3, new HurtByTargetGoal((CreatureEntity) this.getEntity()));
        this.goalSelector.addGoal(4, new BreakDoorGoal(this, e -> true));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
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

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 150.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.5D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 15.0D)
                .createMutableAttribute(Attributes.ARMOR, 10.0D);
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }
    private final ServerBossInfo bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);
    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public void livingTick() {
        super.livingTick();
        double x = this.getPosX();
        double y = this.getPosY();
        double z = this.getPosZ();
        Random random = this.rand;
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
                world.addParticle(ParticleTypes.SPLASH, d0, d1, d2, d3, d4, d5);
            }
    }



    // Made with Blockbench 3.7.5
    // Exported for Minecraft version 1.15
    // Paste this class into your mod and generate all required imports
    public static class Model extends EntityModel<Entity> {
        private final ModelRenderer body;
        private final ModelRenderer cube_r1;
        private final ModelRenderer rightleg;
        private final ModelRenderer leftleg;
        private final ModelRenderer righthand;
        private final ModelRenderer lefthand;
        public Model() {
            textureWidth = 64;
            textureHeight = 64;
            body = new ModelRenderer(this);
            body.setRotationPoint(0.0F, 24.0F, 0.0F);
            body.setTextureOffset(0, 0).addBox(-7.0F, -23.0F, -7.0F, 14.0F, 14.0F, 14.0F, 0.0F, false);
            body.setTextureOffset(0, 0).addBox(-1.0F, -14.0F, 7.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
            body.setTextureOffset(0, 0).addBox(-1.0F, -19.0F, 7.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
            cube_r1 = new ModelRenderer(this);
            cube_r1.setRotationPoint(0.0F, -17.0F, 9.0F);
            body.addChild(cube_r1);
            setRotationAngle(cube_r1, -1.5708F, 0.0F, 0.0F);
            cube_r1.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
            rightleg = new ModelRenderer(this);
            rightleg.setRotationPoint(0.0F, 0.0F, 0.0F);
            body.addChild(rightleg);
            rightleg.setTextureOffset(16, 46).addBox(-4.0F, -10.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
            leftleg = new ModelRenderer(this);
            leftleg.setRotationPoint(4.0F, 0.0F, 0.0F);
            body.addChild(leftleg);
            leftleg.setTextureOffset(0, 46).addBox(-4.0F, -10.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
            righthand = new ModelRenderer(this);
            righthand.setRotationPoint(-1.0F, 0.0F, 0.0F);
            body.addChild(righthand);
            setRotationAngle(righthand, 0.0F, 0.0F, 0.0873F);
            righthand.setTextureOffset(18, 28).addBox(-8.6101F, -17.9734F, -2.0F, 4.0F, 13.0F, 4.0F, 0.0F, false);
            lefthand = new ModelRenderer(this);
            lefthand.setRotationPoint(11.0F, -1.0F, 0.0F);
            body.addChild(lefthand);
            setRotationAngle(lefthand, 0.0F, 0.0F, -0.0873F);
            lefthand.setTextureOffset(0, 28).addBox(-5.3937F, -17.8862F, -2.0F, 4.0F, 13.0F, 5.0F, 0.0F, false);
        }

        public Model(ModelRenderer body, ModelRenderer cube_r1, ModelRenderer rightleg, ModelRenderer leftleg, ModelRenderer righthand, ModelRenderer lefthand) {
            this.body = body;
            this.cube_r1 = cube_r1;
            this.rightleg = rightleg;
            this.leftleg = leftleg;
            this.righthand = righthand;
            this.lefthand = lefthand;
        }

        @Override
        public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
                           float alpha) {
            body.render(matrixStack, buffer, packedLight, packedOverlay);
        }

        public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }

        public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
            this.lefthand.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
            this.rightleg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
            this.leftleg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
            this.righthand.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
        }
    }
}
