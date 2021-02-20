package io.github.haykam821.missionworld.game.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;

public class MissionWorldMapConfig {
	public static final Codec<MissionWorldMapConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
			Identifier.CODEC.fieldOf("lobby").forGetter(MissionWorldMapConfig::getLobby)
		).apply(instance, MissionWorldMapConfig::new);
	});

	private final Identifier lobby;

	public MissionWorldMapConfig(Identifier lobby) {
		this.lobby = lobby;
	}

	public Identifier getLobby() {
		return this.lobby;
	}
}