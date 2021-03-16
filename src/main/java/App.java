import java.util.ArrayList;
import java.util.List;


class Processor {
    private List<Integer> list = new ArrayList<>();
    public static final int UPPER_LIMIT = 5;
    public static final int LOWER_LIMIT = 0;
    private final Object lock = new Object();
    private int value = 0;

    public void produce() throws InterruptedException {
        synchronized (lock){
            while(true){
                Thread.sleep(500);
                if(list.size() == UPPER_LIMIT){
                    System.out.println("Warte bis Elemente entnommen werden.");
                    lock.wait();
                }else{
                    System.out.println("Adding: "+value);
                    list.add(value);
                    value++;
                    lock.notify();
                }
            }
        }
    }

    public void consume() throws InterruptedException {
        synchronized (lock){
            while(true){
                Thread.sleep(500);
                if(list.size() == LOWER_LIMIT){
                    System.out.println("Warte bis Elemente entnommen werden.");
                    lock.wait();
                }else{
                    System.out.println("Entnehme Wert: "+list.remove(list.size()-1));
                    lock.notify();
                }
            }
        }
    }
}

public class App {

    public static void main(String[] args) throws InterruptedException {
        Processor process = new Processor();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    process.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    process.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
    }
}
