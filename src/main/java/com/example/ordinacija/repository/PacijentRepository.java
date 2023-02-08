package com.example.ordinacija.repository;

import com.example.ordinacija.model.Pacijent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacijentRepository extends JpaRepository<Pacijent, Integer> {

    Pacijent findByBrojtelefona(String brojTelefona);
}
