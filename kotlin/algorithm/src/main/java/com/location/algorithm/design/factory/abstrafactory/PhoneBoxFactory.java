package com.location.algorithm.design.factory.abstrafactory;


import com.location.algorithm.design.factory.statics.Phone;

public interface PhoneBoxFactory {
    Charging createChrging();
    Phone createPhone();
}
