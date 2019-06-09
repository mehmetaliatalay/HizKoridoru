package mehmetali.com.hizkoridoru;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

public class TimeFormat {

    @SuppressLint("DefaultLocale")
    public static String getFormat(long miliseconds){

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(miliseconds), TimeUnit.MILLISECONDS.toMinutes(miliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(miliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(miliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(miliseconds)));
    }
}
