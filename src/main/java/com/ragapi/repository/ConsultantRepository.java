package com.ragapi.repository;

import com.ragapi.entity.Consultant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultantRepository extends MongoRepository<Consultant, String> {
    
    /**
     * Tìm consultant theo category
     */
    List<Consultant> findByCategories(String category);
    
    /**
     * Tìm consultant theo specialization
     */
    List<Consultant> findBySpecializations(String specialization);
    
    /**
     * Tìm consultant theo code
     */
    Optional<Consultant> findByConsultantCode(String code);
    
    /**
     * Tìm consultant theo email
     */
    Optional<Consultant> findByEmail(String email);
    
    /**
     * Tìm consultant active (đang hoạt động)
     */
    List<Consultant> findByActiveTrue();
    
    /**
     * Tìm consultant theo category và active
     */
    List<Consultant> findByActiveTrueAndCategoriesIn(List<String> categories);
    
    /**
     * Tìm consultant verified
     */
    List<Consultant> findByVerifiedTrue();
    
    /**
     * Tìm consultant theo keyword (full text search)
     */
    @Query("{ 'keywords': { $in: ?0 }, 'active': true }")
    List<Consultant> searchByKeywords(List<String> keywords);
    
    /**
     * Tìm consultant có rating >= minRating
     */
    List<Consultant> findByAverageRatingGreaterThanEqual(Double minRating);
    
    /**
     * Tìm consultant với số chat hiện tại < max
     */
    @Query("{ 'active': true }")
    List<Consultant> findAvailableConsultants();
}

