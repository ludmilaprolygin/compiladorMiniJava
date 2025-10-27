///[Error:m1|11]
// // No se puede llamar a un metodo no estatico desde un contexto estatico
//Author:Franco
class A {

    void m1() {

    }

    static void main() {
        m1();
    }
}