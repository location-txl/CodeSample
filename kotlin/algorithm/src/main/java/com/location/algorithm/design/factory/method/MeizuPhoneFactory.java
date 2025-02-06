package com.location.algorithm.design.factory.method;

import com.location.algorithm.design.factory.statics.MeizuPhone;
import com.location.algorithm.design.factory.statics.Phone;

public class MeizuPhoneFactory implements IPhoneFactory{
    @Override
    public Phone createPhone() {
        return new MeizuPhone();
    }
}
