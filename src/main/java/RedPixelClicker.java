import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedPixelClicker {
    private static volatile boolean isProgramClosed = false;

    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(0, 0, 2560, 1080);

            int boxSize = 10;
            int boxX = screenRect.width / 2 - boxSize / 2;
            int boxY = screenRect.height / 2 - boxSize / 2;

            // ExecutorService ile iki thread'i eşzamanlı olarak çalıştırma
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            // Click işlemleri thread'i
            executorService.submit(() -> {
                try {
                    while (!isProgramClosed) {
                        boolean redPixelDetected = isRedPixelDetected(robot, boxX, boxY, boxSize);
                        if (redPixelDetected) {

                            // Kırmızı piksel algılandığında sol tıklama yap
                            robot.mouseMove(boxX + boxSize / 2, boxY + boxSize / 2);
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

                            // 100 milisaniye bekleyerek basılı tut
                            Thread.sleep(100);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                        }

                        // Bir saniye /10 bekleyerek döngüyü tekrarla
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            // Kullanıcının programı kapattığını kontrol et
            while (!isProgramClosed) {
                Thread.sleep(1000); // Bekleme süresini uygun bir şekilde ayarlayabilirsiniz.
            }

            // ExecutorService'i kapat
            executorService.shutdown();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean isRedPixelDetected(Robot robot, int x, int y, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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