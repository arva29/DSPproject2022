package SETA;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom class to simulate a thread pool where a list of thread could be launched and then joined to waiting for their ending
 */
public class CustomThreadPool {

    private final List<Thread> threadList = new ArrayList<>();

    public void addThread(Runnable t){
        threadList.add(new Thread(t));
    }

    public void run() {
        for(Thread t: threadList){
            t.start();
        }

        for(Thread t: threadList){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
