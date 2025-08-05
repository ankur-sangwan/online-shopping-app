package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.ReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnEntityRepository extends JpaRepository<ReturnEntity,Integer> {
}
