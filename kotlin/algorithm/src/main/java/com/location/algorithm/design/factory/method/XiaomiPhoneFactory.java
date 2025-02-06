package com.location.algorithm.design.factory.method;

import com.location.algorithm.design.factory.statics.Phone;
import com.location.algorithm.design.factory.statics.XiaomiPhone;

public class XiaomiPhoneFactory implements IPhoneFactory{
    @Override
    public Phone createPhone() {
        return new XiaomiPhone();
    }
}
