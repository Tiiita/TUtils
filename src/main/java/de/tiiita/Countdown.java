package de.tiiita;

import java.time.Duration;

@Deprecated
public class Countdown {
    private final Duration duration;
    private long counter;
    private Runnable whenComplete;
    private Runnable eachCycle;

    private boolean running;
    private Thread countdownThread;

    //Doesnt work currently
    public Countdown(Duration duration) {
        this.duration = duration;
        reset();
    }

    /**
     * Starts the countdown.
     * The counter will go down 1, every millisecond.
     * If the countdown is not paused, it will start again but at the time it paused.
     */
    public synchronized void start() {
        if (running)
            throw new IllegalStateException("Cannot start time because it is already running.");
        countdownThread = new Thread(() -> {
            while (counter > 0) {
                counter--;
                try {
                    Thread.sleep(getIntervalMillis(duration));
                } catch (InterruptedException ignore) {
                } //This is caused by pause, no problem

                if (eachCycle != null) {
                    eachCycle.run();
                }
            }

            if (whenComplete != null) {
                whenComplete.run();
            }

        });

        countdownThread.start();
    }


    /**
     * This runnable is called, after the countdown is done or the counter at position 0.
     *
     * @param runnable the runnable that will be run done.
     */
    public synchronized void whenComplete(Runnable runnable) {
        whenComplete = runnable;
    }


    /**
     * Set the runnable that will be called each cycle.
     *
     * @param runnable the runnable that will be called when 1 cycle is done.
     */
    public synchronized void eachCycle(Runnable runnable) {
        eachCycle = runnable;
    }

    /**
     * Pause the current countdown.
     * If already paused nothing happens.
     */
    public synchronized void pause() {
        if (running) return;
        countdownThread.interrupt();
        running = false;
    }

    /**
     * Resume the countdown, if not paused before, nothing happens.
     */
    public synchronized void resume() {
        if (running) return;
        start();
        running = true;
    }

    /**
     * Get the current counter in the unit the duration was given in.
     *
     * @return the counter with the correct duration unit.
     */
    public synchronized long getCounter() {
        return counter;
    }


    public synchronized void reset() {
        counter = duration.toMillis();
    }

    private long getIntervalMillis(Duration duration) {
        long durationMillis = duration.toMillis();

        if (durationMillis < 1000) {
            return 1; // 1 millisecond
        } else if (durationMillis < 60000) {
            return 1000; // 1 second
        } else if (durationMillis < 3600000) {
            return 60000; // 1 minute
        } else if (durationMillis < 86400000) {
            return 3600000; // 1 hour
        } else {
            return 86400000; // 1 day
        }
    }
}
