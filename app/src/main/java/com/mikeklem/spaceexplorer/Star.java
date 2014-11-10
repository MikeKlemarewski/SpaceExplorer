package com.mikeklem.spaceexplorer;

/**
 * Created by juan_laramoreno on 14-11-08.
 */
public class Star {
    private int StarID;
    private String HIP;
    private String HD;
    private String HR;
    private String Gliese;
    private String BayerFlamsteed;
    private String ProperName;
    private String RA;
    private String Dec;
    private String Distance;
    private String PMRA;
    private String PMDec;
    private String RV;
    private float Mag;
    private float AbsMag;
    private String Spectrum;
    private String ColorIndex;
    private String X;
    private String Y;
    private String Z;
    private String VX;
    private String VY;
    private String VZ;
    private int quadrant;


    public int getStarID() {
        return StarID;
    }

    public void setStarID(int starID) {
        StarID = starID;
    }

    public String getHIP() {
        return HIP;
    }

    public void setHIP(String HIP) {
        this.HIP = HIP;
    }

    public String getHD() {
        return HD;
    }

    public void setHD(String HD) {
        this.HD = HD;
    }

    public String getHR() {
        return HR;
    }

    public void setHR(String HR) {
        this.HR = HR;
    }

    public String getGliese() {
        return Gliese;
    }

    public void setGliese(String gliese) {
        Gliese = gliese;
    }

    public String getBayerFlamsteed() {
        return BayerFlamsteed;
    }

    public void setBayerFlamsteed(String bayerFlamsteed) {
        BayerFlamsteed = bayerFlamsteed;
    }

    public String getProperName() {
        return ProperName;
    }

    public void setProperName(String properName) {
        ProperName = properName;
    }

    public String getRA() {
        return RA;
    }

    public void setRA(String RA) {
        this.RA = RA;
    }

    public String getDec() {
        return Dec;
    }

    public void setDec(String dec) {
        Dec = dec;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getPMRA() {
        return PMRA;
    }

    public void setPMRA(String PMRA) {
        this.PMRA = PMRA;
    }

    public String getPMDec() {
        return PMDec;
    }

    public void setPMDec(String PMDec) {
        this.PMDec = PMDec;
    }

    public String getRV() {
        return RV;
    }

    public void setRV(String RV) {
        this.RV = RV;
    }

    public float getMag() {
        return Mag;
    }

    public void setMag(float mag) {
        Mag = mag;
    }

    public float getAbsMag() {
        return AbsMag / 5;
    }

    public void setAbsMag(float absMag) {
        AbsMag = absMag;
    }

    public String getSpectrum() {
        return Spectrum;
    }

    public void setSpectrum(String spectrum) {
        Spectrum = spectrum;
    }

    public String getColorIndex() {
        return ColorIndex;
    }

    public void setColorIndex(String colorIndex) {
        ColorIndex = colorIndex;
    }

    public float getX() {
        return Float.parseFloat(X) * 10;
    }

    public void setX(String x) {
        X = x;
    }

    public float getY() {
        return Float.parseFloat(Y) * 10;
    }

    public void setY(String y) {
        Y = y;
    }

    public float getZ() {
        return Float.parseFloat(Z) * 10;
    }

    public void setZ(String z) {
        Z = z;
    }

    public String getVX() {
        return VX;
    }

    public void setVX(String VX) {
        this.VX = VX;
    }

    public String getVY() {
        return VY;
    }

    public void setVY(String VY) {
        this.VY = VY;
    }

    public String getVZ() {
        return VZ;
    }

    public void setVZ(String VZ) {
        this.VZ = VZ;
    }

    public int getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(int quadrant) {
        this.quadrant = quadrant;
    }
}
