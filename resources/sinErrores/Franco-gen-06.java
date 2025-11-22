///30&false&true&exitosamente

class Init{
    static void main() {
        var x = 10;
        var y = new Integer(20);
        System.printIln(x + y.intValue());

        var z = 'a';
        var w = new Character('b');
        System.printBln(z == w.charValue());

        var a = true;
        var b = new Boolean(false);
        System.printBln(a != b.booleanValue());
    }
}
