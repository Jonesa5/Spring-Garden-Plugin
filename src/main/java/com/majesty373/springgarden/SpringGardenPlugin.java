package com.majesty373.springgarden;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.springgarden.ElementalCollisionDetector;
import net.runelite.client.plugins.springgarden.SpringGardenConfig;
import net.runelite.client.plugins.springgarden.SpringGardenOverlay;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
@PluginDescriptor(
		name = "Spring Garden",
		description = "Helps with the Sorceress's Spring Garden",
		tags = {"Sorceress", "spring", "garden", "thieving", "skilling"},
		loadWhenOutdated = true
)
public class SpringGardenPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private Notifier notifier;

	@Inject
	private ElementalCollisionDetector collisionDetector;

	@Inject
	private SpringGardenOverlay overlay;

	@Inject
	private SpringGardenConfig config;

	private static final WorldPoint GARDEN = new WorldPoint(2928, 5468, 0);
	private static final String STAMINA_MESSAGE = "[Spring Garden] Low Stamina Warning";
	private boolean sentStaminaNotification = false;
	private static final int MAX_DISTANCE = 10;

	@Override
	protected void startUp() throws Exception {
		enableOverlay();
	}

	@Override
	protected void shutDown() throws Exception {
		disableOverlay();
	}

	private boolean overlayEnabled = false;
	private void enableOverlay() {
		if (overlayEnabled)
			return;
		overlayEnabled = true;
		overlayManager.add(overlay);
	}

	private void disableOverlay() {
		if (overlayEnabled)
			overlayManager.remove(overlay);
		overlayEnabled = false;
	}

	@Subscribe
	public void onGameTick(GameTick e) {
		Player p = client.getLocalPlayer();
		if (p == null)
			return;
		if (p.getWorldLocation().distanceTo2D(GARDEN) >= MAX_DISTANCE) {
			disableOverlay();
			return;
		}
		enableOverlay();
		// check for stamina usage
		int stamThreshold = config.staminaThreshold();
		if (stamThreshold != 0) {
			boolean stamActive = client.getVar(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0;
			if (client.getEnergy() <= stamThreshold && !stamActive && !sentStaminaNotification) {
				notifier.notify(STAMINA_MESSAGE, TrayIcon.MessageType.WARNING);
				sentStaminaNotification = true;
			} else if (client.getEnergy() > stamThreshold) {
				sentStaminaNotification = false;
			}
		}
	}

	@Provides
	SpringGardenConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(SpringGardenConfig.class);
	}
}
