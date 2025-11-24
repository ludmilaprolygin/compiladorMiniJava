///Object&exitosamente

class A {
    public void toString() {
        System.printSln("te cambie el toString xd");
    }
}

class Init{
    static void main(){
        var o = new Object();
        var a = new A();

        o.toString();
        System.println("-----");
        a.toString();
    }
}