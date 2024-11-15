public class Gudang {
    private String IDGudang;
    private String namaG;
    private double kapasitasMax;
    //Stok
    private ArrayList <StokMakanan>;
    private String IDStok;
    private String namaS;
    private double jumlahS;
    private String categoryFood;
    private Manager picManager;
    private Ranger picRanger;

    public Gudang(String IDGudang, String namaG, double kapasitasMax){}

    public void setPicRanger(Ranger picRanger, ArrayList <StokMakanan>) {}

    public void setPicManager(Manager picManager, Gudang newGudang){}

    public void addStokMakanan(String IDStok, String namaS, double jumlahS, String categoryFood) {}

    public void updateStok(ArrayList<StokMakanan> stok) {}

    public void deleteStok(ArrayList<StokMakanan> stok) {}

    public void cekListGudang();

    public void cekListStok();

    public void statusGudang();

}
