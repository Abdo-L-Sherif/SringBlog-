package org.studyeasy.sptingBlog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.sptingBlog.models.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long>{
    
}
