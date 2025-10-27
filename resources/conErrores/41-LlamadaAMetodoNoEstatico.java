///[Error:m1|10]
// // No se puede llamar a un metodo no estatico desde un contexto estatico
class A {

    void m1() {

    }

    static void main() {
        m1();
    }
}