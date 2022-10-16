package com.spicerunners.armament.entity;

import com.spicerunners.armament.init.ArmamentEntities;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;


@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class MissileEntity extends AbstractArrow implements ItemSupplier {
    public MissileEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ArmamentEntities.MISSILE.get(), world);
    }

    public MissileEntity(EntityType<? extends MissileEntity> type, Level world) {
        super(type, world);
    }

    public MissileEntity(EntityType<? extends MissileEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public MissileEntity(EntityType<? extends MissileEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    @Override
    protected void onHitBlock(BlockHitResult ray) {
        super.onHitBlock(ray);

        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 4.0f, true, Explosion.BlockInteraction.BREAK);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround)
            this.discard();
    }

    public static MissileEntity shoot(Level world, LivingEntity entity, float power, double damage, int knockback) {
        MissileEntity entityarrow = new MissileEntity(ArmamentEntities.MISSILE.get(), entity, world);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        entityarrow.setSilent(true);
        entityarrow.setCritArrow(false);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        world.addFreshEntity(entityarrow);

        return entityarrow;
    }

    public static MissileEntity shoot(LivingEntity entity, LivingEntity target) {
        MissileEntity entityarrow = new MissileEntity(ArmamentEntities.MISSILE.get(), entity, entity.level);
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setBaseDamage(5);
        entityarrow.setKnockback(5);
        entityarrow.setCritArrow(false);
        entity.level.addFreshEntity(entityarrow);

        return entityarrow;
    }
}