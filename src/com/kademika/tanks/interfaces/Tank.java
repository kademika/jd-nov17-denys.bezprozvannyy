package com.kademika.tanks.interfaces;


import com.kademika.tanks.BattleField.objects.tanks.Action;
import com.kademika.tanks.BattleField.objects.tanks.Direction;

public interface Tank {

	 public Action setupTank();
	 public boolean tanksInterception();
     public boolean interception();
	 public int getX();
	 public int getY();
	 public void setX(int x);
	 public void setY(int y);
	 public int getSpeed();
	 public Direction getDirection();
	 public void updateX(int x);
	 public void updateY(int y);
	 public boolean isDestroed();
	 public void turn(Direction right);

}
