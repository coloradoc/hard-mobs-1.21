package net.duckyshmuckys.hardmobs;

import net.duckyshmuckys.hardmobs.component.fireballComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
//import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.network.ServerPlayNetworkHandler;
//import net.minecraft.server.world.ServerWorld;

//import net.fabricmc.api.ModInitializer;
//import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.time.DayOfWeek;
//import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class HardMobs implements ModInitializer {
	public static final String MOD_ID = "hardmobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Random RANDOM = new Random();


	@Override
	public void onInitialize() {

		//fireballComponent.registerDataComponentTypes();
		// Register an event listener for when an entity is loaded into the world
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (world != null && entity instanceof HostileEntity) {
				// Apply the 1/5 chance to modify the entity's attributes
				if (RANDOM.nextInt(20) == 0) {
					var attributes = ((HostileEntity) entity).getAttributes();

					// Double health
					if (attributes.hasAttribute(EntityAttributes.GENERIC_MAX_HEALTH)) {
						var healthAttribute = attributes.getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
						if (healthAttribute != null) {
							healthAttribute.setBaseValue(healthAttribute.getBaseValue() * 2.5);
							((HostileEntity) entity).setHealth(((HostileEntity) entity).getMaxHealth()); // Set current health to max
							// Equip the entity with an iron helmet
							((HostileEntity) entity).equipStack(EquipmentSlot.HEAD, Items.IRON_HELMET.getDefaultStack());
						}
					}

				}
				else{
					if (RANDOM.nextInt(20)==0){
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
		// Register the player join event

		// Register a server tick event to periodically check the time
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server != null && server.getPlayerManager() != null) {
				// Check the current time and day of the week
				LocalTime currentTime = LocalTime.now(ZoneId.of("America/New_York"));

					int currentHour = currentTime.getHour();

					// If outside allowed hours, kick all players currently online
				List<ServerPlayerEntity> players = new ArrayList<>(server.getPlayerManager().getPlayerList());
				for (ServerPlayerEntity player : players) {
					// Perform your logic here without modifying the original list
							if (Objects.equals(player.getName().getString(), "Eaqualoti")) {
								String playerName = player.getName().getString();
								applyPermanentEffect(player);
								if (currentHour < 14 || currentHour > 22) {
									LOGGER.info("Kicking {} as it is outside the allowed hours.", playerName);
									player.networkHandler.disconnect(Text.literal("Server is only accessible between 2 PM and 10 PM for you."));
								}
							}
					}

			}
		});
		// Register an explosion listener

	}


	private void applyPermanentEffect(ServerPlayerEntity player) {
		// Replace StatusEffects.STRENGTH with any other effect you want
		StatusEffectInstance effectInstance = new StatusEffectInstance(StatusEffects.WEAKNESS, Integer.MAX_VALUE, 1, false, false);
		StatusEffectInstance effectInstance2 = new StatusEffectInstance(StatusEffects.SLOWNESS, Integer.MAX_VALUE, 1, false, false);

		player.addStatusEffect(effectInstance);
		player.addStatusEffect(effectInstance2);
	}


	}
