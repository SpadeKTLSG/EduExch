package com.shop.admin.controller;


import com.shop.pojo.Result;
import com.shop.pojo.dto.NoticeAllDTO;
import com.shop.serve.service.HotsearchService;
import com.shop.serve.service.NoticeService;
import com.shop.serve.service.RotationService;
import com.shop.serve.service.UpshowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理面板 4合一
 *
 * @author SK
 * @date 2024/06/07
 */
@Slf4j
@Tag(name = "Panel", description = "管理面板")
@RequestMapping("/admin/panel")
@RestController
public class PanelController {


    @Autowired
    private HotsearchService hotsearchService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private RotationService rotationService;
    @Autowired
    private UpshowService upshowService;

    //* -- hotsearch热搜 --

    //! ADD
    //! DELETE
    //! UPDATE
    //! QUERY

    //* -- notice公告 --

    //! ADD


    /**
     * 发布公告
     * <p>使用公共字段填充</p>
     */
    @PostMapping("/notice/save")
    @Operation(summary = "发布公告")
    @Parameters(@Parameter(name = "noticeAllDTO", description = "公告发布DTO", required = true))
    public Result publishNotice(@RequestBody NoticeAllDTO noticeAllDTO) {
        noticeService.publishNotice(noticeAllDTO);
        return Result.success();
    }
    //http://localhost:8085/admin/panel/notice/save


    //! DELETE

    //TODO

    //! UPDATE

    @PutMapping("/notice/update")
    @Operation(summary = "更新公告")
    @Parameters(@Parameter(name = "noticeAllDTO", description = "公告更新DTO", required = true))
    public Result updateNotice(@RequestBody NoticeAllDTO noticeAllDTO) {
        noticeService.updateNotice(noticeAllDTO);
        return Result.success();
    }
    //http://localhost:8085/admin/panel/notice/update


    //! QUERY

    //TODO


    //* -- rotation轮播 --

    //! ADD
    //! DELETE
    //! UPDATE
    //! QUERY


    //* -- upshow提升 --

    //! ADD
    //! DELETE
    //! UPDATE
    //! QUERY


}
