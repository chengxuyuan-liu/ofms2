package com.example.demo.util;

import java.math.BigInteger;

public class UnitChange {
    static final BigInteger BASE = new BigInteger("1024");
    public static BigInteger TranslateGBtoByte(Integer space){
        BigInteger result = new BigInteger(space.toString()).multiply(BASE).multiply(BASE).multiply(BASE);
        return result;
    }
    public static BigInteger TranslateMBtoByte(Integer space){
        BigInteger result = new BigInteger(space.toString()).multiply(BASE).multiply(BASE);
        return result;
    }

    public static BigInteger TranslateMBtoGB(Integer space){
        BigInteger result = new BigInteger(space.toString()).multiply(BASE);
        return result;
    }
}
