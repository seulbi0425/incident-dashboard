package com.fnf.incident.domain.incident;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;        // 브랜드 (MLB, DX 등)
    private String orderNo;      // 주문번호
    private String trackingNo;   // 송장번호
    private String incidentType; // 사고유형
    private String status;       // 처리상태

    private String carrier;      // 택배사 (로젠, CJ)
    private String incidentDate; // 사고발생일
    private String season;       // 시즌
    private String styleCode;    // 스타일코드
    private String color;        // 컬러
    private String size;         // 사이즈
    private String memo;         // 메모

    private Integer quantity = 1; // 수량 (기본값 1)
    private Integer amount = 0;   // 금액 (기본값 0)

    private String createdAt;     // 접수일시 (등록 시 자동 저장)
    private String createdBy;     // 등록자

    private String approvedBy;    // 승인자
    private String approvedAt;    // 승인일시
    private String rejectedBy;    // 반려자
    private String rejectedAt;    // 반려일시
    private String rejectReason;  // 반려사유

    private String withdrawnBy;   // 철회자
    private String withdrawnAt;   // 철회일시
    private String withdrawReason;// 철회사유

    private String settledBy;     // 정산자
    private String settledAt;     // 정산일시
}