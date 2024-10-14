package ex9;

import java.io.Serializable;

public class Time implements Serializable {
    private int hours;
    private int minutes;
    private int seconds;

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return "Horas: " + this.hours + " ; Minutos: " + this.minutes + " ; Segundos: " + this.seconds;
    }


}