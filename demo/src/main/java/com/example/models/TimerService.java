package com.example.models;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;


@Entity 
public class TimerService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  
    private Long id;
    public int focusDuration;
    public int breakDuration;
    public boolean isRunning;
    public boolean isBreak;
    public int minutes;
    public int seconds;

    
    // private TimerServiceRepository timerServiceRepository;

    public void startTimer(int focusDuration, int breakDuration) {
        this.focusDuration = focusDuration;
        this.breakDuration = breakDuration;
        this.isRunning = true;
        this.isBreak = false;
        this.minutes = focusDuration;
        this.seconds = 0;
        countdown();
    }

    public void stopTimer() {
        this.isRunning = false;
        this.isBreak = false;
    }

    public void resetTimer() {
        isRunning = false;
        isBreak = false;
        minutes = focusDuration;
        seconds = 0;
    }

    public void startBreak() {
        isRunning = true;
        isBreak = true;
        minutes = breakDuration;
        seconds = 0;
        countdown();
    }

    private void countdown() {
        Thread thread = new Thread(() -> {
            while (this.isRunning) {
                try {
                    Thread.sleep(1000);
                    if (this.seconds > 0) {
                        this.seconds--;
                    } else if (this.minutes > 0) {
                        this.minutes--;
                        this.seconds = 59;
                    } else {
                        // Timer finished, switch to break or focus time
                        if (this.isBreak) {
                            this.minutes = this.focusDuration;
                            this.isBreak = false;
                        } else {
                            this.minutes = this.breakDuration;
                            this.isBreak = true;
                        }
                        this.seconds = 0;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
   
    
}