package com.fnf.incident;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    //    createdBy는 요청 body에서 받고, createdAt은 서버에서 현재 시각으로 자동 저장
    @PostMapping
    public Incident create(@RequestBody Incident incident) {
        incident.setCreatedAt(LocalDateTime.now().toString());  // 접수일시 자동 기록
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

    // 5) 사고건 승인하기  ← 새로 추가
    //    body 예시: { "approvedBy": "본사관리자" }
    @PutMapping("/{id}/approve")
    public Incident approve(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Incident incident = repository.findById(id).orElse(null);
        if (incident == null) {
            return null;  // 해당 사고건 없음
        }
        incident.setStatus("approved");
        incident.setApprovedBy(body.get("approvedBy"));   // 처리한 사람
        incident.setApprovedAt(LocalDateTime.now().toString());  // 승인 시각 (서버 기준)
        return repository.save(incident);
    }

    // 6) 사고건 반려하기  ← 새로 추가
    //    body 예시: { "rejectedBy": "로젠담당", "rejectReason": "송장정보 불일치" }
    @PutMapping("/{id}/reject")
    public Incident reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Incident incident = repository.findById(id).orElse(null);
        if (incident == null) {
            return null;  // 해당 사고건 없음
        }
        incident.setStatus("rejected");
        incident.setRejectedBy(body.get("rejectedBy"));   // 처리한 사람
        incident.setRejectedAt(LocalDateTime.now().toString());  // 반려 시각 (서버 기준)
        incident.setRejectReason(body.get("rejectReason"));  // 반려 사유
        return repository.save(incident);
    }
}