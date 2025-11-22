///2&10&3&exitosamente
class Vehiculo {
    void acelerar() {
        System.printIln(1);
    }

    void frenar() {
        System.printIln(10);
    }

    void prenderLuces() {
        System.printIln(100);
    }
}

class Auto extends Vehiculo {
    void acelerar() {
        System.printIln(2);
    }
}

class Deportivo extends Auto {
    void acelerar() {
        System.printIln(3);
    }

    void frenar() {
        System.printIln(20);
    }
}

class TestHerencia {

    void andar() {
        var v = new Vehiculo();
        var a = new Auto();
        var d = new Deportivo();

        a.acelerar();
        v.frenar();
        d.acelerar();
    }

    static void main() {
        var t = new TestHerencia();
        t.andar();
    }
}