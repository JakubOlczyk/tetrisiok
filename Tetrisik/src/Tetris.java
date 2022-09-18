import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

class Tetris extends JPanel{


    private final Point[][][] Klocki = {
            //klocek I
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)}
            },

            //klocek J
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0)}
            },

            //klocek L
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0)}
            },

            //klocek kwadrat
            {
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)}
            },

            //klocek S
            {
                    {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)},
                    {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)}
            },

            //klocek T
            {
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2)}
            },

            //klocek Z
            {
                    {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2)},
                    {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2)}
            }
    };

    private final Color[] kolory = {
            Color.MAGENTA, Color.RED, Color.CYAN, Color.GREEN,
            Color.YELLOW, Color.ORANGE, Color.PINK
    };

    private Point poczatek;
    private int aktywnyKlocek;
    private int rotacja;
    private ArrayList<Integer> nKlocek = new ArrayList<Integer>();

    private long wynik;
    private Color[][] well;

    public void obramowka() {
        well = new Color[12][24];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                if (i == 0 || i == 11 || j == 22) {
                    well[i][j] = Color.DARK_GRAY;
                } else {
                    well[i][j] = Color.BLACK;
                }
            }
        }
        newKlocek();
    }

    public void newKlocek() {
        poczatek = new Point(5, 2);
        rotacja = 0;
        if (nKlocek.isEmpty()) {
            Collections.addAll(nKlocek, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(nKlocek);
        }
        aktywnyKlocek = nKlocek.get(0);
        nKlocek.remove(0);
    }

    private boolean kolizja(int x, int y, int rotacja) {
        for (Point p : Klocki[aktywnyKlocek][rotacja]) {
            if (well[p.x + x][p.y + y] != Color.BLACK) {
                return true;
            }
        }
        return false;
    }

    public void rotowanie(int i) {
        int newRotacja = (rotacja + i) % 4;
        if (newRotacja < 0) {
            newRotacja = 3;
        }
        if (!kolizja(poczatek.x, poczatek.y, newRotacja)) {
            rotacja = newRotacja;
        }
        repaint();
    }

    public void ruch(int i) {
        if (!kolizja(poczatek.x + i, poczatek.y, rotacja)) {
            poczatek.x += i;
        }
        repaint();
    }

    public void zrzut() {
        if (!kolizja(poczatek.x, poczatek.y + 1, rotacja)) {
            poczatek.y += 1;
        } else {
            fixToWell();
        }
        repaint();
    }

    public void fixToWell() {
        for (Point p : Klocki[aktywnyKlocek][rotacja]) {
            well[poczatek.x + p.x][poczatek.y + p.y] = kolory[aktywnyKlocek];
        }
        czyscWiersze();
        newKlocek();
    }

    public void usunWiersze(int wiersz) {
        for (int j = wiersz-1; j > 0; j--) {
            for (int i = 1; i < 11; i++) {
                well[i][j+1] = well[i][j];
            }
        }
    }

    public void czyscWiersze() {
        boolean luka;
        int numCzyszczen = 0;
        for (int j = 21; j > 0; j--) {
            luka = false;
            for (int i = 1; i < 11; i++) {
                if (well[i][j] == Color.BLACK) {
                    luka = true;
                    break;
                }
            }
            if (!luka) {
                usunWiersze(j);
                j += 1;
                numCzyszczen += 1;
            }
        }
        switch (numCzyszczen) {
            case 1:
                wynik += 100;
                break;
            case 2:
                wynik += 300;
                break;
            case 3:
                wynik += 500;
                break;
            case 4:
                wynik += 800;
                break;
        }
    }

    private void rysujKlocek(Graphics g) {
        g.setColor(kolory[aktywnyKlocek]);
        for (Point p : Klocki[aktywnyKlocek][rotacja]) {
            g.fillRect((p.x + poczatek.x) * 26,
                    (p.y + poczatek.y) * 26,
                    25, 25);
        }
    }





    @Override
    public void paintComponent(Graphics g)
    {



        g.fillRect(0, 0, 26*12, 26*23);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 23; j++) {
                g.setColor(well[i][j]);
                g.fillRect(26*i, 26*j, 25, 25);
            }
        }


        //wyświetlanie wyniku
        g.setColor(Color.WHITE);
        g.drawString("" + wynik, 19 * 12, 25);

        //narysowanie aktualnego klocka
        rysujKlocek(g);

    }







    public static void main(String[] args) {

        JFrame frame = new JFrame("Tetris");


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(12*26+10, 26*23+25);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);



        final Tetris game = new Tetris();
        game.obramowka();
        frame.add(game);






        frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        game.rotowanie(-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        game.rotowanie(+1);
                        break;
                    case KeyEvent.VK_LEFT:
                        game.ruch(-1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.ruch(+1);
                        break;
                    case KeyEvent.VK_SPACE:
                        game.zrzut();
                        game.wynik += 1;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        new Thread() {
            @Override public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        game.zrzut();
                    } catch ( InterruptedException e ) {}
                }
            }
        }.start();

    }
}
