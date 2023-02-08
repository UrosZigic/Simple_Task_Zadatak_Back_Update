package com.example.ordinacija.repository;

import com.example.ordinacija.model.Termin;
import jdk.jfr.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TerminRepository extends JpaRepository<Termin, Integer> {

    @Query("select t from Termin t where t.pacijentIdpacijent = ?1 and  t.vreme > ?2 order by t.vreme asc")
    List<Termin> nadjiTerminePacijenta(int idPacijenta, LocalDateTime date);


    @Query("select t from Termin t order by t.vreme asc")
    List<Termin> sviTerminiSortirani();


    @Query("select t from Termin t where t.vreme >= ?1 order by t.vreme asc")
    List<Termin>sviTerminiSortiraniOdDanasPaNadalje(LocalDateTime danas);


    @Query("select t from Termin t where t.vreme >= ?1 and t.vreme < ?2 order by t.vreme asc")
    List<Termin>sviTerminiSortiraniJedanDan(LocalDateTime danas, LocalDateTime sutra);


}
