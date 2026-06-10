package com.fnf.incident.repository;

import com.fnf.incident.domain.incident.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
}