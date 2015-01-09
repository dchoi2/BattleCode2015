package team158.units;

import team158.utils.*;
import battlecode.common.*;

public class Beaver extends Unit {
	protected void actions() throws GameActionException {
		MapLocation myLocation = rc.getLocation();
		int[] offsets = {0,1,-1,2,-2,3,-3,4};
		RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, rc.getTeam().opponent());
		if (enemies.length > 0 && rc.isWeaponReady()) { 
			if (rc.isWeaponReady()) {
				rc.attackLocation(enemies[0].location);
			}
		}
		
		if (rc.isCoreReady()) {
			// HQ has given command for this particular beaver to build a miner factory
			if (rc.readBroadcast(Broadcast.buildMinerFactoriesCh) == rc.getID()) {
				rc.broadcast(Broadcast.buildMinerFactoriesCh, 0);
				int offsetIndex = 0;
				int dirint = DirectionHelper.directionToInt(rc.senseHQLocation().directionTo(myLocation));
				while (offsetIndex < 8 && !rc.canBuild(DirectionHelper.directions[(dirint+offsets[offsetIndex]+8)%8], RobotType.MINERFACTORY)) {
					offsetIndex++;
				}
				Direction buildDirection = null;
				if (offsetIndex < 8) {
					buildDirection = DirectionHelper.directions[(dirint+offsets[offsetIndex]+8)%8];
				}
				if (buildDirection != null) {
					rc.build(buildDirection, RobotType.MINERFACTORY);
				}
				else {
					rc.disintegrate();
				}
			}
			// HQ has given command for this particular beaver to build a barracks
			else if (rc.readBroadcast(Broadcast.buildBarracksCh) == rc.getID()) {
				rc.broadcast(Broadcast.buildBarracksCh, 0);
				int offsetIndex = 0;
				int dirint = DirectionHelper.directionToInt(rc.senseHQLocation().directionTo(rc.senseEnemyHQLocation()));
				while (offsetIndex < 8 && !rc.canBuild(DirectionHelper.directions[(dirint+offsets[offsetIndex]+8)%8], RobotType.BARRACKS)) {
					offsetIndex++;
				}
				Direction buildDirection = null;
				if (offsetIndex < 8) {
					buildDirection = DirectionHelper.directions[(dirint+offsets[offsetIndex]+8)%8];
				}
				if (buildDirection != null) {
					rc.build(buildDirection, RobotType.BARRACKS);
				}
				else {
					rc.disintegrate();
				}
			}
			// HQ has given command for this particular beaver to build a supply depot
			else if (rc.readBroadcast(Broadcast.buildSupplyCh) == rc.getID()) {
				rc.broadcast(Broadcast.buildSupplyCh, 0);
				int offsetIndex = 0;
				int dirint = DirectionHelper.directionToInt(rc.senseEnemyHQLocation().directionTo(rc.senseHQLocation()));
				while (offsetIndex < 8 && !rc.canBuild(DirectionHelper.directions[(dirint+offsets[offsetIndex]+8)%8], RobotType.SUPPLYDEPOT)) {
					offsetIndex++;
				}
				Direction buildDirection = null;
				if (offsetIndex < 8) {
					buildDirection = DirectionHelper.directions[(dirint+offsets[offsetIndex]+8)%8];
				}
				if (buildDirection != null) {
					rc.build(buildDirection, RobotType.SUPPLYDEPOT);
				}
				else {
					rc.disintegrate();
				}
			}
			else {
				double currentOre = rc.senseOre(myLocation);
				double maxOre = -2;
				Direction bestDirection = null;
				// looks around for an ore concentration that is bigger than its current location by a certain fraction
				for (Direction dir: DirectionHelper.directions) {
					double possibleOre = rc.senseOre(myLocation.add(dir));
					if (possibleOre > maxOre && rc.canMove(dir)) {
						maxOre = possibleOre;
						bestDirection = dir;
					}
				}
				if (maxOre > 1.5 * currentOre && bestDirection != null) {
					rc.move(bestDirection);
				}
				else {
					rc.mine();
				}
			}
		}
	}
}