package com.dotop.pipe.server.rest.service.common;

import com.dotop.pipe.core.form.DictionaryForm;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.web.api.factory.common.ICommonFactory;
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
import java.util.Map;

/**
 *
 * @date 2019年1月16日
 */
@RestController()
@RequestMapping("/common")
public class CommonController implements BaseController<DictionaryForm> {

    private final static Logger logger = LogManager.getLogger(CommonController.class);

    /**
     * 区域管理接口
     */
    @Autowired
    private ICommonFactory iCommonFactory;

    public CommonController() {
        super();
    }

    /**
     * 加载区域树信息
     *
     * @param type
     * @return
     */
    @GetMapping(value = "/{type}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getByType(@PathVariable("type") String type) {
        logger.info(LogMsg.to("msg:", "加载数据字典开始"));
        DictionaryVo dictionary = iCommonFactory.getByType(type);
        logger.info(LogMsg.to("msg:", "加载数据字典结束"));
        logger.info(LogMsg.to("dictionary", dictionary));
        return resp(dictionary);
    }

    /**
     * 新增字典
     *
     * @param dictionaryForm
     * @return
     */
    @Override
    @PostMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody DictionaryForm dictionaryForm) {
        logger.info(LogMsg.to("dictionaryForm", dictionaryForm));

        String name = dictionaryForm.getName();
        String val = dictionaryForm.getVal();
        String des = dictionaryForm.getDes();
        String unit = dictionaryForm.getUnit();
        String type = dictionaryForm.getType();
        // 验证
        VerificationUtils.string("name", name);
        VerificationUtils.string("type", type);
        VerificationUtils.string("val", val);
        VerificationUtils.string("des", des, true, 100);
        VerificationUtils.string("unit", unit, true);
        DictionaryVo dictionaryVo = iCommonFactory.add(dictionaryForm);

        return resp("id", dictionaryVo.getId());
    }

    /**
     * 字典分页
     *
     * @param dictionaryForm
     * @return
     */
    @Override
    @GetMapping(value = "/page/{page}/{pageSize}/{type}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(DictionaryForm dictionaryForm) {
        logger.info(LogMsg.to("dictionaryForm", dictionaryForm));
        Integer page = dictionaryForm.getPage();
        Integer pageSize = dictionaryForm.getPageSize();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        VerificationUtils.string("type", dictionaryForm.getType());
        Pagination<DictionaryVo> pagination = iCommonFactory.page(dictionaryForm);

        return resp(pagination);
    }

    /**
     * 删除字典
     *
     * @param dictionaryForm
     * @return
     */
    @Override
    @DeleteMapping(value = "/{id}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(DictionaryForm dictionaryForm) {
        logger.info(LogMsg.to("dictionaryForm", dictionaryForm));
        String id = dictionaryForm.getId();
        // 验证
        VerificationUtils.string("id", id);
        iCommonFactory.del(dictionaryForm);
        return resp();
    }

    /**
     * 查找编号规则 type指设备类型 例如 pipe sensor valve plug等
     *
     * @return
     */
    @PostMapping(value = "/codeRule", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String codeRule(@RequestBody Map<String, Object> queryParams) {
        logger.info(LogMsg.to("dictionaryForm"));
        VerificationUtils.strList("type", (List<String>) queryParams.get("type"));
        Map<String, String> maxCode = iCommonFactory.getMaxCode(queryParams);
        System.out.println(queryParams);
        return resp(maxCode);
    }

}
