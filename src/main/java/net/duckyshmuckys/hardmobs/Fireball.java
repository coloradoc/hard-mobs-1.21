package net.duckyshmuckys.hardmobs;

import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Fireball extends Item {
    public Fireball(Settings settings) {
        super(settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Vec3d look = user.getRotationVec(1.0F);
            double x = user.getX() + look.x * 2;
            double y = user.getEyeY() + look.y * 2;
            double z = user.getZ() + look.z * 2;

            // Initialize the fireball entity
            FireballEntity fireball = new FireballEntity(world, user, look, 3);
            fireball.updatePosition(x, y, z);  // Set the position
            fireball.setOwner(user);
            fireball.setVelocity(look.x, look.y, look.z, 1.0F, 0);  // Adjust velocity as needed

            // Spawn the fireball in the world
            world.spawnEntity(fireball);

            if (!user.isCreative()){
                user.getStackInHand(hand).decrement(1); // Decrement item stack if not in creative
            }
        }

        // Return the modified item stack
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public static final Item FIREBALL = registerItem("fireball", new Fireball(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(HardMobs.MOD_ID, name), item);
    }

    public static void registerModItems() {
        HardMobs.LOGGER.info("Registering Mod Items for " + HardMobs.MOD_ID);
        // Registration of the fireball item occurs here
        //registerItem("fireball", FIREBALL); // This line is now redundant; you can remove it.
    }
}
