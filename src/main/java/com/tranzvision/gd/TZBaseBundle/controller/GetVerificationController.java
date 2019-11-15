package com.tranzvision.gd.TZBaseBundle.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Auther: ZY
 * @Date: 2019/11/15 17:08
 * @Description: 生成随机UUID，dispatcher分发器校验，防止跨站脚本攻击
 */
@RestController
@RequestMapping(value = "/")
public class GetVerificationController {
    public static List<String> VERIFICATION_LIST = new ArrayList<>();

    @RequestMapping(value = "verification")
    public String GetVerification() {
        String uuid = "";
        uuid = UUID.randomUUID().toString();
        //System.out.println("uuid========="+uuid);
        uuid = uuid.replace("-", "");
        VERIFICATION_LIST.add(uuid);
        return uuid;
    }
}
