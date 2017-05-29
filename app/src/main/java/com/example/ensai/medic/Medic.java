package com.example.ensai.medic;

import java.util.Date;

/**
 * Created by ensai on 18/05/17.
 */

public class Medic {
    String name;
    int idMedic;
    String codeCIS;
    String peremption;
    public Medic(int idMedic,String name,String codeCIS){
        super();
        this.setName(name);
        this.setIdMedic(idMedic);
        this.setCodeCIS(codeCIS);
      //  this.setQuantity(quantity);
    }
    public Medic(int idMedic, String name, String codeCIS,String peremption){
        super();
        this.setName(name);
        this.setIdMedic(idMedic);
        this.setCodeCIS(codeCIS);
        this.setPeremption(peremption);
    }



    public void setCodeCIS(String codeCIS) {
        this.codeCIS=codeCIS;
    }
    public String getCodeCIS() {
        return codeCIS;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }
    public int getIdMedic(){
        return this.idMedic;
    }
    public void setIdMedic(int idMedic){
        this.idMedic=idMedic;
    }
    public void setPeremption(String peremption) {
        this.peremption=peremption;
    }
    public String getPeremption() {
        return peremption;
    }
}
