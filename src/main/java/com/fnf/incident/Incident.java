package com.fnf.incident;

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
}