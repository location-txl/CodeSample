package com.location.algorithm.design.factory.abstrafactory;

import com.location.algorithm.design.factory.statics.Phone;
import com.location.algorithm.design.factory.statics.XiaomiPhone;

public class XiaomiPhoneBoxFactory implements  PhoneBoxFactory{
    @Override
    public Charging createChrging() {
        return new CommonCharging();
    }

    @Override
    public Phone createPhone() {
        return new XiaomiPhone();
    }
}
