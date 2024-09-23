import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TextGenerator {

    private static final BlockingQueue<String> queueA = new ArrayBlockingQueue<>(1000);
    private static final BlockingQueue<String> queueB = new ArrayBlockingQueue<>(1000);
    private static final BlockingQueue<String> queueC = new ArrayBlockingQueue<>(1000);

    private static final String letters = "abc";
    private static final int numberOfTexts = 10_000;
    private static final int textLength = 100_000;

    public static void main(String[] args) {
        Thread generatorThread = new Thread(() -> {
            try {
                for (int i = 0; i < numberOfTexts; i++) {
                    String text = generateText(letters, textLength);
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread analyzerA = new Thread(new Analyzer(queueA, 'a'));
        Thread analyzerB = new Thread(new Analyzer(queueB, 'b'));
        Thread analyzerC = new Thread(new Analyzer(queueC, 'c'));


        generatorThread.start();
        analyzerA.start();
        analyzerB.start();
        analyzerC.start();

        try {
            generatorThread.join();

            queueA.put("END");
            queueB.put("END");
            queueC.put("END");

            analyzerA.join();
            analyzerB.join();
            analyzerC.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
