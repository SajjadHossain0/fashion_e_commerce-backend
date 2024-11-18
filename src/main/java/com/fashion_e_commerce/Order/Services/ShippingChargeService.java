package com.fashion_e_commerce.Order.Services;

import com.fashion_e_commerce.Order.Entities.ShippingCharge;
import com.fashion_e_commerce.Order.Repositories.ShippingChargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingChargeService {

    @Autowired
    private ShippingChargeRepository shippingChargeRepository;

    public double getShippingCharge(boolean isDhaka) {
        ShippingCharge shippingCharge = shippingChargeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Shipping charges not set"));
        return isDhaka ? shippingCharge.getChargeDhaka() : shippingCharge.getChargeOutsideDhaka();
    }

    // Fetch full shipping charge object
    public ShippingCharge getAllShippingCharge() {
        return shippingChargeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Shipping charges not set"));
    }

    public void setShippingCharge(double chargeDhaka, double chargeOutsideDhaka) {
        ShippingCharge shippingCharge = new ShippingCharge();
        shippingCharge.setId(1L);
        shippingCharge.setChargeDhaka(chargeDhaka);
        shippingCharge.setChargeOutsideDhaka(chargeOutsideDhaka);
        shippingChargeRepository.save(shippingCharge);
    }
}
