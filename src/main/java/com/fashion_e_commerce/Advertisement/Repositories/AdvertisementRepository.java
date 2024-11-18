package com.fashion_e_commerce.Advertisement.Repositories;

import com.fashion_e_commerce.Advertisement.Entities.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
