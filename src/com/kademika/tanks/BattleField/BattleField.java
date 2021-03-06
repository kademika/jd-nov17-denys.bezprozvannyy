package com.kademika.tanks.BattleField;

import com.kademika.tanks.BattleField.objects.*;
import com.kademika.tanks.interfaces.Drawable;

import java.awt.*;
import java.util.Random;

public class BattleField implements Drawable {

    private int size = 64;
    private int quadrantsX = 9;
    private int quadrantsY = 9;

    private int BF_WIDTH = quadrantsX*size;
	private int BF_HEIGHT = quadrantsY*size;

    private String[][] battleFieldString = new String[quadrantsY][quadrantsX];
    private Object[][] battleField = new Object[quadrantsY][quadrantsX];

	/*private String[][] battleFieldString = new String[][] {
			{ "B", "B", "B", "B", "B", "B", "B", "B", "B" },
			{ "B", "", "", "W", "W", "", "B", "", "B" },
			{ "B", "R", "R", "", "", "", "", "", "B" },
			{ "B", "", "", "", "", "W", "B", "", "B" },
			{ "B", "", "", "", "", "", "", "", "B" },
			{ "B", "B", "", "W", "", "", "W", "B", "B" },
			{ "B", "W", "W", "", "", "B", "", "", "B" },
			{ "B", "W", "W", "B", "B", "B", "", "", "B" },
			{ "B", "B", "B", "B", "E", "B", "B", "B", "B" } };*/


	public BattleField() {
        generateBattleFieldString();
	}

    public String generateElements(int n) {
        String result = "";
        if (n < 35) {
            result = "B";
        } else if (n>35 && n<50) {
            result = "R";
        } else if (n>50 && n < 60) {
            result = "W";
        }
        return result;
    }

    public void generateBattleFieldString() {
        battleFieldString = new String[quadrantsY][quadrantsX];
        Random rand = new Random();
        for (int i=0; i<quadrantsY;i++){
            for (int j=0; j<quadrantsX;j++) {
                battleFieldString[i][j] = generateElements(rand.nextInt(100));
            }
        }
        battleFieldString[quadrantsY-1][quadrantsX/2] = "E";
    }

	public void generateBattleField() {

		for (int j = 0; j < quadrantsY; j++) {
			for (int k = 0; k < quadrantsX; k++) {
				String XY = getQuadrantXY(j + 1, k + 1);
				int separator = XY.indexOf("_");
				int x = Integer.parseInt(XY.substring(separator + 1));
				int y = Integer.parseInt(XY.substring(0, separator));
				switch (battleFieldString[j][k]) {
				case "B":
					battleField[j][k] = new Brick(x, y);
					break;
				case "R":
					battleField[j][k] = new Rock(x, y);
					break;
				case "W":
					battleField[j][k] = new Water(x, y);
					break;
				case "E":
					battleField[j][k] = new Eagle(x, y);
					break;
				default:
					battleField[j][k] = null;
					break;

				}
			}
		}
	}

	public void draw(Graphics g) {

		g.setColor(new Color(160, 160, 160));
		g.fillRect(0, 0, 4000, 4000);

		for (Object[] objects : battleField) {
			for (Object object : objects) {
				if (object != null && object instanceof AbstractObjects) {
					AbstractObjects o = (AbstractObjects) object;
					o.draw(g);
				}
			}
		}
	}

	String getQuadrantXY(int v, int h) {
		return (v - 1) * 64 + "_" + (h - 1) * 64;
	}

	public Object scanQuadrant(int x, int y) {
		if (x >= 0 && x < quadrantsY && y >= 0 && y < quadrantsX) {
		Object value = battleField[x][y];
		return value;
		} 
		return null;
	}

	public void updateQuadrant(int x, int y, Object newValue) {
		if (x >= 0 && x < quadrantsY && y >= 0 && y < quadrantsX) {
			battleField[x][y] = newValue;
		}
	}

    public int getQuadrantsX() {
        return quadrantsX;
    }

    public int getQuadrantsY() {
        return quadrantsY;
    }

    public void setQuadrantsXY(int quadrantsX,int quadrantsY) {
        this.quadrantsX = quadrantsX;
        this.quadrantsY = quadrantsY;
        BF_WIDTH = quadrantsX*size;
        battleFieldString = new String[quadrantsY][quadrantsX];
        battleField = new Object[quadrantsY][quadrantsX];
        BF_WIDTH = quadrantsX*size;
        BF_HEIGHT = quadrantsY*size;
        generateBattleFieldString();
    }

    public int getBF_WIDTH() {
		return BF_WIDTH;
	}

	public int getBF_HEIGHT() {
		return BF_HEIGHT;
	}

}
