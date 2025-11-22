///1&exitosamente
class Vehiculo {
    int a;
}

class TestHerencia {
    Vehiculo v;

    public TestHerencia(){
        v = new Vehiculo();
    }

    void andar() {
        v.a = 1;
        System.printIln(v.a);
    }

    static void main() {
        var t = new TestHerencia();
        t.andar();
    }
}