import org.junit.Test

class TestGroovy {
    @Test
    void switchDemo() {
        String a = "123"
        switch (a) {
            case "1":
                throw new RuntimeException(a)
                break
            case "123":
                println "caught: $a"
                break
            default:
                println "not a caught: $a"
                break
        }
    }

    @Test
    void reflectionDemo() {
        Class cls = Class.forName("java.lang.Integer")
        println cls.valueOf("1111")
    }
}
