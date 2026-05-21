package com.ragapi.repository;

import com.ragapi.entity.FormTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormTemplateRepository extends MongoRepository<FormTemplate, String> {
    
    // Tìm form theo form code
    Optional<FormTemplate> findByFormCode(String formCode);
    
    // Tìm form theo form type
    List<FormTemplate> findByFormType(String formType);
    
    // Tìm tất cả active form
    List<FormTemplate> findByActiveTrue();
    
    // Tìm form có chứa keyword trong keywords hoặc categories
    @Query("{ $or: [ { 'keywords': ?0 }, { 'categories': ?0 } ] }")
    List<FormTemplate> findByKeywordOrCategory(String keyword);
    
    // Tìm form theo form code - text search
    @Query("{ 'formName': { $regex: ?0, $options: 'i' } }")
    List<FormTemplate> findByFormNameContains(String name);
}

