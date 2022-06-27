package SETA;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom class to simulate a thread pool where a list of thread could be launched and then joined to waiting for their ending
 */
public class CustomThreadPool {

    private final List<Thread> threadList = new ArrayList<>();

    public void addThread(Thread t){
        threadList.add(t);
    }

    public void run() throws InterruptedException {
        for(Thread t: threadList){
            t.start();
        }

        for(Thread t: threadList){
            t.join();
        }
    }

}
