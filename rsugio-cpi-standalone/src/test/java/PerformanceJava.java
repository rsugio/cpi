import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

class JavaPojo {
    int value;
    String stringValue;
    JavaPojo(int x, String y) {
        this.value = x;
        this.stringValue = y;
    }
}

public class PerformanceJava {
    int proceed(int reps) {
        List<JavaPojo> list = new ArrayList<>(reps);
        int sum = 0;
        for (int it=0; it<reps; it++) {
            list.add(new JavaPojo(it, String.valueOf(it))) ;
        }

        for (JavaPojo it: list) {
            if (Integer.parseInt(it.stringValue) == it.value) {
                sum += it.value;
            }
        }
        return sum;
    }

    @Test
    public void run() {
        proceed(100000);
    }

}
