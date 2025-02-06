package com.location.algorithm.design.factory.abstrafactory;

import com.location.algorithm.design.factory.statics.MeizuPhone;
import com.location.algorithm.design.factory.statics.Phone;

public class MeizuPhoneBoxFactory implements  PhoneBoxFactory{
    @Override
    public Charging createChrging() {
        return new CommonCharging();
    }

    @Override
    public Phone createPhone() {
        return new MeizuPhone();
    }
}
