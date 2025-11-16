package com.interview.crm.common.export;

import com.interview.crm.client.dto.ClientResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExportService {

    private final Map<String, ExportStrategy<ClientResponse>> clientStrategies;

    public ExportService(List<ExportStrategy<ClientResponse>> clientStrategyList) {
        this.clientStrategies = clientStrategyList.stream()
                .collect(Collectors.toMap(ExportStrategy::getFormat, Function.identity()));
    }

    public ExportStrategy<ClientResponse> getClientStrategy(String format) {
        ExportStrategy<ClientResponse> strategy = clientStrategies.get(format.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
        return strategy;
    }
}