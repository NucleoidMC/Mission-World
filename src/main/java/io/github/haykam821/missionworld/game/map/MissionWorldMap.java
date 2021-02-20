package io.github.haykam821.missionworld.game.map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import xyz.nucleoid.plasmid.map.template.MapTemplate;
import xyz.nucleoid.plasmid.map.template.TemplateChunkGenerator;
import xyz.nucleoid.plasmid.map.template.TemplateRegion;
import xyz.nucleoid.plasmid.util.BlockBounds;

public class MissionWorldMap {
	private final MapTemplate template;
	private final Vec3d spawn;
	private final Vec3d board;
	private final BlockBounds exit;

	public MissionWorldMap(MapTemplate template) {
		this.template = template;

		this.spawn = MissionWorldMap.getBoundsOrDefault(template, "spawn").getCenterBottom();
		this.board = MissionWorldMap.getBoundsOrDefault(template, "board").getCenter();
		this.exit = MissionWorldMap.getBoundsOrEmpty(template, "exit");
	}

	public boolean isOutOfBounds(ServerPlayerEntity player) {
		return !this.template.getBounds().contains(player.getBlockPos());
	}

	public boolean isInExit(ServerPlayerEntity player) {
		return this.exit.contains(player.getBlockPos());
	}

	public void spawn(ServerWorld world, ServerPlayerEntity player) {
		player.teleport(world, this.spawn.getX(), this.spawn.getY(), this.spawn.getZ(), 0, 0);
	}

	public Vec3d getBoard() {
		return this.board;
	}

	public ChunkGenerator createGenerator(MinecraftServer server) {
		return new TemplateChunkGenerator(server, this.template);
	}

	private static BlockBounds getBoundsOrDefault(MapTemplate template, String marker) {
		TemplateRegion region = template.getMetadata().getFirstRegion(marker);
		return region == null ? template.getBounds() : region.getBounds();
	}

	private static BlockBounds getBoundsOrEmpty(MapTemplate template, String marker) {
		TemplateRegion region = template.getMetadata().getFirstRegion(marker);
		return region == null ? BlockBounds.EMPTY : region.getBounds();
	}
}