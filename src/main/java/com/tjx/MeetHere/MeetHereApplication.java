package com.tjx.MeetHere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.DigestUtils;

@SpringBootApplication
public class MeetHereApplication {
    public static String salt = "dhfka468538*^*^(;jl";

    public static void main(String[] args) {
        SpringApplication.run(MeetHereApplication.class, args);
    }

    public static String getMD5(String s) {
        String base = s + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

}
