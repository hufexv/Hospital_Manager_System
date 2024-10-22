package com.hospital.utils;

import java.util.Random;

//RandomUtil类提供了两种生成随机数的方法，分别用于生成订单ID和六位随机验证码。通过这种方式，可以为订单生成唯一性标识，或为用户提供随机验证码，增加系统的安全性和灵活性。

public class RandomUtil {
    public static Integer randomOid(int oId){
        int flag = new Random().nextInt(9999);
        if (flag < 1000)
            flag += 1000;
        return oId+flag;
    }
    public static Integer randomCode(){
        int flag = new Random().nextInt(999999);
        if (flag < 100000)
            flag += 100000;
        return flag;
    }

}
