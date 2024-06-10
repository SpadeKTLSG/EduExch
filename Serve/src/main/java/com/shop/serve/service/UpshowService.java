package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.UpshowAllDTO;
import com.shop.pojo.entity.Upshow;


/**
 * UpshowService
 *
 * @author admin
 */
public interface UpshowService extends IService<Upshow> {

    /**
     * add2Upshow
     *
     * @param upshowAllDTO upshowAllDTO
     */
    void add2Upshow(UpshowAllDTO upshowAllDTO);

    /**
     * add2Upshow
     *
     * @param prodLocateDTO prodLocateDTO
     */
    void add2Upshow(ProdLocateDTO prodLocateDTO);

    /**
     * remove4Upshow
     *
     * @param upshowAllDTO upshowAllDTO
     */
    void remove4Upshow(UpshowAllDTO upshowAllDTO);

    /**
     * remove4Upshow
     *
     * @param prodLocateDTO prodLocateDTO
     */
    void remove4Upshow(ProdLocateDTO prodLocateDTO);


}
