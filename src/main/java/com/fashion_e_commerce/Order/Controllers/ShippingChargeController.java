package com.fashion_e_commerce.Order.Controllers;

import com.fashion_e_commerce.Order.Entities.ShippingCharge;
import com.fashion_e_commerce.Order.Services.ShippingChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping")
public class ShippingChargeController {

    @Autowired
    private ShippingChargeService shippingChargeService;

    @PostMapping("/set")
    public void setShippingCharge(@RequestParam double chargeDhaka,
                                  @RequestParam double chargeOutsideDhaka) {
        shippingChargeService.setShippingCharge(chargeDhaka, chargeOutsideDhaka);
    }

    @GetMapping("/view")
    public ShippingCharge getAllShippingCharge() {
        return shippingChargeService.getAllShippingCharge();
    }
}