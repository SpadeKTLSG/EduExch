package com.shop.admin.controller;


import com.shop.common.utils.AliOssUtil;
import com.shop.pojo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.shop.common.constant.MessageConstant.UPLOAD_FAILED;

/**
 * 通用Controller
 *
 * @author SK
 * @date 2024/06/03
 */
@Slf4j
@Tag(name = "Common", description = "通用")
@RequestMapping("/admin/common")
@RestController
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;


    /**
     * 文件上传
     * <p>上传在前端弹窗完成, 之后需要将路径拼接到属性字段中用, 划分</p>
     *
     * @param file 文件
     * @return 文件访问路径
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public Result upload(MultipartFile file) { //MultiPartFile是SpringMVC封装的文件上传对象
        log.debug("文件上传：{}", file);

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   .png ...
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            //构造新文件名称
            String objectName = UUID.randomUUID() + extension; //UUID.randomUUID()生成一个随机的字符串

            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);

        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage());
        }

        return Result.error(UPLOAD_FAILED);
    }

}
