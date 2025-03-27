import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

class LogNode {
    String timestamp;
    String message;
    LogNode next;

    public LogNode(String timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
        this.next = null;
    }
}

class Logger {
    private static Logger instance;
    private LogNode head = null;
    private LogNode tail = null;

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public synchronized void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        String logMessage = timestamp + " - " + message;
        LogNode newNode = new LogNode(timestamp, logMessage);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        System.out.println("Logged by instance: " + this.hashCode() + " => " + logMessage);
    }

    public void displayLogs() {
        System.out.println("\n--- Final Log Messages ---");
        LogNode current = head;
        while (current != null) {
            System.out.println(current.message);
            current = current.next;
        }
    }
}

class LogWriter extends Thread {
    private String srn;
    private int threadId;
    private Random random = new Random();

    public LogWriter(String srn, int threadId) {
        this.srn = srn;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        for (int i = 1; i <= 10; i++) {
            String message = srn + " - Thread-" + threadId + ": Log message " + i;
            logger.log(message);

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class task1 {
    public static void main(String[] args) {
        int numThreads = 4;
        Thread[] threads = new Thread[numThreads];
        String srn = "PES1UG22CS360";

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new LogWriter(srn, i + 1);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Logger.getInstance().displayLogs();
    }
}