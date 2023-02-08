package com.example.ordinacija.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Termin {
    private int idtermin;
    private LocalDateTime vreme;
    private int trajanje;
    private String tippregleda;
    private int zubarIdzubar;
    private int pacijentIdpacijent;

    @Id
    @Column(name = "idtermin")
    public int getIdtermin() {
        return idtermin;
    }

    public void setIdtermin(int idtermin) {
        this.idtermin = idtermin;
    }

    @Basic
    @Column(name = "vreme")
    public LocalDateTime getVreme() {
        return vreme;
    }

    public void setVreme(LocalDateTime vreme) {
        this.vreme = vreme;
    }

    @Basic
    @Column(name = "trajanje")
    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    @Basic
    @Column(name = "tippregleda")
    public String getTippregleda() {
        return tippregleda;
    }

    public void setTippregleda(String tippregleda) {
        this.tippregleda = tippregleda;
    }

    @Basic
    @Column(name = "zubar_idzubar")
    public int getZubarIdzubar() {
        return zubarIdzubar;
    }

    public void setZubarIdzubar(int zubarIdzubar) {
        this.zubarIdzubar = zubarIdzubar;
    }

    @Basic
    @Column(name = "pacijent_idpacijent")
    public int getPacijentIdpacijent() {
        return pacijentIdpacijent;
    }

    public void setPacijentIdpacijent(int pacijentIdpacijent) {
        this.pacijentIdpacijent = pacijentIdpacijent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Termin termin = (Termin) o;
        return idtermin == termin.idtermin &&
                trajanje == termin.trajanje &&
                zubarIdzubar == termin.zubarIdzubar &&
                pacijentIdpacijent == termin.pacijentIdpacijent &&
                Objects.equals(vreme, termin.vreme) &&
                Objects.equals(tippregleda, termin.tippregleda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idtermin, vreme, trajanje, tippregleda, zubarIdzubar, pacijentIdpacijent);
    }

    @Override
    public String toString() {
        return "Termin{" +
                "idtermin=" + idtermin +
                ", vreme=" + vreme +
                ", trajanje=" + trajanje +
                ", tippregleda='" + tippregleda + '\'' +
                ", zubarIdzubar=" + zubarIdzubar +
                ", pacijentIdpacijent=" + pacijentIdpacijent +
                '}';
    }



}
