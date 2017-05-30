package com.example.ensai.medic;

/**
 * Created by ensai on 30/05/17.
 */

public class Vaccin {
    private String individu;
    private String denomination;
    private String date;
    private int realise;

    public Vaccin(String ind,String denom,String date,int fait){
        super();
        this.individu=ind;
        this.date=date;
        this.denomination=denom;
        this.realise=fait;
    }

    public void setDate(String date){
        this.date=date;
    }
    public void setDenomination(String denomination){
        this.denomination=denomination;
    }
    public void setIndividu(String individu){
        this.individu=individu;
    }
    public String getDate(){
        return date;
    }
    public String getdenomination(){
        return denomination;
    }
    public String getIndividu(){
        return individu;
    }
    public void setRealise(int fait){
        this.realise=fait;
    }
    public int getRealise(){
        return realise;
    }





}
