package com.example.ordinacija.repository;

import com.example.ordinacija.model.Zubar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZubarRepository extends JpaRepository<Zubar, Integer> {

    Zubar findByKod(String kod);
}
