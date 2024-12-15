package net.duckyshmuckys.hardmobs.mixin;

import com.mojang.serialization.MapDecoder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;

@Mixin(FireChargeItem.class)
public class FireChargeEntityMixin {

    // Injecting after the normal method execution, preserving original behavior
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {        // Retrieve the player from ItemUsageContext

        /*
        boolean hasNetherite = false;

        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++){
            ItemStack stack = inventory.getStack(i);
            if(stack.getItem() == Items.ANCIENT_DEBRIS){
                hasNetherite = true;
                stack.decrement(1);
                break;
            }
        }
        if(!hasNetherite){
            return;
        }
         */
        ItemStack stack = player.getStackInHand(hand);
        if (!"epic".equals(Objects.requireNonNull(stack.getComponents().get(DataComponentTypes.RARITY)).asString())) {
            return TypedActionResult.fail(stack);
        }
/*
        Vec3d vec3d = new Vec3d(player.getYaw(), player.getPitch(), player.get());

        // Create the fireball entity
        FireballEntity fireballEntity = new FireballEntity(world, player, vec3d, 2);
        fireballEntity.setItem(stack);  // Set the stack in the fireball entity
        fireballEntity.setPosition(pos.getX(), pos.getY(), pos.getZ()); // Position of fireball
        world.spawnEntity(fireballEntity);  // Spawn the fireball entity
*/

        FireballEntity fireball = new FireballEntity(
                world,
                player,
                new Vec3d(player.getRotationVector().x,player.getRotationVector().y,player.getRotationVector().z),
                2
        );

        fireball.setPosition(
                player.getX(),
                player.getEyeY() - 0.1F,
                player.getZ()
        );
        world.spawnEntity(fireball);
        player.getStackInHand(hand).decrement(1); // Decrease the fire charge stack
        return TypedActionResult.success(stack);
    }
}
