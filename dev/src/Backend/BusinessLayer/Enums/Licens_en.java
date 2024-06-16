package Backend.BusinessLayer.Enums;

public enum Licens_en {
    B(1),
    C1(2),
    C(3),
    CE(4);
    private int value;
    Licens_en(int value) {this.value = value;}
    public boolean canDrive(Licens_en le){return le.value <= this.value;}

}
