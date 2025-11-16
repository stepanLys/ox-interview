package com.interview.crm.contact.service.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.interview.crm.contact.dto.ContactCriteria;
import com.interview.crm.contact.model.Contact;

public final class ContactSpecification {
    public static Specification<Contact> byCriteria(Long clientId, ContactCriteria criteria) {
        Specification<Contact> spec = (root, query, cb) -> cb.equal(root.get("client").get("id"), clientId);

        if (StringUtils.isNotEmpty(criteria.firstName())) {
            spec = spec.and(hasFirstNameLike(criteria.firstName()));
        }
        if (StringUtils.isNotEmpty(criteria.email())) {
            spec = spec.and(hasEmailLike(criteria.email()));
        }
        return spec;
    }

    private static Specification<Contact> hasFirstNameLike(String firstName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    private static Specification<Contact> hasEmailLike(String email) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
}
