package whereQR.project.domain.dashboard;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;


public class DashboardSpecification {

    public static Specification<Dashboard> contentContains(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(root.get("content"), "%" + search + "%");
        };
    }

    public static Specification<Dashboard> lostedDistrictEquals(String lostedDistrict) {
        return (root, query, cb) -> {
            if (lostedDistrict == null || lostedDistrict.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("lostedDistrict"), lostedDistrict);
        };
    }

    public static Specification<Dashboard> lostedTypeEquals(String lostedType) {
        return (root, query, cb) -> {
            if (lostedType == null || lostedType.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("lostedType"), lostedType);
        };
    }

    public static Specification<Dashboard> createdAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) {
                return cb.conjunction();
            }
            return cb.between(root.get("createdAt"), startDate, endDate);
        };
    }
}