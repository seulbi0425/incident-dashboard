package com.fnf.incident;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentRepository repository;

    public IncidentController(IncidentRepository repository) {
        this.repository = repository;
    }

    // 1) 사고건 전체 목록 받아오기
    @GetMapping
    public List<Incident> getAll() {
        return repository.findAll();
    }

    // 2) 사고건 새로 등록하기
    @PostMapping
    public Incident create(@RequestBody Incident incident) {
        return repository.save(incident);
    }

    // 3) 번호(id)로 사고건 1개만 찾기  ← 새로 추가
    @GetMapping("/{id}")
    public Incident getOne(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // 4) 번호(id)로 사고건 삭제하기  ← 새로 추가
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}