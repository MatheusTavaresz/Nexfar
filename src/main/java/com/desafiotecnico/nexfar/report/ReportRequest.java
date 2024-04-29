package com.desafiotecnico.nexfar.report;

import com.desafiotecnico.nexfar.model.Order;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ReportRequest {
    private String key;
    private String format;
    private List<ReportFilter> filters;
}
