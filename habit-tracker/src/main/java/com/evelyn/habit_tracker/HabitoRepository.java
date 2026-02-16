package com.evelyn.habit_tracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HabitoRepository extends JpaRepository<Habito, Long> {
    
    
    List<Habito> findByUsuarioEmail(String email);
}