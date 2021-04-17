package com.majesty373.springgarden;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
@PluginDescriptor(
		name = "Spring Garden",
		description = "Helps with the Sorceress's Spring Garden",
		tags = {"Sorceress", "spring", "garden", "thieving", "skilling"}
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
	private SpringGardenOverlayPanel overlayPanel;

	@Inject
	private SpringGardenConfig config;

	@Getter(AccessLevel.PACKAGE)
	private SpringGardenSession session;

	@Getter(AccessLevel.PACKAGE)
	static long START_TIME;

	private static final WorldPoint GARDEN = new WorldPoint(2928, 5468, 0);
	private static final int MAX_DISTANCE = 20;
	private static final String STAMINA_MESSAGE = "[Spring Garden] Low Stamina Warning";
	private boolean sentStaminaNotification = false;


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
		overlayManager.add(overlayPanel);
		overlayManager.add(overlay);
		START_TIME = System.currentTimeMillis();
	}

	private void disableOverlay() {
		if (overlayEnabled) {
			overlayManager.remove(overlayPanel);
			overlayManager.remove(overlay);
		}
		overlayEnabled = false;
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		if (event.getMessage().startsWith("An elemental force emanating from the garden teleports you away")) {
			if (session == null) {
				session = new SpringGardenSession();
			}
			session.increaseFruitGathered();
		}
		else if (event.getMessage().startsWith("You squeeze 4 sq'irks into an empty glass")) {
			if (session == null) {
				session = new SpringGardenSession();
			}
			session.increaseDrinksMade();
		}
		else if (event.getMessage().startsWith("You've been spotted by an elemental and teleported out of its garden")) {
			if (session == null) {
				session = new SpringGardenSession();
			}
			session.increaseFailedRuns();
		}
	}

	@Subscribe
	public void onGameTick(GameTick e) {
		Player p = client.getLocalPlayer();
		if (p == null)
			return;
		if (p.getWorldLocation().distanceTo2D(GARDEN) >= MAX_DISTANCE) {
			disableOverlay();
			return;
		} else if (session == null) {
			session = new SpringGardenSession();
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
