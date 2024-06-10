package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdGreatDTO;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.ProdFunc;
import com.shop.pojo.vo.ProdAllVO;
import com.shop.pojo.vo.ProdGreatVO;

import java.time.LocalDateTime;
import java.util.List;

public interface ProdService extends IService<Prod> {

    /**
     * 用户更新单件商品
     */
    void update4User(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException;

    /**
     * 用户更新单件商品[缓存]
     */
    void update4UserCache(ProdGreatDTO prodGreatDTO) throws InstantiationException, IllegalAccessException;

    /**
     * 管理员审核单件商品
     */
    void check(ProdLocateDTO prodLocateDTO);

    /**
     * 管理员冻结单件商品
     */
    void freeze(ProdLocateDTO prodLocateDTO);

    /**
     * 管理员分页查看需要审核商品
     */
    Page<ProdGreatVO> page2Check(Integer current);

    /**
     * 管理员删除一件商品
     */
    void deleteProd4Admin(ProdLocateDTO prodLocateDTO);

    /**
     * 管理员查看单件商品(简易)
     */
    Prod getProd4Admin(ProdLocateDTO prodLocateDTO);

    /**
     * 管理员分页查询所有商品列表
     */
    Page<ProdGreatVO> pageProd(Integer current);

    /**
     * 用户添加商品
     */
    void publishProd(ProdGreatDTO prodGreatDTO);

    /**
     * 用户删除商品
     */
    void deleteProd(String name);

    /**
     * 优惠券商品状态修改
     */
    void updateStatus(ProdLocateDTO prodLocateDTO, Integer func);

    /**
     * 用户查看单件商品详细
     */
    ProdGreatVO getSingle(ProdLocateDTO prodLocateDTO);

    /**
     * 用户查看单件商品详细[缓存]
     */
    ProdGreatVO getSingleCache(ProdLocateDTO prodLocateDTO);

    /**
     * 用户分页分类查商品列表
     */
    Page<Prod> getPageByCate(String cate, Integer current);

    /**
     * 用户分页查分类下所有商品列表
     */
    Page<Prod> pageCateAllProd(String cate, Integer current);

    /**
     *
     */
    List<ProdFunc> getOutdateOnes(LocalDateTime time);

    void cooldownUpshowProd(ProdFunc prodFunc);

    void cooldownRotationProd(ProdFunc prodFunc);

    List<ProdFunc> extractList4HotProd();

    void add2HotSearch(ProdFunc prodFunc);

    Page<ProdGreatVO> searchByName(String name, Integer current);

    Page<ProdAllVO> searchByNameSimple(String name, Integer current);
}
