package io.github.haykam821.missionworld.game;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.haykam821.missionworld.game.map.MissionWorldMapConfig;

public class MissionWorldConfig {
	public static final Codec<MissionWorldConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
			MissionWorldMapConfig.CODEC.fieldOf("map").forGetter(MissionWorldConfig::getMapConfig)
		).apply(instance, MissionWorldConfig::new);
	});

	private final MissionWorldMapConfig mapConfig;

	public MissionWorldConfig(MissionWorldMapConfig mapConfig) {
		this.mapConfig = mapConfig;
	}

	public MissionWorldMapConfig getMapConfig() {
		return this.mapConfig;
	}
}