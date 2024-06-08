package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.NoticeAllDTO;
import com.shop.pojo.entity.Notice;


public interface NoticeService extends IService<Notice> {


    void publishNotice(NoticeAllDTO noticeAllDTO);

    void updateNotice(NoticeAllDTO noticeAllDTO);
}
