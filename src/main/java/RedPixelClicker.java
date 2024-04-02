import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedPixelClicker {
    private static final int SCREEN_WIDTH = 2560;
    private static final int SCREEN_HEIGHT = 1080;
    private static final int BOX_SIZE = 30;
    private static final int INTERVAL = 2;

    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            int boxX = SCREEN_WIDTH / 2 - BOX_SIZE / 2;
            int boxY = SCREEN_HEIGHT / 2 - BOX_SIZE / 2;

            // ExecutorService ile iki thread'i eşzamanlı olarak çalıştırma
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            // Click işlemleri thread'i
            executorService.submit(() -> {
                try {
                    while (true) {
                        boolean redPixelDetected = isRedPixelDetected(robot, boxX, boxY);
                        if (redPixelDetected) {
                            // Kırmızı piksel algılandığında sol tıklama yap
                            robot.mouseMove(boxX + BOX_SIZE / 2, boxY + BOX_SIZE / 2);
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

                            // 50 milisaniye bekleyerek basılı tut
                            Thread.sleep(50);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        }

                        // Bir saniye / 10 bekleyerek döngüyü tekrarla
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            // ExecutorService'i kapat
            executorService.shutdown();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static boolean isRedPixelDetected(Robot robot, int x, int y) {
        for (int i = 0; i < BOX_SIZE; i += INTERVAL) { // Belirli aralıklarla kontrol et
            for (int j = 0; j < BOX_SIZE; j += INTERVAL) { // Belirli aralıklarla kontrol et
                Color pixelColor = robot.getPixelColor(x + i, y + j);
                if (isRed(pixelColor)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isRed(Color color) {
        return color.getRed() > 200 && color.getGreen() < 95 && color.getBlue() < 95;
    }
}