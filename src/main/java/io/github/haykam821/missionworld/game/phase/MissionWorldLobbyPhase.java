package io.github.haykam821.missionworld.game.phase;

import io.github.haykam821.missionworld.game.MissionWorldConfig;
import io.github.haykam821.missionworld.game.map.MissionWorldMap;
import io.github.haykam821.missionworld.game.map.MissionWorldMapBuilder;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import xyz.nucleoid.fantasy.BubbleWorldConfig;
import xyz.nucleoid.plasmid.entity.FloatingText;
import xyz.nucleoid.plasmid.entity.FloatingText.VerticalAlign;
import xyz.nucleoid.plasmid.game.GameOpenContext;
import xyz.nucleoid.plasmid.game.GameOpenProcedure;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.event.GameOpenListener;
import xyz.nucleoid.plasmid.game.event.GameTickListener;
import xyz.nucleoid.plasmid.game.event.OfferPlayerListener;
import xyz.nucleoid.plasmid.game.event.PlayerAddListener;
import xyz.nucleoid.plasmid.game.event.PlayerDamageListener;
import xyz.nucleoid.plasmid.game.event.PlayerDeathListener;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.game.rule.GameRule;
import xyz.nucleoid.plasmid.game.rule.RuleResult;

public class MissionWorldLobbyPhase implements GameOpenListener, GameTickListener, OfferPlayerListener, PlayerAddListener, PlayerDamageListener, PlayerDeathListener {
	private static final Formatting COMPLETION_FORMATTING = Formatting.YELLOW;

	private final GameSpace gameSpace;
	private final MissionWorldMap map;

	public MissionWorldLobbyPhase(GameSpace gameSpace, MissionWorldMap map) {
		this.gameSpace = gameSpace;
		this.map = map;
	}

	public static GameOpenProcedure open(GameOpenContext<MissionWorldConfig> context) {
		MissionWorldMapBuilder mapBuilder = new MissionWorldMapBuilder(context.getConfig().getMapConfig());
		MissionWorldMap map = mapBuilder.create();

		BubbleWorldConfig worldConfig = new BubbleWorldConfig()
			.setGenerator(map.createGenerator(context.getServer()))
			.setTimeOfDay(18000)
			.setDefaultGameMode(GameMode.ADVENTURE);

		return context.createOpenProcedure(worldConfig, game -> {
			MissionWorldLobbyPhase phase = new MissionWorldLobbyPhase(game.getSpace(), map);

			game.setRule(GameRule.BLOCK_DROPS, RuleResult.DENY);
			game.setRule(GameRule.BREAK_BLOCKS, RuleResult.DENY);
			game.setRule(GameRule.CRAFTING, RuleResult.DENY);
			game.setRule(GameRule.FALL_DAMAGE, RuleResult.DENY);
			game.setRule(GameRule.HUNGER, RuleResult.DENY);
			game.setRule(GameRule.INTERACTION, RuleResult.DENY);
			game.setRule(GameRule.PLACE_BLOCKS, RuleResult.DENY);
			game.setRule(GameRule.PORTALS, RuleResult.DENY);
			game.setRule(GameRule.PVP, RuleResult.DENY);
			game.setRule(GameRule.THROW_ITEMS, RuleResult.DENY);

			// Listeners
			game.on(GameOpenListener.EVENT, phase);
			game.on(GameTickListener.EVENT, phase);
			game.on(OfferPlayerListener.EVENT, phase);
			game.on(PlayerAddListener.EVENT, phase);
			game.on(PlayerDamageListener.EVENT, phase);
			game.on(PlayerDeathListener.EVENT, phase);
		});
	}

	// Listeners
	@Override
	public void onOpen() {
		// Spawn completion text
		Vec3d completionPos = this.map.getBoard();
		Text completionText = new TranslatableText("text.missionworld.mission_completion", 0, 0).formatted(COMPLETION_FORMATTING);

		this.gameSpace.getWorld().getChunk(new BlockPos(completionPos));
		FloatingText.spawn(this.gameSpace.getWorld(), completionPos, new Text[]{completionText}, VerticalAlign.CENTER);
	}

	@Override
	public void onTick() {
		for (ServerPlayerEntity player : this.gameSpace.getPlayers()) {
			if (this.map.isOutOfBounds(player)) {
				this.map.spawn(this.gameSpace.getWorld(), player);
			} else if (this.map.isInExit(player)) {
				this.gameSpace.removePlayer(player);
			}
		}
	}

	@Override
	public JoinResult offerPlayer(ServerPlayerEntity player) {
		return JoinResult.ok();
	}

	@Override
	public void onAddPlayer(ServerPlayerEntity player) {
		this.map.spawn(this.gameSpace.getWorld(), player);
	}

	@Override
	public ActionResult onDamage(ServerPlayerEntity player, DamageSource source, float amount) {
		return ActionResult.FAIL;
	}

	@Override
	public ActionResult onDeath(ServerPlayerEntity player, DamageSource source) {
		this.map.spawn(this.gameSpace.getWorld(), player);
		return ActionResult.FAIL;
	}
}