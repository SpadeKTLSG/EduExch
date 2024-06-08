package com.shop.serve.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.annotation.AutoFill;
import com.shop.common.enumeration.OperationType;
import com.shop.common.exception.SthHasCreatedException;
import com.shop.common.exception.SthNotFoundException;
import com.shop.pojo.dto.NoticeAllDTO;
import com.shop.pojo.entity.Notice;
import com.shop.serve.mapper.NoticeMapper;
import com.shop.serve.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.shop.common.constant.MessageConstants.OBJECT_HAS_ALIVE;
import static com.shop.common.constant.MessageConstants.OBJECT_NOT_ALIVE;


@Slf4j
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {


    @AutoFill(OperationType.INSERT)
    @Override
    public void publishNotice(NoticeAllDTO noticeAllDTO) {

        Notice notice2 = this.getOne(Wrappers.<Notice>lambdaQuery()
                .eq(Notice::getTitle, noticeAllDTO.getTitle()));
        if (notice2 != null) throw new SthHasCreatedException(OBJECT_HAS_ALIVE);

        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeAllDTO, notice);
        this.save(notice);
    }


    @AutoFill(OperationType.UPDATE)
    @Override
    public void updateNotice(NoticeAllDTO noticeAllDTO) {

        Notice notice = this.getOne(Wrappers.<Notice>lambdaQuery()
                .eq(Notice::getTitle, noticeAllDTO.getTitle()));
        if (notice == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        BeanUtils.copyProperties(noticeAllDTO, notice);
        this.updateById(notice);
    }


    @Override
    public void removeNotice(NoticeAllDTO noticeAllDTO) {

        Notice notice = this.getOne(Wrappers.<Notice>lambdaQuery()
                .eq(Notice::getTitle, noticeAllDTO.getTitle()));
        if (notice == null) throw new SthNotFoundException(OBJECT_NOT_ALIVE);

        this.removeById(notice.getId());
    }


}
