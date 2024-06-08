package com.shop.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.constant.SystemConstants;
import com.shop.pojo.Result;
import com.shop.pojo.dto.NoticeAllDTO;
import com.shop.pojo.dto.RotationDTO;
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

    /**
     * 删除公告
     */
    @DeleteMapping("/notice/delete")
    @Operation(summary = "删除公告")
    @Parameters(@Parameter(name = "noticeAllDTO", description = "公告删除DTO", required = true))
    public Result removeNotice(@RequestBody NoticeAllDTO noticeAllDTO) {
        noticeService.removeNotice(noticeAllDTO);
        return Result.success();
    }
    //http://localhost:8085/admin/panel/notice/delete


    //! UPDATE

    /**
     * 更新公告
     */
    @PutMapping("/notice/update")
    @Operation(summary = "更新公告")
    @Parameters(@Parameter(name = "noticeAllDTO", description = "公告更新DTO", required = true))
    public Result updateNotice(@RequestBody NoticeAllDTO noticeAllDTO) {
        noticeService.updateNotice(noticeAllDTO);
        return Result.success();
    }
    //http://localhost:8085/admin/panel/notice/update


    //! QUERY

    /**
     * 分页查询公告
     */


    //* -- rotation轮播 --

    //! ADD

    /**
     * 添加轮播
     * <p></p>
     */
    @PostMapping("/rotation/add")
    @Operation(summary = "添加轮播")
    @Parameters(@Parameter(name = "rotationDTO", description = "轮播添加DTO", required = true))
    public Result add2Rotation(@RequestBody RotationDTO rotationDTO) {
        rotationService.add2Rotation(rotationDTO);
        return Result.success();
    }
    //http://localhost:8085/admin/panel/rotation/add


    //! DELETE

    /**
     * 删除轮播
     */
    @DeleteMapping("/rotation/delete")
    @Operation(summary = "删除轮播")
    @Parameters(@Parameter(name = "rotationDTO", description = "轮播删除DTO", required = true))
    public Result remove4Rotation(@RequestBody RotationDTO rotationDTO) {
        rotationService.remove4Rotation(rotationDTO);
        return Result.success();
    }
    //http://localhost:8085/admin/panel/rotation/delete


    //! UPDATE

    // 禁止


    //! QUERY

    /**
     * 分页查询轮播
     */
    @GetMapping("/rotation/query/page")
    public Result queryRotationPage(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.success(rotationService.page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE)));
    }
    //http://localhost:8085/admin/panel/rotation/query/page


    //* -- upshow提升 --

    //! ADD
    //! DELETE
    //! UPDATE
    //! QUERY


}
