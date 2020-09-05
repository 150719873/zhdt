package com.dotop.pipe.web.factory.third;

import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.auth.api.service.enterprise.IEnterpriseService;
import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.form.ProductForm;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.web.api.factory.third.IThirdProductFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThirdProductFactoryImpl implements IThirdProductFactory {

    @Autowired
    private IEnterpriseService iEnterpriseService;

    @Autowired
    private IProductService iProductService;

    @Override
    public Pagination<ProductVo> page(ProductForm productForm) {
        String eid = productForm.getEnterpriseId();
        EnterpriseVo enterpriseVo = iEnterpriseService.get(eid);

        String enterpriseId = enterpriseVo.getEnterpriseId();
        Integer page = productForm.getPage();
        Integer pageSize = productForm.getPageSize();
        String name = productForm.getName();
        String code = productForm.getCode();

        ProductBo productBo = new ProductBo();
        productBo.setEnterpriseId(enterpriseId);
        productBo.setPage(page);
        productBo.setPageSize(pageSize);
        productBo.setName(name);
        productBo.setCode(code);
//        return iProductService.page(page, pageSize, null, null, null, name, code, enterpriseId);
        return iProductService.page(productBo);
    }

}
