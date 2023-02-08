package com.example.ordinacija.service;

import com.example.ordinacija.model.Pacijent;
import com.example.ordinacija.model.Termin;
import com.example.ordinacija.model.Zubar;
import com.example.ordinacija.repository.PacijentRepository;
import com.example.ordinacija.repository.TerminRepository;
import com.example.ordinacija.repository.ZubarRepository;
import com.fasterxml.jackson.databind.deser.std.JsonLocationInstantiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.desktop.ScreenSleepEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class OrdinacijaService {

    @Autowired
    private ZubarRepository zubarRepository;

    @Autowired
    private PacijentRepository pacijentRepository;

    @Autowired
    private TerminRepository terminRepository;

    @Autowired
    private EmailService emailService;


    public boolean zakazi(Map<String, String> json) {
        LocalDateTime now = LocalDateTime.now();

        Pacijent pacijent = pacijentRepository.findByBrojtelefona(json.get("brojtelefona"));

        Termin termin = new Termin();
        termin.setVreme(LocalDateTime.parse(json.get("vreme")));
        termin.setTrajanje(Integer.parseInt(json.get("trajanje")));
        termin.setTippregleda(json.get("tippregleda"));
        termin.setPacijentIdpacijent(pacijent.getIdpacijent());
        termin.setZubarIdzubar(1);


        if (termin.getVreme().isBefore(now)) {
            return false;
        }


        if (!(termin.getVreme().getMinute() == 0 || termin.getVreme().getMinute()  == 30)) {
            return false;
        }

        if (termin.getTrajanje() != 30 && termin.getTrajanje() != 60) {
            return false;
        }

        LocalDate vreme = termin.getVreme().toLocalDate();


        List<LocalDateTime> slobodniTerminiTogDana = slobodniTermini(termin.getTrajanje(), vreme);

        if (!slobodniTerminiTogDana.contains(termin.getVreme())) {
            return false;
        }


        terminRepository.save(termin);


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm, dd-MM-yyyy");

        emailService.posaljiEmail(zubarRepository.findAll().get(0).getEmail(), "Zakazan termin",
                "Zakazan je termin (" + termin.getTrajanje() + "min) u " + termin.getVreme().format(dateTimeFormatter) + ", za pacijenta " + pacijent.getIme() + " " + pacijent.getPrezime() + ".\nTip: " + termin.getTippregleda() + ".");

        emailService.posaljiEmail(pacijent.getEmail(), "Zakazan termin",
                "Postovani " + pacijent.getIme() + ",\n\nUspesno ste zakazali termin (" + termin.getTrajanje() + "min) u " + termin.getVreme().format(dateTimeFormatter) + ".\nTip: " + termin.getTippregleda() + ".\n\nSrdacan pozdrav,\nZubarska Ordinacija");

        return true;
    }


    public boolean dodajPacijenta(Map<String, String> json) {
        Pacijent pacijent = new Pacijent();

        if (json.get("ime") == "" || json.get("prezime") == "" || json.get("email") == "" || json.get("brojtelefona") == "" || json.get("ime") == null || json.get("prezime") == null || json.get("email") == null || json.get("brojtelefona") == null) {
            return false;
        }
        else {
            pacijent.setIme(json.get("ime"));
            pacijent.setPrezime(json.get("prezime"));
            pacijent.setEmail(json.get("email"));
            pacijent.setBrojtelefona(json.get("brojtelefona"));

            pacijentRepository.save(pacijent);

            return true;
        }

    }


    public boolean otkazi(Map<String, String> json) {
        int idTermin = Integer.parseInt(json.get("idTermin"));
        String brojTelefona = json.get("brojTelefona");

        Termin termin = terminRepository.findById(idTermin).get();

        Pacijent pacijent = pacijentRepository.findById(termin.getPacijentIdpacijent()).get();


        if (identifikacija(brojTelefona) == 0) {
            terminRepository.delete(termin);
        }
        else {
            if (!pacijent.getBrojtelefona().equals(brojTelefona)) {
                return false;
            }

            int vremeOtkazivanjeSekunde = zubarRepository.findAll().get(0).getVremeotkazivanje() * 3600;
            LocalDateTime now = LocalDateTime.now();
            if (now.until(termin.getVreme(), ChronoUnit.SECONDS) < vremeOtkazivanjeSekunde) {
                return false;
            }

            terminRepository.delete(termin);
        }



        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm, dd-MM-yyyy");

        emailService.posaljiEmail(zubarRepository.findAll().get(0).getEmail(), "Otkazan termin",
                "Otkazan je termin (" + termin.getTrajanje() + "min) u " + termin.getVreme().format(dateTimeFormatter) + ", za pacijenta " + pacijent.getIme() + " " + pacijent.getPrezime() + ".\nTip: " + termin.getTippregleda() + ".");

        emailService.posaljiEmail(pacijent.getEmail(), "Otkazan termin",
                "Postovani " + pacijent.getIme() + ",\n\nUspesno ste otkazali termin (" + termin.getTrajanje() + "min) u " + termin.getVreme().format(dateTimeFormatter) + ".\nTip: " + termin.getTippregleda() + ".\n\nSrdacan pozdrav,\nZubarska Ordinacija");


        return true;
    }


    public List<Termin> getTermini(String brojTelefona) {

        List<Termin> termini = new ArrayList<Termin>();

        if (identifikacija(brojTelefona) == 0) {
            return terminRepository.sviTerminiSortiraniOdDanasPaNadalje(LocalDateTime.now());
        }



        Pacijent pacijent = pacijentRepository.findByBrojtelefona(brojTelefona);

        if (pacijent == null) {
            return termini;
        }

        termini = terminRepository.nadjiTerminePacijenta(pacijent.getIdpacijent(), LocalDateTime.now());

        return termini;
    }


    public List<Termin> getSviTermini() {
        return terminRepository.sviTerminiSortirani();
    }

    public List<Termin> getSviTerminiOdDanasPaNadalje() {
        LocalDateTime danas = LocalDate.now().atTime(0, 0);

        return terminRepository.sviTerminiSortiraniOdDanasPaNadalje(danas);
    }


    public List<Termin> getSviTerminiOdDanas() {
        LocalDateTime danas = LocalDate.now().atTime(0, 0);

        return terminRepository.sviTerminiSortiraniJedanDan(danas, danas.plusDays(1));
    }


    public List<Termin> getSviTerminiOdDatuma(LocalDateTime vremeTermina) {
        LocalDate datum = vremeTermina.toLocalDate();
        LocalDateTime datumUPonoc = datum.atTime(0, 0);

        return terminRepository.sviTerminiSortiraniJedanDan(datumUPonoc, datumUPonoc.plusDays(1));
    }




    public List<LocalDateTime> slobodniTermini(int trajanjeTermina, LocalDate datum) {
        List<LocalDateTime> slobodniTermini = new ArrayList<>();

        if (datum.isBefore(LocalDate.now())) {
            return slobodniTermini;
        }

        LocalDateTime datumSaVremenom = datum.atTime(0, 0);

        List<Termin> lista = getSviTerminiOdDatuma(datumSaVremenom);


        LocalDateTime pocetak = datum.atTime(9, 0);
        LocalDateTime kraj = datum.atTime(17, 0);



        slobodniTermini.add(pocetak);
        for (int i = 1; i < 16; i++) {
            slobodniTermini.add(pocetak.plusMinutes(30*i));
        }


        List<LocalDateTime> zauzetiTermini = new ArrayList<>();
        for (Termin temp : lista) {
            if (temp.getTrajanje() == 30) {
                zauzetiTermini.add(temp.getVreme());
            } else {
                zauzetiTermini.add(temp.getVreme());
                zauzetiTermini.add(temp.getVreme().plusMinutes(30));
            }
        }

        slobodniTermini.removeAll(zauzetiTermini);


        if (trajanjeTermina == 60) {

            for (int i = 0; i < slobodniTermini.size()-1; i++) {
                if (!(slobodniTermini.get(i).plusMinutes(30).isEqual(slobodniTermini.get(i + 1)))) {
                    slobodniTermini.remove(i);
                    i--;
                }
            }

            if ( slobodniTermini.size() != 0 && (slobodniTermini.get(slobodniTermini.size()-1).plusMinutes(30).isEqual(kraj))) {
                slobodniTermini.remove(slobodniTermini.size()-1);
            }

        }


        if (datum.isEqual(LocalDate.now())) {
            LocalDateTime trenutnoVreme = LocalDateTime.now();
            for (int i = 0; i < slobodniTermini.size(); i++) {
                if (slobodniTermini.get(i).isBefore(trenutnoVreme)) {
                    slobodniTermini.remove(i);
                    i--;
                }
            }
        }


        return slobodniTermini;
    }


    public Pacijent getPacijenta(String broj) {
        return pacijentRepository.findByBrojtelefona(broj);
    }


    public int identifikacija(String brojtelefona) {

        String sifra = brojtelefona;

        Zubar zubar = zubarRepository.findByKod(sifra);

        if (zubar != null) {
            return 0;
        }

        if (brojtelefona.matches("[0-9]+") && brojtelefona.length() > 2) {
            return 1;
        }

        return 2;
    }


    public String sifraAlgoritam(String sifra) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sifra.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return generatedPassword;
    }



    public int getVremeOtkazivanje() {
        return zubarRepository.findAll().get(0).getVremeotkazivanje(); //ovako moze jer broj zubara nije konfigurabilan
    }


    public boolean setVremeOtkazivanje(int vreme, String brojtelefona) {

        if (identifikacija(brojtelefona) == 0) {
            Zubar zubar = zubarRepository.findAll().get(0);
            zubar.setVremeotkazivanje(vreme);

            if(zubarRepository.save(zubar) != null) {
                return true;
            }
        }

        return false;
    }


    public List<Map<String, String>> getTerminiKalendar(String brojTelefona) {

        if (identifikacija(brojTelefona) == 0) {

            List<Termin> termini = new ArrayList<Termin>();

            termini = terminRepository.sviTerminiSortirani();

            List<Map<String, String>> formatiraniTermini = new ArrayList<Map<String, String>>();

            for (Termin termin : termini) {
                Pacijent pacijent = pacijentRepository.findById(termin.getPacijentIdpacijent()).get();

                Map<String, String> temp = new HashMap<String, String>() {
                    {
                        put("id", String.valueOf(termin.getIdtermin()));
                        put("title", pacijent.getIme() + " (" + pacijent.getBrojtelefona() + "), " + termin.getTippregleda());
                        put("start", String.valueOf(termin.getVreme()));
                        put("end", String.valueOf(termin.getVreme().plusMinutes(termin.getTrajanje())));
                    }
                };
                formatiraniTermini.add(temp);
            }
            return formatiraniTermini;

        }

        return null;

    }


}