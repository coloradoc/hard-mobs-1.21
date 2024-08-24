package net.duckyshmuckys.hardmobs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class HardMobs implements ModInitializer {
	public static final String MOD_ID = "hardmobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Random RANDOM = new Random();

	@Override
	public void onInitialize() {
		LOGGER.info("HardMobs mod initialized!");

		// Register an event listener for when an entity is loaded into the world
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (world != null && entity instanceof HostileEntity) {
				// Apply the 1/5 chance to modify the entity's attributes
				if (RANDOM.nextInt(5) == 0) {
					var attributes = ((HostileEntity) entity).getAttributes();

					// Double health
					if (attributes.hasAttribute(EntityAttributes.GENERIC_MAX_HEALTH)) {
						var healthAttribute = attributes.getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
						if (healthAttribute != null) {
							healthAttribute.setBaseValue(healthAttribute.getBaseValue() * 2);
							((HostileEntity) entity).setHealth(((HostileEntity) entity).getMaxHealth()); // Set current health to max
							// Equip the entity with an iron helmet
							((HostileEntity) entity).equipStack(EquipmentSlot.HEAD, Items.IRON_HELMET.getDefaultStack());
						}
					}

				}
				else{
					if (RANDOM.nextInt(3)==0){
						var attributes = ((HostileEntity) entity).getAttributes();

						if (attributes.hasAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
							var attackAttribute = attributes.getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
							if (attackAttribute != null){
								attackAttribute.setBaseValue(attackAttribute.getBaseValue() * 2);
								// Equip the entity with a chainmail helmet
								((HostileEntity)entity).equipStack(EquipmentSlot.HEAD, Items.CHAINMAIL_HELMET.getDefaultStack());
							}
						}
					}
				}
			}
		});
	}
}
