package com.example.ordinacija.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Zubar {
    private int idzubar;
    private String ime;
    private String prezime;
    private String email;
    private String brojtelefona;
    private String kod;
    private int vremeotkazivanje;

    @Id
    @Column(name = "idzubar")
    public int getIdzubar() {
        return idzubar;
    }

    public void setIdzubar(int idzubar) {
        this.idzubar = idzubar;
    }

    @Basic
    @Column(name = "ime")
    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    @Basic
    @Column(name = "prezime")
    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "brojtelefona")
    public String getBrojtelefona() {
        return brojtelefona;
    }

    public void setBrojtelefona(String brojtelefona) {
        this.brojtelefona = brojtelefona;
    }

    @Basic
    @Column(name = "kod")
    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    @Basic
    @Column(name = "vremeotkazivanje")
    public int getVremeotkazivanje() {
        return vremeotkazivanje;
    }

    public void setVremeotkazivanje(int vremeotkazivanje) {
        this.vremeotkazivanje = vremeotkazivanje;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zubar zubar = (Zubar) o;
        return idzubar == zubar.idzubar &&
                vremeotkazivanje == zubar.vremeotkazivanje &&
                Objects.equals(ime, zubar.ime) &&
                Objects.equals(prezime, zubar.prezime) &&
                Objects.equals(email, zubar.email) &&
                Objects.equals(brojtelefona, zubar.brojtelefona) &&
                Objects.equals(kod, zubar.kod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idzubar, ime, prezime, email, brojtelefona, kod, vremeotkazivanje);
    }

    @Override
    public String toString() {
        return "Zubar{" +
                "idzubar=" + idzubar +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", email='" + email + '\'' +
                ", brojtelefona='" + brojtelefona + '\'' +
                ", kod='" + kod + '\'' +
                ", vremeotkazivanje=" + vremeotkazivanje +
                '}';
    }
}
