package io.github.haykam821.missionworld;

import io.github.haykam821.missionworld.game.MissionWorldConfig;
import io.github.haykam821.missionworld.game.phase.MissionWorldLobbyPhase;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.game.GameType;

public class Main implements ModInitializer {
	private static final String MOD_ID = "missionworld";

	private static final Identifier MISSION_WORLD_ID = new Identifier(MOD_ID, "mission_world");
	public static final GameType<MissionWorldConfig> MISSION_WORLD_TYPE = GameType.register(MISSION_WORLD_ID, MissionWorldLobbyPhase::open, MissionWorldConfig.CODEC);

	@Override
	public void onInitialize() {
		return;
	}
} 