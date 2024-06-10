package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.NoticeAllDTO;
import com.shop.pojo.entity.Notice;


public interface NoticeService extends IService<Notice> {

    /**
     * 发布通知
     */
    void publishNotice(NoticeAllDTO noticeAllDTO);

    /**
     * 更新通知
     */
    void updateNotice(NoticeAllDTO noticeAllDTO);

    /**
     * 删除通知
     */
    void removeNotice(NoticeAllDTO noticeAllDTO);
}
