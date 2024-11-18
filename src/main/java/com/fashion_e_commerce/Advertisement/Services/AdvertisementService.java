package com.fashion_e_commerce.Advertisement.Services;

import com.fashion_e_commerce.Advertisement.Entities.Advertisement;
import com.fashion_e_commerce.Advertisement.Repositories.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    // Add a new advertisement
    public Advertisement addAdvertisement(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    // Get all advertisements
    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

    // Delete an advertisement by ID
    public void deleteAdvertisement(Long id) {
        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        advertisementRepository.delete(ad);
    }
}
