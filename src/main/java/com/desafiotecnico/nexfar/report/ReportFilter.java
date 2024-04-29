package com.desafiotecnico.nexfar.report;

import lombok.Data;

@Data
public class ReportFilter {
    private String key;
    private String operation;
    private String value1;
    private String value2;
}
