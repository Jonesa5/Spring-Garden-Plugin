package net.runelite.client.plugins.springgarden;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class SpringGardenOverlay extends Overlay
{

	private final Client client;
	private final SpringGardenConfig config;
	private final ElementalCollisionDetector collisionDetector;
	private static final WorldPoint LAUNCH_POINT = new WorldPoint(2921, 5473, 0);

	@Inject
	public SpringGardenOverlay(Client client, SpringGardenConfig config, ElementalCollisionDetector collisionDetector) {
		this.client = client;
		this.config = config;
		this.collisionDetector = collisionDetector;
		setPosition(OverlayPosition.DYNAMIC);
	}

	public static NPC[] bubbleSort(NPC[] npcs) {
		int lastPos, index;
		NPC temp;
		for (lastPos = npcs.length - 1; lastPos >= 0; lastPos--) {
			for (index = 0; index < lastPos; index++) {
				if (npcs[index].getId() > npcs[index + 1].getId()) {
					temp = npcs[index];
					npcs[index] = npcs[index + 1];
					npcs[index + 1] = temp;
				}
			}
		}
		return npcs;
	}

	@Override
	public Dimension render(Graphics2D graphics) {
		NPC[] npc = bubbleSort(client.getNpcs().stream().filter(ElementalCollisionDetector::isSpringElemental).sequential().toArray(NPC[]::new));
		for (int i = 0; i < ElementalCollisionDetector.RUNTILES.length; i++) {
			renderTile(graphics, ElementalCollisionDetector.RUNTILES[i], selectTileColor(collisionDetector.correctPosition(npc, i)));
		}
		renderTile(graphics, LAUNCH_POINT, Color.CYAN);
		return null;
	}

	private void renderTile(Graphics2D graphics, WorldPoint wp, Color color) {
		LocalPoint lp = LocalPoint.fromWorld(client, wp);
		if (lp != null) {
			Polygon poly = Perspective.getCanvasTilePoly(client, lp);
			if (poly != null)
				OverlayUtil.renderPolygon(graphics, poly, color);
		}
	}

	private Color selectTileColor(boolean onLocation) {
		if (onLocation)
			return config.tilesGood();
		return config.tilesBad();
	}
}