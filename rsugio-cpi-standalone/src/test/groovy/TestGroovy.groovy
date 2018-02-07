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

    @Test
    void dynamicTemplates() {
        Map x = ["a": "Aa", "b": "bB"]
        println "$x.a/$x.b"
        println Eval.me('x', x, 'x.a')

        StringBuilder sb = new StringBuilder()
        Formatter frm = new Formatter(sb, Locale.forLanguageTag("ru-RU"))
        frm.format("%s/%s/%s", 1, 2, 3, 4, 5, 6)
        println sb
    }
}
