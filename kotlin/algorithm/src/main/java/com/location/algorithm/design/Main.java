package com.location.algorithm.design;

import com.location.algorithm.design.daili.BannerShopStore;
import com.location.algorithm.design.daili.ProxyFactory;
import com.location.algorithm.design.daili.ShopStore;
import com.location.algorithm.design.factory.abstrafactory.MeizuPhoneBoxFactory;
import com.location.algorithm.design.factory.abstrafactory.PhoneBoxFactory;
import com.location.algorithm.design.factory.abstrafactory.XiaomiPhoneBoxFactory;
import com.location.algorithm.design.factory.method.IPhoneFactory;
import com.location.algorithm.design.factory.method.*;
import com.location.algorithm.design.factory.statics.Phone;
import com.location.algorithm.design.factory.statics.PhoneFactory;
import com.location.algorithm.design.zerenlian.Request;
import com.location.algorithm.design.zerenlian.Response;
import com.location.algorithm.design.zerenlian.sample.java.ChinClient;
import com.location.algorithm.design.zerenlian.sample.java.*;

public class Main {


    /**
     * 责任链模式
     */
    public void testZeRenLian(){
        ChinClient client = new ChinClient();
        client.addInterceptor(new AInterceptor());
        client.addInterceptor(new BInterceptor());
        client.addInterceptor(new CInterceptor());
        Response response = client.proceed(new Request("拦截器", 260));
        System.out.println(response.getBody());
    }

    /**
     * 代理模式
     */
    public void daili() {
        ProxyFactory proxy = new ProxyFactory();
        ShopStore shopStore = new BannerShopStore();
        ShopStore proxyService = proxy.createProxy(shopStore);
        proxyService.payBanner(3);
        ShopStore shopStoreProxy = proxy.createShopStoreProxy(shopStore);
        System.out.println("shopStoreProxy.payBanner(3) = " + shopStoreProxy.payBanner(3));

    }

    /**
     * 静态工厂模式
     * 通过 {@link PhoneFactory#createPhone(PhoneFactory.PhoneType)}函数创建实际的手机对象
     * 如果需要再增加一个手如果需要再增加一个手机机 则需要修改{@link PhoneFactory}的函数
     */
    public void staticFactory(){
        Phone meizuPhone = PhoneFactory.createPhone(PhoneFactory.PhoneType.MEIZU);
        Phone xiaomiPhone = PhoneFactory.createPhone(PhoneFactory.PhoneType.XIAOMI);
        meizuPhone.open();
        xiaomiPhone.open();
    }

    /**
     * 方法工厂模式
     * @see IPhoneFactory
     * @see MeizuPhoneFactory
     * @see XiaomiPhoneFactory
     */
    public  void methodFactory(){
        IPhoneFactory xiaomiFactory = new XiaomiPhoneFactory();
        IPhoneFactory meizuFactory = new MeizuPhoneFactory();
        Phone xiaomiPhone = xiaomiFactory.createPhone();
        Phone meizuPhone = meizuFactory.createPhone();
        xiaomiPhone.open();
        meizuPhone.open();
    }

    /**
     * 抽象工厂模式
     * @see Charging
     * @see PhoneBoxFactory
     */
    public void abstractFactory(){
        PhoneBoxFactory xiaomiPhoneBox = new XiaomiPhoneBoxFactory();
        PhoneBoxFactory meizuPhoneBox = new MeizuPhoneBoxFactory();
        xiaomiPhoneBox.createPhone().open();
        xiaomiPhoneBox.createChrging().charg();
        meizuPhoneBox.createPhone().open();
        meizuPhoneBox.createChrging().charg();
    }
}
