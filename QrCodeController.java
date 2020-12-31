package com.aop.aoptest.controller;

import com.aop.aoptest.aop.BaseResponse;
import com.aop.aoptest.aop.StatusCode;
import com.aop.aoptest.util.QRCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("qr/code")
public class QrCodeController  {

    private static final String RootPath="D:\\images\\QRCode";
    private static final String FileFormat=".png";

    private static final ThreadLocal<SimpleDateFormat> LOCALDATEFORMAT=ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));

    //生成二维码并将其存放于本地目录
    @PostMapping("generate/v1")
    public BaseResponse generateV1(String content){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            final String fileName=LOCALDATEFORMAT.get().format(new Date());
            QRCodeUtil.createCodeToFile(content,new File(RootPath),fileName+FileFormat);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //生成二维码并将其返回给前端调用者
    @PostMapping("generate/v2")
    public BaseResponse generateV2(String content, HttpServletResponse servletResponse){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            QRCodeUtil.createCodeToOutputStream(content,servletResponse.getOutputStream());

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
