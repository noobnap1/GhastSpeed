package pixeru.speedghast;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.effect.ServerMobEffectEvents;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class GhastSpeed implements ModInitializer {
	public static final String MOD_ID = "ghastspeed";

	private static final double BASE_SPEED = 0.05;
	private static final double SPEED_START = 1.5;
	private static final double SPEED_MULTIPLIER = 1.5;

	@Override
	public void onInitialize() {
		ServerMobEffectEvents.AFTER_ADD.register((effect, entity, context) -> {
			if (!(entity instanceof HappyGhast ghast)) return;

			MobEffectInstance speedEffect = ghast.getEffect(MobEffects.SPEED);
			if (speedEffect == null) return;

			AttributeInstance attribute = ghast.getAttribute(Attributes.FLYING_SPEED);
			if (attribute == null) return;

			int level = speedEffect.getAmplifier() + 1;
			double newSpeed = SPEED_START * Math.pow(SPEED_MULTIPLIER, level - 1);

			attribute.setBaseValue(newSpeed);
		});

		ServerMobEffectEvents.AFTER_REMOVE.register((effect, entity, context) -> {
			if (!(entity instanceof HappyGhast ghast)) return;

			MobEffectInstance speedEffect = ghast.getEffect(MobEffects.SPEED);
			if (speedEffect != null) return;

			AttributeInstance attribute = ghast.getAttribute(Attributes.FLYING_SPEED);
			if (attribute == null) return;

			attribute.setBaseValue(BASE_SPEED);
		});
	}
}