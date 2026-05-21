package com.ragapi.repository;

import com.ragapi.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    
    // Tìm profile theo userId
    Optional<UserProfile> findByUserId(String userId);
}

