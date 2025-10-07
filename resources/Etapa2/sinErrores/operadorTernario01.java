///[SinErrores]

class OperadorTernario {
    void testTernario(){

        var esMayor = (edad >= 18) ? true : false;
        int esMayor = (edad >= 18) ? true : false;
        var esMayor = edad >= 18 ? true : false;
        var esMayor = (edad >= 18) ? x = 2 : false;
        var x = (true ? 1 : 0);
        var z = true ? 1 : false ? 2 : 3;
    }
}