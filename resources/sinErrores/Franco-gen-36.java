///10&exitosamente

class Box {
    int value;

    public Box(int v) {
        value = v;
    }

    int getValue() {
        return value;
    }
}

class Main {
    static void main() {
        System.printIln((new Box(10)).getValue());
    }
}