package com.c203.db.Repository;

import com.c203.db.Entity.RoomDeco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDecoRepository extends JpaRepository<RoomDeco,Integer> {
}