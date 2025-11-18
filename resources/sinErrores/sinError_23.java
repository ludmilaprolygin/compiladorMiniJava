///10&exitosamente

class TestWhile {
    static void main() {
        var i = 0;
        var suma = 0;

        while(i < 5) {
            suma = suma + i;
            i = i + 1;
            debugPrint(i);
        }

        debugPrint(suma); // Debería imprimir 0+1+2+3+4 = 10
    }
}
