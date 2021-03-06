package com.kademika.tanks;

import com.kademika.tanks.BattleField.BattleField;
import com.kademika.tanks.BattleField.objects.AbstractObjects;
import com.kademika.tanks.BattleField.objects.Eagle;
import com.kademika.tanks.BattleField.objects.Water;
import com.kademika.tanks.BattleField.objects.tanks.*;
import com.kademika.tanks.BattleField.objects.tanks.Action;
import com.kademika.tanks.BattleField.objects.tanks.bullet.Bullet;
import com.kademika.tanks.interfaces.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ActionField {

    private JTextField dimentionX;
    private JTextField dimentionY;
    private Icon icon1;
    private Icon icon2;
    private JLabel picture;
    private JButton go;
    private JRadioButton bt7Button;
    private JRadioButton tigerButton;
    private JFrame startFrame = new JFrame("BATTLE FIELD, DAY 8");
    private JFrame gameFrame = new JFrame("BATTLE FIELD, DAY 8");
    private JFrame endFrame = new JFrame("BATTLE FIELD, DAY 8");
    private JPanel mainPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            bf.draw(g);
            agressor.draw(g);
            defender.draw(g);
            bul.draw(g);
        }
    };
    private BattleField bf;
    private T34 defender;
    private Bullet bul;
    private AbstractTank agressor;
    private Random randCoordinate = new Random();


    public void runTheGame() {
        // processAction(defender, bul, defender.setupTank());
        while (!agressor.isDestroed() && !defender.isDestroed() && bf.scanQuadrant(bf.getQuadrantsY() - 1, findEagle()) != null) {

            processAction(agressor, bul, agressor.setupTank());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            processAction(defender, bul, defender.setupTank());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameOver();
        System.out.println("Game Over");
    }

    public void processAction(Tank tank, Bullet b, Action a)  {
        switch (a) {
            case MOVE:
                processMove(tank);
                break;
            case TURN:
                processTurn(tank);
                break;
            case FIRE:
                processFire(tank);
                break;
            default:
        }
    }

    public int findEagle() {
        for (int i = 0; i < bf.getQuadrantsX(); i++) {
            if (bf.scanQuadrant(bf.getQuadrantsY() - 1, i) instanceof Eagle) {
                return i;
            }
        }
        return -1;
    }

    public void processMove(Tank tank)  {
        int step = 1;
        int covered = 0;
        Direction direction = tank.getDirection();

        // check limits x: 0, 513; y: 0, 513
        if ((tank.getDirection() == Direction.UP && tank.getY() == 0)
                || (tank.getDirection() == Direction.DOWN && tank.getY() >= bf.getBF_HEIGHT()-64)
                || (tank.getDirection() == Direction.LEFT && tank.getX() == 0)
                || (tank.getDirection() == Direction.RIGHT && tank.getX() >= bf.getBF_WIDTH()-64)) {
            System.out.println("[illegal move] direction: "
                    + tank.getDirection() + " tankX: " + tank.getX()
                    + ", tankY: " + tank.getY());
            return;
        }

        tank.turn(direction);

        if (processInterceptionTank(tank)) {
            System.out.println(tank.getClass() + " Illegal move");
            return;
        }

        while (covered < 64) {

            if (processInterceptionBetweenTanks(tank)) {
                System.out.println(tank.getClass() + " Killing Enemy");
                return;
            }

            if (tank.getDirection() == Direction.UP) {

                tank.updateY(-step);
                System.out.println("[move up] direction: "
                        + tank.getDirection() + " tankX: " + tank.getX()
                        + ", tankY: " + tank.getY());
            } else if (tank.getDirection() == Direction.DOWN) {

                tank.updateY(step);
                System.out.println("[move down] direction: "
                        + tank.getDirection() + " tankX: " + tank.getX()
                        + ", tankY: " + tank.getY());
            } else if (tank.getDirection() == Direction.LEFT) {

                tank.updateX(-step);
                System.out.println("[move left] direction: "
                        + tank.getDirection() + " tankX: " + tank.getX()
                        + ", tankY: " + tank.getY());
            } else {

                tank.updateX(step);
                System.out.println("[move right] direction: "
                        + tank.getDirection() + " tankX: " + tank.getX()
                        + ", tankY: " + tank.getY());
            }

            covered += step;
            mainPanel.repaint();

            try {
                Thread.sleep(tank.getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void processTurn(Tank tank) {
        mainPanel.repaint();

    }

    public void processFire(Tank tank) {

        bul.destroy();
        bul.setTank(tank);
        bul.updateX(tank.getX() + 125);
        bul.updateY(tank.getY() + 125);
        bul.setDirection(tank.getDirection());
        int step2 = 1;

        while (bul.getBulletX() > 0 && bul.getBulletY() > 0
                && bul.getBulletX() < bf.getBF_WIDTH() && bul.getBulletY() < bf.getBF_HEIGHT()) {

            int step = 0;

            while (step < 64) {

                if (tank.getDirection().equals(Direction.UP)) {
                    bul.updateY(-step2);
                    System.out.println("[fire up] direction: "
                            + tank.getDirection() + " bulletY: "
                            + bul.getBulletY() + ", bulletX: "
                            + bul.getBulletX());
                } else if (tank.getDirection().equals(Direction.DOWN)) {
                    bul.updateY(step2);
                    System.out.println("[fire down] direction: "
                            + tank.getDirection() + " bulletY: "
                            + bul.getBulletY() + ", bulletX: "
                            + bul.getBulletX());
                } else if (tank.getDirection().equals(Direction.LEFT)) {
                    bul.updateX(-step2);
                    System.out.println("[fire left] direction: "
                            + tank.getDirection() + " bulletY: "
                            + bul.getBulletY() + ", bulletX: "
                            + bul.getBulletX());
                } else {
                    bul.updateX(step2);
                    System.out.println("[fire right] direction: "
                            + tank.getDirection() + " bulletY: "
                            + bul.getBulletY() + ", bulletX: "
                            + bul.getBulletX());
                }
                step++;
                if (processInterceptionBullet() || bul.getBulletX() < 0
                        && bul.getBulletY() < 0 || bul.getBulletX() > bf.getBF_WIDTH()
                        || bul.getBulletY() > bf.getBF_HEIGHT()) {
                    bul.destroy();
                }
                mainPanel.repaint();

                try {
                    Thread.sleep(bul.getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean processInterceptionBullet() {

        String koordinate = getQuadrant(bul.getBulletX(), bul.getBulletY());
        int delim = koordinate.indexOf("_");
        int elemY = Integer.parseInt(koordinate.substring(0, delim));
        int elemX = Integer.parseInt(koordinate.substring(delim + 1));

        if (elemY >= 0 && elemX >= 0 && elemY < bf.getQuadrantsY() && elemX < bf.getQuadrantsX()) {

            if (getQuadrant(bul.getBulletX(), bul.getBulletY()).equals(getQuadrant(agressor.getX(), agressor.getY()))
                    && !(agressor.equals(bul.getTank()))) {
                agressor.destroy();

                mainPanel.repaint();
                return true;
            } else if (getQuadrant(bul.getBulletX(), bul.getBulletY()).equals(getQuadrant(defender.getX(), defender.getY()))
                    && !(defender.equals(bul.getTank()))) {
                defender.destroy();
                mainPanel.repaint();

                return true;
            } else if (bf.scanQuadrant(elemY, elemX) != null && !(bf.scanQuadrant(elemY, elemX) instanceof Water)) {

                if (bf.scanQuadrant(elemY, elemX) instanceof AbstractObjects) {
                    AbstractObjects o = (AbstractObjects) bf.scanQuadrant(
                            elemY, elemX);
                    o.destroy();
                    bf.updateQuadrant(elemY, elemX, null);
                    mainPanel.repaint();

                    return true;
                }

            }

        }
        return false;
    }

    private boolean processInterceptionBetweenTanks(Tank tank) {

        return tank.tanksInterception();

    }

    private boolean processInterceptionTank(Tank tank) {

        return tank.interception();
    }

    String getQuadrant(int x, int y) {
        String result = bf.getQuadrantsY() + "_" + bf.getQuadrantsX();
        if (x < bf.getBF_WIDTH() && x > 0 && y < bf.getBF_HEIGHT() && y > 0) {
            int quadrantX = x / 64;
            int quadrantY = y / 64;
            result = quadrantY + "_" + quadrantX;
        }
        return result;
    }

    public void gameOver() {

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 19, 31));
                g.fillRect(0,0,1024,1024);
            }
        };
        panel.setLayout(new GridBagLayout());
        endFrame.setLocation(100, 100);
        endFrame.setMinimumSize(new Dimension(bf.getBF_WIDTH(),
                bf.getBF_HEIGHT() + 22));
        endFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setVisible(false);
        endFrame.setVisible(true);
        JButton button = new JButton("Try Again");
        button.setSize(20,40);
        JLabel lable = new JLabel();
        lable.setText("GAME OVER");
        lable.setSize(100, 100);
        lable.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
        panel.add(button, new GridBagConstraints(0, 1, 10, 10, 0, 0,
                GridBagConstraints.LINE_START, 0, new Insets(0, 80, 0, 0), 0, 0));
        panel.add(lable, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0, new Insets(0, 0, 0, 0), 0, 0));
        endFrame.setContentPane(panel);
        endFrame.pack();
        endFrame.repaint();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
    }

    public void restartGame() {
        endFrame.setVisible(false);
        startFrame.setVisible(true);
        startFrame.pack();
        startFrame.repaint();
    }


    public ActionField() {
        drawGUI();
    }

    public void drawGUI() {
        bt7Button = new JRadioButton("BT7 faster tank", true);
        tigerButton = new JRadioButton("Tiger with good armor");
        go = new JButton("GO");
        icon1 = new ImageIcon("BT7_up.png");
        icon2 = new ImageIcon("tiger_up.png");
        picture = new JLabel();
        picture.setIcon(icon1);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        JPanel dimentionPanel = new JPanel();

        bf = new BattleField();
        bf.generateBattleField();
        bul = new Bullet(-100, -100, Direction.LEFT);

        JLabel labelX = new JLabel("X Dimention: ");
        JLabel labelY = new JLabel("Y Dimention: ");
        dimentionX = new JTextField("9", 5);
        dimentionY = new JTextField("9", 5);
        dimentionPanel.add(labelX);
        dimentionPanel.add(dimentionX);
        dimentionPanel.add(labelY);
        dimentionPanel.add(dimentionY);

        radioPanel.add(bt7Button);
        radioPanel.add(tigerButton);

        panel.add(dimentionPanel, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(radioPanel, BorderLayout.LINE_START);
        panel.add(picture, BorderLayout.CENTER);
        panel.add(go, BorderLayout.AFTER_LAST_LINE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        startFrame.setContentPane(panel);
        startFrame.setLocation(100, 100);
        startFrame.setMinimumSize(new Dimension(bf.getBF_WIDTH(),
                bf.getBF_HEIGHT() + 22));
        startFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        startFrame.pack();
        startFrame.setVisible(true);

        bt7Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                picture.setIcon(icon1);
                tigerButton.setSelected(false);
            }
        });

        tigerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                picture.setIcon(icon2);
                bt7Button.setSelected(false);
            }
        });

        go.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                bf.setQuadrantsXY(Integer.valueOf(dimentionX.getText()), Integer.valueOf(dimentionY.getText()));
                bf.generateBattleField();

                defender = new T34(bf, (bf.getQuadrantsX() / 2 + 1) * 64, (bf.getQuadrantsY() - 1) * 64, Direction.UP);

                if (tigerButton.isSelected()) {
                    agressor = new Tiger(bf, randCoordinate.nextInt(bf.getQuadrantsX() - 1) * 64,
                            (randCoordinate.nextInt(bf.getQuadrantsY()/2)) * 64, Direction.DOWN);
                } else {
                    agressor = new BT7(bf, randCoordinate.nextInt(bf.getQuadrantsX() - 1) * 64,
                            (randCoordinate.nextInt(bf.getQuadrantsY()/2)) * 64, Direction.DOWN);
                }
                agressor.setEnemy(defender);
                defender.setEnemy(agressor);
                bf.updateQuadrant(agressor.getY() / 64, agressor.getX() / 64, null);
                bf.updateQuadrant(defender.getY() / 64, defender.getX() / 64, null);
                gameFrame.setContentPane(mainPanel);
                gameFrame.setLocation(100, 100);
                gameFrame.setMinimumSize(new Dimension(bf.getBF_WIDTH(),
                        bf.getBF_HEIGHT() + 22));
                gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                gameFrame.pack();
                startFrame.setVisible(false);
                gameFrame.repaint();
                gameFrame.setVisible(true);
                gameFrame.revalidate();
                new Thread() {
                    @Override
                    public void run() {
                        runTheGame();
                    }
                }.start();


            }

        });
    }

}
