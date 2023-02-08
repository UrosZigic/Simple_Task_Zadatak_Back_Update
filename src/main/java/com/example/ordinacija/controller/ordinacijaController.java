package com.example.ordinacija.controller;


import com.example.ordinacija.model.Pacijent;
import com.example.ordinacija.model.Termin;
import com.example.ordinacija.repository.PacijentRepository;
import com.example.ordinacija.repository.TerminRepository;
import com.example.ordinacija.repository.ZubarRepository;
import com.example.ordinacija.service.OrdinacijaService;
import com.example.ordinacija.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ordinacijaController {


    @Autowired
    TokenService tokenService;

    @Autowired
    private ZubarRepository zubarRepository;

    @Autowired
    private PacijentRepository pacijentRepository;

    @Autowired
    private TerminRepository terminRepository;

    @Autowired
    private OrdinacijaService ordinacijaService;



    @PostMapping("/token")
    public String token(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        return token;
    }


    @PostMapping("/zakazi")
    public boolean zakazi(@RequestBody Map<String, String> json) {
        return ordinacijaService.zakazi(json);
    }

    @PostMapping("/zakaziNovog")
    public boolean zakaziNovog(@RequestBody Map<String, String> json) {
        if (ordinacijaService.dodajPacijenta(json)) {
            return ordinacijaService.zakazi(json);
        }
        return false;
    }



    @PostMapping("/otkazi")
    public boolean otkazi(@RequestBody Map<String, String> json) {
        return ordinacijaService.otkazi(json);
    }


    @GetMapping("/pregled")
    public List<Termin> getTermini(@RequestParam String brojTelefona) {
        return ordinacijaService.getTermini(brojTelefona);
    }


    @GetMapping("/pregledZubar")
    public List<Map<String, String>> getTerminiKalendar(@RequestParam String brojTelefona) {
        return ordinacijaService.getTerminiKalendar(brojTelefona);
    }


    @GetMapping("/slobodni")
    public List<LocalDateTime> getSlobodniTermini(@RequestParam() int trajanje, @RequestParam() @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate datum) {
        return ordinacijaService.slobodniTermini(trajanje, datum);
    }


    @GetMapping("/nadjiPacijenta")
    public Pacijent getPacijent(@RequestParam String brojTelefona) {
        return ordinacijaService.getPacijenta(brojTelefona);
    }


    @GetMapping("/ident")
    public int Identifikacija(@RequestParam String brojTelefona) {
        return ordinacijaService.identifikacija(brojTelefona);
    }


    @GetMapping("/vremeOtkazivanje")
    public int getvremeOtkazivanje() {
        return ordinacijaService.getVremeOtkazivanje();
    }


    @PutMapping("/setVremeOtkazivanje/{vreme}/{brojtelefona}")
    public boolean setvremeOtkazivanje(@PathVariable("vreme") int vreme, @PathVariable("brojtelefona") String brojtelefona) {
        return ordinacijaService.setVremeOtkazivanje(vreme, brojtelefona);
    }

}
