package io.github.haykam821.missionworld.game.map;

import java.io.IOException;

import net.minecraft.text.TranslatableText;
import xyz.nucleoid.plasmid.game.GameOpenException;
import xyz.nucleoid.plasmid.map.template.MapTemplate;
import xyz.nucleoid.plasmid.map.template.MapTemplateSerializer;

public class MissionWorldMapBuilder {
	private final MissionWorldMapConfig config;

	public MissionWorldMapBuilder(MissionWorldMapConfig config) {
		this.config = config;
	}

	public MissionWorldMap create() {
		try {
			MapTemplate template = MapTemplateSerializer.INSTANCE.loadFromResource(this.config.getLobby());
			return new MissionWorldMap(template);
		} catch (IOException exception) {
			throw new GameOpenException(new TranslatableText("text.missionworld.template_load_failed"), exception);
		}
	}
}