package com.fnf.incident;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // 7) 사고건 철회하기  ← 새로 추가
    //    삭제하지 않고 상태만 'withdrawn'으로 바꿔 기록으로 남긴다
    //    단, '정산완료' 상태인 건은 철회 불가
    //    body 예시: { "withdrawnBy": "메타엠담당", "withdrawReason": "오접수" }
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Incident incident = repository.findById(id).orElse(null);
        if (incident == null) {
            return ResponseEntity.notFound().build();  // 해당 사고건 없음 (404)
        }
        // 정산이 끝난 건은 철회할 수 없게 막는다
        if ("settled".equals(incident.getStatus())) {
            return ResponseEntity.badRequest().body("정산 완료된 사고건은 철회할 수 없습니다");  // 400
        }
        incident.setStatus("withdrawn");
        incident.setWithdrawnBy(body.get("withdrawnBy"));   // 철회한 사람
        incident.setWithdrawnAt(LocalDateTime.now().toString());  // 철회 시각 (서버 기준)
        incident.setWithdrawReason(body.get("withdrawReason"));  // 철회 사유 (없어도 됨)
        return ResponseEntity.ok(repository.save(incident));
    }

    // 8) 사고건 단건 정산완료 처리  ← 새로 추가
    //    body 예시: { "settledBy": "본사관리자" }
    @PutMapping("/{id}/settle")
    public ResponseEntity<?> settle(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Incident incident = repository.findById(id).orElse(null);
        if (incident == null) {
            return ResponseEntity.notFound().build();  // 해당 사고건 없음 (404)
        }
        // 이미 정산완료된 건은 다시 처리하지 않음
        if ("settled".equals(incident.getStatus())) {
            return ResponseEntity.badRequest().body("이미 정산 완료된 사고건입니다");  // 400
        }
        // 철회된 건은 정산완료 처리할 수 없게 막는다
        if ("withdrawn".equals(incident.getStatus())) {
            return ResponseEntity.badRequest().body("철회된 사고건은 정산 완료 처리할 수 없습니다");  // 400
        }
        incident.setStatus("settled");
        incident.setSettledBy(body.get("settledBy"));   // 정산 처리한 사람
        incident.setSettledAt(LocalDateTime.now().toString());  // 정산 시각 (서버 기준)
        return ResponseEntity.ok(repository.save(incident));
    }

    // 9) 사고건 일괄 정산완료 처리  ← 새로 추가
    //    화면에서 여러 건 체크 후 한 번에 정산완료로 변경
    //    body 예시: { "ids": [1, 3, 5], "settledBy": "본사관리자" }
    @PutMapping("/settle")
    public ResponseEntity<?> settleBatch(@RequestBody Map<String, Object> body) {
        // ids 목록 꺼내기
        Object rawIds = body.get("ids");
        if (!(rawIds instanceof List)) {
            return ResponseEntity.badRequest().body("ids 목록이 필요합니다");  // 400
        }
        List<?> ids = (List<?>) rawIds;
        Object settledByObj = body.get("settledBy");
        String settledBy = settledByObj == null ? null : settledByObj.toString();
        String now = LocalDateTime.now().toString();  // 같은 배치는 동일 시각으로 기록

        List<Long> settled = new ArrayList<>();   // 정산완료 처리된 id
        List<Long> skipped = new ArrayList<>();   // 없거나 이미 정산완료라 건너뛴 id

        for (Object rawId : ids) {
            Long id = Long.valueOf(rawId.toString());
            Incident incident = repository.findById(id).orElse(null);
            // 없는 건, 이미 정산완료된 건, 철회된 건은 건너뜀 (철회 절대 변경 금지)
            if (incident == null || "settled".equals(incident.getStatus()) || "withdrawn".equals(incident.getStatus())) {
                skipped.add(id);
                continue;
            }
            incident.setStatus("settled");
            incident.setSettledBy(settledBy);
            incident.setSettledAt(now);
            repository.save(incident);
            settled.add(id);
        }
        // 무엇이 처리/건너뛰어졌는지 응답에 표시
        return ResponseEntity.ok(Map.of(
                "settledCount", settled.size(),
                "settled", settled,
                "skipped", skipped
        ));
    }
}