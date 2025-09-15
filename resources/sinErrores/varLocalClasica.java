///[SinErrores]

class VarLocal {
    public VarLocal() {
        int x;
        int x,y,z = 10;
        int x,y,z;
        int x = 10;
        int x,y,z = 1 + 2;
        int x = true ? 1 : 2;
        VarLocal x = true ? 1 : 2;
        VarLocal x;
        VarLocal x,y,z = 1 + 2;
    }
}