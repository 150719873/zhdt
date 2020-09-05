package com.dotop.pipe.server.rest.service.product;

import com.dotop.pipe.core.form.ProductForm;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.web.api.factory.product.IProductFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/product")
public class ProductController implements BaseController<ProductForm> {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    private IProductFactory iProductFactory;

    /**
     * 添加产品
     */
    @Override
    @PostMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody ProductForm productForm) {
        logger.info(LogMsg.to("productForm", productForm));

        String category = productForm.getCategory();
        String code = productForm.getCode();
        String name = productForm.getName();
        String address = productForm.getAddress();
        String des = productForm.getDes();

        // 验证
        VerificationUtils.string("category", category);
        VerificationUtils.string("code", code);
        VerificationUtils.string("name", name);
        VerificationUtils.string("des", des, true, 200);
        VerificationUtils.string("address", address, true, 200);
        ProductVo productVo = iProductFactory.add(productForm);

        return resp("productId", productVo.getProductId());
    }

    /**
     * 查询产品
     */
    @Override
    @GetMapping(value = "/{productId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(ProductForm productForm) {
        logger.info(LogMsg.to("productForm", productForm));
        String productId = productForm.getProductId();
        // 验证
        VerificationUtils.string("productId", productId);
        ProductVo productVo = iProductFactory.get(productForm);

        return resp(productVo);
    }

    /**
     * 产品分页
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody ProductForm productForm) {
        logger.info(LogMsg.to("productForm", productForm));
        Integer page = productForm.getPage();
        Integer pageSize = productForm.getPageSize();
        String category = productForm.getCategory();
        String code = productForm.getCode();
        String name = productForm.getName();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        VerificationUtils.string("category", category, true);
        VerificationUtils.string("code", code, true);
        VerificationUtils.string("name", name, true);

        Pagination<ProductVo> pagination = iProductFactory.page(productForm);

        return resp(pagination);
    }

    /**
     * 列出产品
     */
    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody ProductForm productForm) {
        logger.info(LogMsg.to("productForm", productForm));
        String category = productForm.getCategory();
        VerificationUtils.string("category", category, true);
        // 验证
        List<ProductVo> list = iProductFactory.list(productForm);
        return resp(list);
    }

    /**
     * 更新产品
     */
    @Override
    @PutMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody ProductForm productForm) {
        logger.info(LogMsg.to("productForm", productForm));

        String productId = productForm.getProductId();
        String category = productForm.getCategory();
        String code = productForm.getCode();
        String name = productForm.getName();
        String des = productForm.getDes();
        // 验证
        VerificationUtils.string("productId", productId);
        VerificationUtils.string("category", category, true);
        VerificationUtils.string("code", code, true);
        VerificationUtils.string("name", name, true);
        VerificationUtils.string("des", des, true, 200);
        iProductFactory.edit(productForm);

        return resp();
    }

    /**
     * 删除产品
     */
    @Override
    @DeleteMapping(value = "/{productId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(ProductForm productForm) {
        logger.info(LogMsg.to("productForm", productForm));
        String productId = productForm.getProductId();
        // 验证
        VerificationUtils.string("productId", productId);
        iProductFactory.del(productForm);

        return resp();
    }



}
