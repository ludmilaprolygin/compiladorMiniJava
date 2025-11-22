///25&exitosamente

class PrecedenceTest {

    static int calculate() {
        var x = 5;
        var y = 10;
        var z = 2;

        return x + (y * z);
    }

    static void main() {
        System.printIln(PrecedenceTest.calculate());
    }
}