package com.fashion_e_commerce.Advertisement.Controllers;


import com.fashion_e_commerce.Advertisement.Entities.Advertisement;
import com.fashion_e_commerce.Advertisement.Services.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
@CrossOrigin
public class AdvertisementController {
    @Autowired
    private AdvertisementService advertisementService;

    // Add a new advertisement
    @PostMapping
    public ResponseEntity<Advertisement> addAdvertisement(
            @RequestParam("title") String title,
            @RequestParam("image") MultipartFile image) throws IOException {

        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(title);
        advertisement.setImage(image.getBytes());

        Advertisement savedAd = advertisementService.addAdvertisement(advertisement);
        return ResponseEntity.ok(savedAd);
    }

    // Get all advertisements
    @GetMapping
    public ResponseEntity<List<Advertisement>> getAllAdvertisements() {
        List<Advertisement> ads = advertisementService.getAllAdvertisements();
        return ResponseEntity.ok(ads);
    }

    // Delete an advertisement by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.ok("Advertisement deleted successfully");
    }
}