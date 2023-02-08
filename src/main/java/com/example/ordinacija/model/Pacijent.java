package com.example.ordinacija.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Pacijent {
    private int idpacijent;
    private String ime;
    private String prezime;
    private String email;
    private String brojtelefona;

    @Id
    @Column(name = "idpacijent")
    public int getIdpacijent() {
        return idpacijent;
    }

    public void setIdpacijent(int idpacijent) {
        this.idpacijent = idpacijent;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pacijent pacijent = (Pacijent) o;
        return idpacijent == pacijent.idpacijent &&
                Objects.equals(ime, pacijent.ime) &&
                Objects.equals(prezime, pacijent.prezime) &&
                Objects.equals(email, pacijent.email) &&
                Objects.equals(brojtelefona, pacijent.brojtelefona);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idpacijent, ime, prezime, email, brojtelefona);
    }

    @Override
    public String toString() {
        return "Pacijent{" +
                "idpacijent=" + idpacijent +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", email='" + email + '\'' +
                ", brojtelefona='" + brojtelefona + '\'' +
                '}';
    }
}
