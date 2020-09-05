package com.dotop.pipe.api.dao.product;

import com.dotop.pipe.core.dto.product.ProductDto;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 *
 * @date 2018/10/25.
 */
public interface IProductDao extends BaseDao<ProductDto, ProductVo> {

    /**
     * 添加产品
     *
     * @param productDto
     * @throws DataAccessException
     */
    @Override
    void add(ProductDto productDto) throws DataAccessException;

    /**
     * 获取产品
     *
     * @param productDto
     * @return
     * @throws DataAccessException
     */
//	List<ProductVo> getByParam(ProductDto productDto) throws DataAccessException;

    /**
     * 获取产品列表
     *
     * @param productDto
     * @return
     * @throws DataAccessException
     */
    @Override
    List<ProductVo> list(ProductDto productDto) throws DataAccessException;

    /**
     * 编辑产品
     *
     * @param productDto
     * @return
     * @throws DataAccessException
     */
    @Override
    Integer edit(ProductDto productDto) throws DataAccessException;

    /**
     * 删除产品
     *
     * @param productDto
     * @return
     * @throws DataAccessException
     */
    @Override
    Integer del(ProductDto productDto) throws DataAccessException;

    @MapKey("code")
    Map<String, ProductVo> mapAll(@Param("isDel") Integer isDel) throws DataAccessException;

    @Override
    ProductVo get(ProductDto productDto) throws DataAccessException;

}
