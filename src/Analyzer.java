import java.util.concurrent.BlockingQueue;

public class Analyzer implements Runnable {
    private final BlockingQueue<String> queue;
    private final char character;
    private int maxCount = 0;
    private String maxText = "";

    public Analyzer(BlockingQueue<String> queue, char character) {
        this.queue = queue;
        this.character = character;
    }

    @Override
    public void run() {
        try {
            String text;
            while (!(text = queue.take()).equals("END")) {
                int count = countCharacter(text, character);
                synchronized (this) {
                    if (count > maxCount) {
                        maxCount = count;
                        maxText = text;
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Max count of '" + character + "': " + maxCount);
            System.out.println("Text with max count: " + maxText);
        }
    }

    private int countCharacter(String text, char character) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }

}
