package team158.units;

import team158.Robot;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import team158.com.Broadcast;
import team158.utils.*;


public class Missile extends Robot {
	
	public Missile (RobotController newRC) {
		rc = newRC;
	}

	@Override
	public void move() {
		try {
			if (rc.isCoreReady()) {
				RobotInfo unitsInAttack[] = rc.senseNearbyRobots(2);
				int valuableAlliesInAttack = 0;
				int effectiveEnemiesInAttack = 0;
				for (int i = unitsInAttack.length; --i >= 0; ) {
					if (unitsInAttack[i].type != RobotType.MISSILE) {
						if (unitsInAttack[i].team.equals(rc.getTeam())) {
							valuableAlliesInAttack++;
						} else {
							effectiveEnemiesInAttack++;
						}
					}
				}
				
				if (effectiveEnemiesInAttack > 0 && valuableAlliesInAttack == 0) {
					rc.explode();
					return;
				}
				RobotInfo[] enemies = rc.senseNearbyRobots(24, rc.getTeam().opponent());
				if (enemies.length > 0) {
					Direction moveDirection = rc.getLocation().directionTo(enemies[0].location);
					if (rc.canMove(moveDirection)) {
					    rc.move(moveDirection);
					    return;
					}
					moveDirection = DirectionHelper.directions[(DirectionHelper.directionToInt(moveDirection) + 9) % 8];
					if (rc.canMove(moveDirection)) {
						rc.move(moveDirection);
						return;
					}
					moveDirection = DirectionHelper.directions[(DirectionHelper.directionToInt(moveDirection) + 6) % 8];
					if (rc.canMove(moveDirection)) {
						rc.move(moveDirection);
						return;
					}
					if (effectiveEnemiesInAttack - valuableAlliesInAttack >= 1) {
						rc.explode();
					}
				} else {
					Direction moveDirection = rc.getLocation().directionTo(Broadcast.readLocation(rc, Broadcast.enemyHQLocation));
					if (rc.canMove(moveDirection)) {
						rc.move(moveDirection);
					    return;
					}
					moveDirection = DirectionHelper.directions[(DirectionHelper.directionToInt(moveDirection) + 9) % 8];
					if (rc.canMove(moveDirection)) {
						rc.move(moveDirection);
						return;
					}
					moveDirection = DirectionHelper.directions[(DirectionHelper.directionToInt(moveDirection) + 6) % 8];
					if (rc.canMove(moveDirection)) {
						rc.move(moveDirection);
						return;
					}
					if (effectiveEnemiesInAttack - valuableAlliesInAttack >= 1) {
						rc.explode();
					}	  
				}
			}
		} catch (GameActionException e) {
			System.out.println(rc.getType());
			e.printStackTrace();
		}
	}

	@Override
	protected void actions() throws GameActionException {
	}

}
