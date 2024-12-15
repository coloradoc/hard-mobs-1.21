package net.duckyshmuckys.hardmobs.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireballEntity.class)
public class FireballEntityMixin {

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onFireballCollision(HitResult hitResult, CallbackInfo info) {
        FireballEntity fireball = (FireballEntity) (Object) this;
        World world = fireball.getWorld();

        if (fireball.getOwner() instanceof GhastEntity) {
            return;
        }
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();

            // Check if the block is obsidian
            if (world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) {
                // Create an explosion at the block's position
                Explosion explosion = new Explosion(world, fireball, fireball.getX(), fireball.getY(), fireball.getZ(), 2.0F, true, Explosion.DestructionType.DESTROY);
                explosion.collectBlocksAndDamageEntities(); // Prepare the explosion
                explosion.affectWorld(true);
                // Destroy the obsidian block directly
                // Break obsidian blocks within the radius
                for (BlockPos blockPos : BlockPos.iterateOutwards(pos, 1, 1, 1)) {
                    if (world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) {
                        world.breakBlock(blockPos, true); // True to drop items
                    }
                }

                // Cancel the original collision behavior to avoid default handling
                info.cancel();
            }
        }
    }
}
