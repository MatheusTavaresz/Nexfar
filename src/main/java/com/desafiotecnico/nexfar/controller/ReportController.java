package com.desafiotecnico.nexfar.controller;

import com.desafiotecnico.nexfar.model.Order;
import com.desafiotecnico.nexfar.report.ReportFilter;
import com.desafiotecnico.nexfar.report.ReportRequest;
import com.desafiotecnico.nexfar.service.ReportService;
import com.desafiotecnico.nexfar.util.DateUtils;
import com.desafiotecnico.nexfar.util.ReportGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<ByteArrayResource> generateReport(@RequestBody ReportRequest reportRequest) {
        try {
            Map<String, Object> filters = convertRequestToFilters(reportRequest);
            logger.info("Filtros aplicados: {}", filters);


            List<Order> orders = reportService.findOrdersWithDynamicFilters(filters);
            logger.info("Dados recebidos: {}", orders.size());

            byte[] reportData = ReportGenerator.generateReport(orders, reportRequest.getKey(), reportRequest.getFormat());
            ByteArrayResource resource = new ByteArrayResource(reportData);
            String reportType = reportRequest.getKey().equals("ORDER_SIMPLE") ? "PedidoResumido" : "PedidoDetalhado";
            String fileExtension = reportRequest.getFormat().equalsIgnoreCase("XLS") ? "xlsx" : "csv";
            String filename = reportType + "." + fileExtension;

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(reportRequest.getFormat().equalsIgnoreCase("XLS") ? MediaType.APPLICATION_OCTET_STREAM : MediaType.TEXT_PLAIN)
                    .body(resource);
        } catch (Exception e) {
            logger.error("Error generating report", e);
            return ResponseEntity.internalServerError().body(new ByteArrayResource(new byte[0]));
        }
    }

    private Map<String, Object> convertRequestToFilters(ReportRequest request) {
        Map<String, Object> filters = new HashMap<>();
        for (ReportFilter filter : request.getFilters()) {
            if (filter.getOperation().equals("INTERVAL") && filter.getValue2() != null) {
                LocalDateTime from = DateUtils.parse(filter.getValue1());
                LocalDateTime to = DateUtils.parse(filter.getValue2());
                if (from != null && to != null) {
                    Map<String, LocalDateTime> range = new HashMap<>();
                    range.put("from", from);
                    range.put("to", to);
                    filters.put(filter.getKey(), range);
                }
            } else {
                if (filter.getKey().equalsIgnoreCase("createdAt")) {
                    LocalDateTime date = DateUtils.parse(filter.getValue1());
                    if (date != null) {
                        filters.put(filter.getKey(), date);
                    }
                } else {
                    filters.put(filter.getKey(), filter.getValue1());
                }
            }
        }
        return filters;
    }
}
