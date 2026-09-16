package pixeru.speedghast;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.HappyGhastEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class GhastSpeed implements ModInitializer {
	public static final String MOD_ID = "ghastspeed";

	// Change this to whatever effect you want to drive the speed boost.
	// Swap for your own registered effect's id if you have a custom one.
	private static final Identifier SPEED_EFFECT_ID = Identifier.of("minecraft", "speed");

	private static final double BASE_SPEED = 0.1;
	private static final double PER_LEVEL_INCREMENT = 0.02;

	@Override
	public void onInitialize() {
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			var speedEffect = Registries.getStatusEffect.(SPEED_EFFECT_ID);
			if (speedEffect == null) return;

			for (HappyGhastEntity ghast : world.getEntitiesByType(EntityType.HAPPY_GHAST, e -> true)) {
				EntityAttributeInstance attribute = ghast.getAttributeInstance(EntityAttributes.FLYING_SPEED);
				if (attribute == null) continue;

				StatusEffectInstance effect = ghast.getStatusEffect(speedEffect);
				if (effect != null) {
					int level = effect.getAmplifier() + 1; // amplifier 0 = level 1
					double newSpeed = BASE_SPEED + level * PER_LEVEL_INCREMENT;
					attribute.setBaseValue(newSpeed);
				} else {
					attribute.setBaseValue(BASE_SPEED);
				}
			}
		});
	}
}