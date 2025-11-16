package com.interview.crm.client.service.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.interview.crm.client.dto.ClientCriteria;
import com.interview.crm.client.model.Client;

public final class ClientSpecification {
    public static Specification<Client> byCriteria(ClientCriteria criteria) {
        Specification<Client> spec = Specification.unrestricted();

        if (StringUtils.isNotEmpty(criteria.companyName())) {
            spec = spec.and(hasCompanyNameLike(criteria.companyName()));
        }
        if (StringUtils.isNotEmpty(criteria.industry())) {
            spec = spec.and(hasIndustryLike(criteria.industry()));
        }
        return spec;
    }

    private static Specification<Client> hasCompanyNameLike(String companyName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("companyName")), "%" + companyName.toLowerCase() + "%");
    }

    private static Specification<Client> hasIndustryLike(String industry) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("industry")), "%" + industry.toLowerCase() + "%");
    }
}
