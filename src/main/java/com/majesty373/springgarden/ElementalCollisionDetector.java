package net.runelite.client.plugins.springgarden;

import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import javax.inject.Singleton;

@Singleton
public class ElementalCollisionDetector {

    public static final WorldPoint[] RUNTILES = {
            new WorldPoint(2923, 5465, 0), // 0 NPC 0
            new WorldPoint(2923, 5459, 0), // 1 NPC 0
            new WorldPoint(2926, 5468, 0), // 2 NPC 1, 2, 6
            new WorldPoint(2928, 5470, 0), // 3 NPC 6
            new WorldPoint(2930, 5470, 0), // 4 NPC 5, 7
            new WorldPoint(2932, 5466, 0), // 5 NPC 5, 7
    };

    public static boolean isSpringElemental(NPC npc) {
        return npc.getId() >= 2956 && npc.getId() <= 2963;
    }

    public boolean correctPosition(NPC[] npcs, int runTileIndex) {
        switch (runTileIndex) {
            case 0:
                if (npcs[0].getOrientation() == 0 && npcs[0].getWorldLocation().getY() > 5460) return true;
                break;
            case 1:
                if (npcs[0].getOrientation() == 1024 && npcs[0].getWorldLocation().getY() > 5465) return true;
                break;
            case 2:
                if (npcs[1].getWorldLocation().getX() > 2925 &&
                  ((npcs[2].getWorldLocation().getX() > 2925 && npcs[2].getWorldLocation().getY() > 2925) || npcs[2].getWorldLocation().getX() > 2926) &&
                   (npcs[6].getOrientation() == 1024 && (npcs[6].getWorldLocation().getY() < 5473 && npcs[6].getWorldLocation().getY() > 5463))) return true;
                break;
            case 3:
                if ((npcs[6].getWorldLocation().getY() < 5467 || npcs[6].getWorldLocation().getY() > 5472) || (npcs[6].getOrientation() == 0 && npcs[6].getWorldLocation().getY() > 5473)) return true;
                break;
            case 4:
                if ((npcs[5].getOrientation() == 1536 && npcs[5].getWorldLocation().getX() > 2930) &&
                    (npcs[7].getOrientation() == 0 && npcs[7].getWorldLocation().getY() > 5472) ||
                     npcs[7].getOrientation() == 1024) return true;
                break;
            case 5:
                if ((npcs[5].getOrientation() == 1536 && npcs[5].getWorldLocation().getX() > 2931) &&
                   ((npcs[7].getOrientation() == 0 && npcs[7].getWorldLocation().getY() > 5473) ||
                     npcs[7].getOrientation() == 1024)) return true;
                break;
        }
        return false;
    }
}
