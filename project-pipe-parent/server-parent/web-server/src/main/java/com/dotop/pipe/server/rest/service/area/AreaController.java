package com.dotop.pipe.server.rest.service.area;

import com.dotop.pipe.auth.web.api.factory.area.IAreaFactory;
import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.area.AreaTreeVo;
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
 * @date 2019年1月16日
 */
@RestController()
@RequestMapping("/area")
public class AreaController implements BaseController<AreaForm> {

    private final static Logger logger = LogManager.getLogger(AreaController.class);

    public AreaController() {
        super();
    }

    /**
     * 区域管理接口
     */
    @Autowired
    private IAreaFactory iAreaFactory;

    /**
     * 新增区域
     */
    @Override
    @PostMapping(value = "/addArea", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "新增区域开始", "areaForm", areaForm));
        // 校验
        VerificationUtils.string("parentCode", areaForm.getParentCode());
        VerificationUtils.string("name", areaForm.getName());
        AreaModelVo areaModelVo = iAreaFactory.add(areaForm);
        logger.info(LogMsg.to("msg:", "新增区域结束", "areaModelVo", areaModelVo));
        return resp(areaModelVo);
    }

    /**
     * 加载区域树信息
     */
    @Override
    @GetMapping(value = "/showTree/{areaId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "加载区域树开始", "areaForm", areaForm));
        // 校验
        AreaTreeVo areaTreeVO = iAreaFactory.showTreeDetails(areaForm.getAreaId());
        logger.info(LogMsg.to("msg:", "加载区域树结束"));
        logger.info(LogMsg.to("areaTreeVO", areaTreeVO));
        return resp(areaTreeVO);
    }

    /**
     * 区域停用
     */
    @Override
    @DeleteMapping(value = "/stopArea/{areaId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "区域停用开始", "areaForm", areaForm));
        // 校验
        VerificationUtils.string("areaId", areaForm.getAreaId());

        String flag = iAreaFactory.del(areaForm);
        logger.info(LogMsg.to("msg:", "区域停用结束", "flag", flag));
        return resp(flag);
    }

    /**
     * 区域分页 只查询根节点
     */
    @Override
    @GetMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "区域列表查询开始", "areaForm", areaForm));
        List<AreaModelVo> list = iAreaFactory.list(areaForm);
        logger.info(LogMsg.to("msg:", "区域列表查询结束"));
        return resp(list);
    }

    /**
     * 区域分页 查询所有节点
     *
     * @param areaForm
     * @return
     */
    @GetMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String listAll(AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "区域列表查询开始", "areaForm", areaForm));
        List<AreaModelVo> list = iAreaFactory.listAll(areaForm);
        logger.info(LogMsg.to("msg:", "区域列表查询结束"));
        return resp(list);
    }

    /**
     * 区域修改
     */
    @Override
    @PutMapping(value = "/upd", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "区域信息修改开始", "areaForm", areaForm));
        String areaId = areaForm.getAreaId();
        String name = areaForm.getName();
        VerificationUtils.string("areaId", areaId);
        VerificationUtils.string("name", name);
        VerificationUtils.string("areaColorNum", areaForm.getAreaColorNum());
        VerificationUtils.string("scale", areaForm.getScale());
        iAreaFactory.edit(areaForm);
        logger.info(LogMsg.to("msg:", "区域信息修改结束"));
        return resp();
    }

    /**
     * 修改区域节点位置
     */
    @PutMapping(value = "/updNode", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editNodeParent(@RequestBody AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "修改区域节点位置开始", "areaForm", areaForm));
        VerificationUtils.string("areaCode", areaForm.getAreaCode());
        VerificationUtils.string("parentcode", areaForm.getParentCode());
        AreaTreeVo areaTreeVO = iAreaFactory.editNodeParent(areaForm);
        logger.info(LogMsg.to("msg:", "修改区域节点位置结束"));
        return resp(areaTreeVO);
    }

    /**
     * 区域划线 新增
     */
    @PostMapping(value = "/addDrawArea", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String addDrawArea(@RequestBody AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "地图描边新增功能开始", "areaForm", areaForm));
        // 校验
        VerificationUtils.string("areaId", areaForm.getAreaId());
        AreaModelVo areaVo = iAreaFactory.addDrawArea(areaForm);
        logger.info(LogMsg.to("msg:", "地图描边新增功能结束", "areaId", areaForm.getAreaId()));
        return resp(areaVo);
    }

    /**
     * 区域划线 修改
     */
    @PostMapping(value = "/editDrawArea", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editDrawArea(@RequestBody AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "地图描边修改功能开始", "areaForm", areaForm));
        // 校验
        VerificationUtils.string("areaId", areaForm.getAreaId());
        // VerificationUtils.string("extent", areaForm.getExtent());
        iAreaFactory.editDrawArea(areaForm);
        logger.info(LogMsg.to("msg:", "地图描边修改功能结束", "areaId", areaForm.getAreaId()));
        return resp();
    }

    /**
     * 区域划线 修改
     */
    @GetMapping(value = "/drawAreaList/{limit}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String drawAreaList(AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", "地图描边列表查询功能开始", "areaForm", areaForm));
        // 校验
        VerificationUtils.integer("limit", areaForm.getLimit());
        List<AreaModelVo> list = iAreaFactory.drawAreaList(areaForm);
        logger.info(LogMsg.to("msg:", "地图描边列表查询功能结束"));
        return resp(list);
    }

    @Override
    @PostMapping(value = "/page/all", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody AreaForm areaForm) {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "areaForm", areaForm));
        Integer page = areaForm.getPage();
        Integer pageSize = areaForm.getPageSize();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        Pagination<AreaModelVo> pagination = iAreaFactory.page(areaForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(pagination);
    }

}
