import groovy.transform.CompileStatic
import org.junit.Test

@CompileStatic

class GroovyPojo {
    int value
    String stringValue
}

class PerformanceGroovy {
    int proceed(int reps) {
        List<GroovyPojo> list = new ArrayList<>(reps)
        int sum = 0;
        reps.times {
            // first param is int and second is String
            list.add(new GroovyPojo(value: it, stringValue: it))
        }
        list.each {
            if (Integer.parseInt(it.stringValue) == it.value) {
                sum += it.value
            }
        }
        sum
    }

    @Test
    void run() {
        proceed(100000)
    }
}
