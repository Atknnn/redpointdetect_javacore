import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedPixelClicker {

    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(0, 0, 2560, 1080);

            int boxSize = 15;
            int boxX = screenRect.width / 2 - boxSize / 2;
            int boxY = screenRect.height / 2 - boxSize / 2;

            int interval = 5; // Kontrol aralığı

            // ExecutorService ile iki thread'i eşzamanlı olarak çalıştırma
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            // Click işlemleri thread'i
            executorService.submit(() -> {
                try {
                    while (true) {
                        boolean redPixelDetected = isRedPixelDetected(robot, boxX, boxY, boxSize, interval);
                        if (redPixelDetected) {

                            // Kırmızı piksel algılandığında sol tıklama yap
                            robot.mouseMove(boxX + boxSize / 2, boxY + boxSize / 2);
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

                            // 100 milisaniye bekleyerek basılı tut
                            Thread.sleep(50);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                        }

                        // Bir saniye /10 bekleyerek döngüyü tekrarla
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

    private static boolean isRedPixelDetected(Robot robot, int x, int y, int size, int interval) {
        for (int i = 0; i < size; i += interval) { // Belirli aralıklarla kontrol et
            for (int j = 0; j < size; j += interval) { // Belirli aralıklarla kontrol et
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