///3&-3&1&0&6&4&exitosamente
class Init {
    static void main() {
        System.printIln(+3);   // 3 → el operador + no cambia nada
        System.printIln(-3);   // -3 → cambio de signo
        System.printBln(!false);   // 1 → negación lógica
        System.printBln(!true);   // 0 → negación lógica
        System.printIln(++5);  // 6 = 5 + 1
        System.printIln(--5);  // 4 = 6 - 1
    }
}
