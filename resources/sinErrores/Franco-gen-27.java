///-1&exitosamente

class SimpleLoopTest {
    static int run() {
        var t = 5;

        while (t > 0) {
            t = t - 3;
        }

        return t;
    }

    static void main() {
        System.printIln(SimpleLoopTest.run());
    }
}