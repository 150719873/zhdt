package com.dotop.pipe.auth.web.factory.area;

import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.auth.web.api.factory.area.IAreaFactory;
import com.dotop.pipe.api.service.area.IAreaService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.api.service.point.IPointMapService;
import com.dotop.pipe.api.service.point.IPointService;
import com.dotop.pipe.auth.core.constants.CasConstants;
import com.dotop.pipe.core.bo.area.AreaBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.utils.AreaUtils;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.area.AreaTreeVo;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 */
@Component
public class AreaFactoryImpl implements IAreaFactory {

    private static final Logger logger = LogManager.getLogger(AreaFactoryImpl.class);

    @Autowired
    private IAreaService iAreaService;

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IPointService iPointService;

    @Autowired
    private IPointMapService iPointMapService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AreaModelVo add(AreaForm areaForm) throws FrameworkRuntimeException {
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();

        // 区域编号 自动生成
        // 查询此父节点下的的最大值 并做+1 操作或者001 操作
        String maxAreaCode = iAreaService.selectMaxAreaCodeByParentCode(areaForm.getParentCode());
        // 新生成的areaCode
        String newAreaCode = "";
        if (StringUtils.isBlank(maxAreaCode)) {
            // 为空时表示 此节点下面没有子节点 直接拼接字符串 +001
            newAreaCode = areaForm.getParentCode() + PipeConstants.AREA_CODE_ADD;
            areaForm.setAreaCode(areaForm.getParentCode() + PipeConstants.AREA_CODE_ADD);
        } else {
            // 此节点下面有子节点 最大的areaCode + 1 操作
            newAreaCode = AreaUtils.createAreaModelKey(maxAreaCode);
            areaForm.setAreaCode(newAreaCode);
        }

        // 校验newAreaCode 是否唯一
        // AreaModelVo areaModelVo = iAreaService.getByAreaCode(newAreaCode, UUid);
        boolean flag = iAreaService.isExist(newAreaCode, operEid);

        if (flag) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_CODE_EXIST, "newAreaCode", newAreaCode));
            throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_CODE_EXIST);
        }
        // 新增区域
        AreaModelVo newAreaModelVo = iAreaService.add(areaForm, operEid, userBy, curr);
        // 更新根节点是1
        int updCount = iAreaService.updateIsParentByAreaCode(areaForm.getParentCode(), operEid, curr);
        if (updCount != 1) {
            logger.error(
                    LogMsg.to("ex", PipeExceptionConstants.AREA_UPDATE_ERROR, "msg:更新数据失败", areaForm.getParentCode()));
            throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_UPDATE_ERROR);
        }
        return newAreaModelVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void addAreaRoot(String name, String des, String operEid) throws FrameworkRuntimeException {
        // 1 查询根节点的最大值
        // 2 递增插入
        // LoginCas loginCas = iAuthCasApi.get();
        // String operEid = loginCas.getOperEid();
        // String userBy = loginCas.getUserName();
        String userBy = GlobalContext.SYSTEM_NAME;
        String parentCode = PipeConstants.AREA_PARENT_CODE;
        String maxAreaCode = iAreaService.selectMaxAreaCodeByParentCode(parentCode);
        // 递增后的更节点值
        int areaCodeAddOne = Integer.parseInt(maxAreaCode) + 1;
        String newAreaCode;
        if (areaCodeAddOne < 10) {
            // 拼接两个0
            newAreaCode = PipeConstants.AREA_CODE_ADD_DZERO + areaCodeAddOne;
        } else if (areaCodeAddOne < 100) {
            // 拼接一个0
            newAreaCode = PipeConstants.AREA_CODE_ADD_SZERO + areaCodeAddOne;
        } else {
            newAreaCode = areaCodeAddOne + "";
        }

        iAreaService.addAreaRoot(newAreaCode, name, operEid, userBy, des, new Date());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(AreaForm areaForm) throws FrameworkRuntimeException {
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        String areaId = areaForm.getAreaId();

        // TODO area与设备或工单为一对一关系，故将area_map关联关系删除，验证被使用方法由设备或工单提供
        // 判断是否有子区域 或者区域下有设备
        boolean flag = iAreaService.isExistChild(operEid, areaId);
        if (flag) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_USE, "msg区域正在被使用 areaId:", areaId));
            throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_USE);
        } else {
            DeviceBo deviceBo = new DeviceBo();
            deviceBo.setAreaId(areaId);
            deviceBo.setEnterpriseId(operEid);
            if (iDeviceService.isExist(deviceBo)) {
                logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_USE, "msg区域正在被使用 areaId:", areaId));
                throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_USE);
            }
        }

        // 即将被删除的节点的详细信息
        AreaModelVo areaModelVo = iAreaService.getNodeDetails(operEid, areaId);

        // 停用
        int updCount = iAreaService.del(areaId, userBy, curr, operEid);
        if (updCount != 1) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_UPDATE_ERROR, "msg:更新操作失败 areaId:", areaId));
            throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_UPDATE_ERROR);
        }

        // 判断父节点是否还有其他节点 否则 更新为0
        int count = iAreaService.selectCountByParentCode(areaModelVo.getParentCode(), operEid);
        if (count == 0) {
            // 更新这个节点isparent 字段
            int updCountParent = iAreaService.updateIsParentByAreaId(areaModelVo.getParentCode(), operEid, curr);
            if (updCountParent != 1) {
                logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_UPDATE_ERROR, "msg:更新操作失败areaId:", areaId));
                throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_UPDATE_ERROR);
            }
        }
        return null;
    }

    @Override
    public AreaTreeVo showTreeDetails(String nodeAreaId) throws FrameworkRuntimeException {
        // 待优化
        // if (PipeConstants.ENTERPRISEID.equals(enterpriseId)) { // 区域列表的公用组件
        LoginCas loginCas = iAuthCasApi.get();
        String enterpriseId = loginCas.getOperEid();
        // String enterpriseId = loginCas.getEnterpriseId();
        // }
        // 1 企业编号加载树的根节点
        // 2 根节点后递归加载
        AreaTreeVo areaTreeVo = new AreaTreeVo();
        // 查询所有的区域 封装成map
        Map<String, List<AreaModelVo>> areaMap = new HashMap<>();
        List<AreaModelVo> areaList = iAreaService.listAll(enterpriseId);

        // 封装map
        for (AreaModelVo areaModelVo : areaList) {
            if ("0".equals(areaModelVo.getParentCode())) {
                // TODO 改造后 传参的nodeId无实际作用
                nodeAreaId = areaModelVo.getAreaId();
            }

            if (areaMap.containsKey(areaModelVo.getParentCode())) {
                // 如果包含当前的key 值
                areaMap.get(areaModelVo.getParentCode()).add(areaModelVo);
            } else {
                List<AreaModelVo> areaListChild = new ArrayList<>();
                areaListChild.add(areaModelVo);
                areaMap.put(areaModelVo.getParentCode(), areaListChild);
            }
        }
        // 根节点只有一个 所有取第一个就好
        AreaModelVo areaModelVo = (AreaModelVo) areaMap.get(PipeConstants.AREA_PARENT_CODE).get(0);
        // 封装树对象信息
        areaTreeVo.setTitle(areaModelVo.getName());
        areaTreeVo.setKey(areaModelVo.getAreaId());
        // 默认展开第一层
        areaTreeVo.setExpanded(true);
        areaTreeVo.setAreaCode(areaModelVo.getAreaCode());
        areaTreeVo.setDes(areaModelVo.getDes());
        areaTreeVo.setAreaColorNum(areaModelVo.getAreaColorNum());
        areaTreeVo.setScale(areaModelVo.getScale());
        areaTreeVo.setIsParent(areaModelVo.getIsParent());
        areaTreeVo.setExtent(areaModelVo.getExtent());
        // 递归调用Map<String, List<AreaModelVo>>
        List<AreaTreeVo> childrenList = getChildrenList(areaModelVo.getAreaCode(), nodeAreaId, areaMap);
        // childrenList.remove(childrenList.size() - 1); // 去掉最后一个标志位
        areaTreeVo.setChildren(childrenList);
        return areaTreeVo;
    }

    /**
     * 递归调用
     *
     * @param areaCode
     * @param nodeAreaId
     * @param areaMap
     * @return
     * @throws FrameworkRuntimeException
     */
    public List<AreaTreeVo> getChildrenList(String areaCode, String nodeAreaId, Map<String, List<AreaModelVo>> areaMap)
            throws FrameworkRuntimeException {
        // Boolean flag = false; // 记录子节点 是否需要展开 true是要求展开 false是不展开
        // 再添加到list的最后一个里面传给父节点
        // setchildren时记得去掉最后一个节点
        // 封装newChildrenlist
        List<AreaTreeVo> newChildrenlist = new ArrayList<AreaTreeVo>();
        if (areaMap.containsKey(areaCode)) {
            List<AreaModelVo> areaList = areaMap.get(areaCode);
            // 循环列表 递归操作
            for (AreaModelVo areaModelVo : areaList) {
                AreaTreeVo areaTreeVo = new AreaTreeVo();
                if (areaModelVo.getAreaId().equals(nodeAreaId)) {
                    // 判断哪个节点需要选中
                    areaTreeVo.setSelected(true);
                    areaTreeVo.setExpanded(true);
                    // flag = true;
                }
                areaTreeVo.setAreaCode(areaModelVo.getAreaCode());
                areaTreeVo.setDes(areaModelVo.getDes());
                areaTreeVo.setAreaColorNum(areaModelVo.getAreaColorNum());
                areaTreeVo.setScale(areaModelVo.getScale());
                areaTreeVo.setTitle(areaModelVo.getName());
                areaTreeVo.setKey(areaModelVo.getAreaId());
                areaTreeVo.setIsParent(areaModelVo.getIsParent());
                areaTreeVo.setExtent(areaModelVo.getExtent());
                areaTreeVo.setExpanded(true);
                List<AreaTreeVo> childrenList = getChildrenList(areaModelVo.getAreaCode(), nodeAreaId, areaMap);

                /*
                 * 不需要判断是否展开 默认 全展开 if (childrenList.get(childrenList.size() - 1).getExpanded()
                 * == true) { // 最后一个是否展开 areaTreeVo.setExpanded(true); flag = true; // 如果有
                 * 则为true 只更新一次 } else { areaTreeVo.setExpanded(true); } // 去掉最后一个对象
                 * childrenList.remove(childrenList.size() - 1);
                 */
                // areaTreeVo.setChildren(getChildrenList(areaModelVo.getAreaCode()));
                areaTreeVo.setChildren(childrenList);
                newChildrenlist.add(areaTreeVo);
            }
        }
        /*
         * // 最后一个不计入 只是作为标记 AreaTreeVo areaTreeVo = new AreaTreeVo();
         * areaTreeVo.setExpanded(flag); newChildrenlist.add(areaTreeVo);
         */
        return newChildrenlist;
    }

    @Override
    public List<AreaModelVo> list(AreaForm areaForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        boolean flag = CasConstants.isAdmin(loginCas);
        if (flag) {
            // 水务平台人员
            operEid = null;
        }
        List<AreaModelVo> list = iAreaService.list(operEid);
        return list;
    }

    public AreaModelVo getNodeDetails(String areaId) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        AreaModelVo areaModelVo = iAreaService.getNodeDetails(operEid, areaId);
        return areaModelVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AreaModelVo edit(AreaForm areaForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String userBy = loginCas.getUserName();
        String enterpriseId = loginCas.getOperEid();
        AreaBo areaBo = BeanUtils.copyProperties(areaForm, AreaBo.class);
        areaBo.setUserBy(userBy);
        areaBo.setEnterpriseId(enterpriseId);
        areaBo.setCurr(new Date());
        int updCount = iAreaService.edit(areaBo);
        if (updCount != 1) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_UPDATE_ERROR, "msg:更新操作失败", "areaCode:",
                    areaForm.getAreaId()));
            throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_UPDATE_ERROR);
        }
        return null;
    }

    @Override
    public List<AreaModelVo> listAll(AreaForm areaForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String enterpriseId = loginCas.getOperEid();
        List<AreaModelVo> areaList = iAreaService.listAll(enterpriseId);
        return areaList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AreaTreeVo editNodeParent(AreaForm areaForm) throws FrameworkRuntimeException {
        /**
         * 1 查询所有的节点信息 2 需要修改的所有节点
         */
        Date curr = new Date();
        String code = areaForm.getAreaCode();
        String parentCode = areaForm.getParentCode();
        LoginCas loginCas = iAuthCasApi.get();
        String enterpriseId = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        List<AreaModelVo> updateList = new ArrayList<>();

        // 区域编号 自动生成
        // 查询此父节点下的的最大值 并做+1 操作或者001 操作
        String maxAreaCode = iAreaService.selectMaxAreaCodeByParentCode(areaForm.getParentCode());
        // 新生成的areaCode
        String newAreaCode = "";
        if (StringUtils.isBlank(maxAreaCode)) {
            // 为空时表示 此节点下面没有子节点 直接拼接字符串 +001
            newAreaCode = areaForm.getParentCode() + PipeConstants.AREA_CODE_ADD;
        } else {
            // 此节点下面有子节点 最大的areaCode + 1 操作
            newAreaCode = AreaUtils.createAreaModelKey(maxAreaCode);
        }
        boolean flag = iAreaService.isExist(newAreaCode, enterpriseId);

        String oldParentCode = null;
        if (flag) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_UPDATE_ERROR, "msg:区域编码已存在:", oldParentCode));
            throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_UPDATE_ERROR);

        } else {
            Map<String, List<AreaModelVo>> areaMap = new HashMap<>();
            List<AreaModelVo> areaList = iAreaService.listAll(enterpriseId);
            for (AreaModelVo areaModelVo : areaList) {
                if (areaModelVo.getAreaCode().startsWith(code)) {
                    if (areaModelVo.getAreaCode().equals(code)) {
                        oldParentCode = areaModelVo.getParentCode();
                        areaModelVo.setAreaCode(newAreaCode);
                        areaModelVo.setParentCode(parentCode);
                    } else {
                        areaModelVo.setAreaCode(areaModelVo.getAreaCode().replaceFirst(code, newAreaCode));
                        areaModelVo.setParentCode(areaModelVo.getParentCode().replaceFirst(code, newAreaCode));
                    }
                    updateList.add(areaModelVo);
                }
            }
        }
        int count = iAreaService.editNodeParent(updateList, enterpriseId, userBy, curr);
        // if (count == updateList.size()) {
        // 更新
        iAreaService.updateIsParentByAreaCode(parentCode, enterpriseId, curr);
        // 判断父节点是否还有其他节点 否则 更新为0
        int oldParentCodecount = iAreaService.selectCountByParentCode(oldParentCode, enterpriseId);
        if (oldParentCodecount == 0) {
            // 更新这个节点isparent 字段
            int updCountParent = iAreaService.updateIsParentByAreaId(oldParentCode, enterpriseId, curr);
            if (updCountParent != 1) {
                logger.error(
                        LogMsg.to("ex", PipeExceptionConstants.AREA_UPDATE_ERROR, "msg:更新操作失败areaId:", oldParentCode));
                throw new FrameworkRuntimeException(PipeExceptionConstants.AREA_UPDATE_ERROR);
            }
        }

        // } else {
        // logger.error(LogMsg.to("ex", PipeExceptionConstants.AREA_UPDATE_ERROR,
        // "msg:更新操作失败areaId:", oldParentCode));
        // throw new
        // FrameworkRuntimeException(PipeExceptionConstants.AREA_UPDATE_ERROR);
        // }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AreaModelVo addDrawArea(AreaForm areaForm) {
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        boolean flag = iPointMapService.isExistByDeviceId(operEid, null, areaForm.getAreaId());
        AreaBo areaBo = BeanUtils.copyProperties(areaForm, AreaBo.class);
        areaBo.setEnterpriseId(operEid);
        areaBo.setUserBy(userBy);
        areaBo.setCurr(curr);
        // 修改区域中心
        iAreaService.edit(areaBo);
        if (!flag) {
            // 不存在则新增
            // 添加坐标关系
            iPointMapService.addList(operEid, areaForm.getAreaId(), curr, userBy, areaForm.getPoints());
            iPointService.addList(operEid, curr, userBy, areaForm.getPoints());
        } else {
            // 存在则修改
            // 由于区域描边的点的集合比较多 不操作更新 先删除 再添加新的数据
            iPointMapService.delTables(operEid, areaForm.getAreaId(), curr, userBy);
            // 添加坐标关系
            iPointMapService.addList(operEid, areaForm.getAreaId(), curr, userBy, areaForm.getPoints());
            iPointService.addList(operEid, curr, userBy, areaForm.getPoints());
        }
        AreaModelVo areaVo = iAreaService.getNodeDetails(operEid, areaForm.getAreaId());
        return areaVo;
    }

    @Override
    public void editDrawArea(AreaForm areaForm) {
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        AreaBo areaBo = BeanUtils.copyProperties(areaForm, AreaBo.class);
        areaBo.setEnterpriseId(operEid);
        areaBo.setUserBy(userBy);
        areaBo.setCurr(curr);
        // 修改区域中心
        iAreaService.edit(areaBo);
        // 由于区域描边的点的集合比较多 不操作更新 先删除 再添加新的数据
        iPointMapService.delTables(operEid, areaForm.getAreaId(), curr, userBy);
        // 添加坐标关系
        iPointMapService.addList(operEid, areaForm.getAreaId(), curr, userBy, areaForm.getPoints());
        iPointService.addList(operEid, curr, userBy, areaForm.getPoints());
    }

    @Override
    public List<AreaModelVo> drawAreaList(AreaForm areaForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        AreaBo areaBo = BeanUtils.copyProperties(areaForm, AreaBo.class);
        areaBo.setEnterpriseId(operEid);
        List<AreaModelVo> list = iAreaService.drawAreaList(areaBo);
        // java8 按照code 排序
        for (AreaModelVo areaModelVo : list) {
            areaModelVo.getPoints().sort(Comparator.comparing(PointVo::sortBycode));
        }
        return list;
    }

    @Override
    public Pagination<AreaModelVo> page(AreaForm areaForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String enterpriseId = loginCas.getOperEid();
        List<AreaModelVo> areaList = iAreaService.listAll(enterpriseId);
        AreaBo areaBo = BeanUtils.copyProperties(areaForm, AreaBo.class);
        areaBo.setEnterpriseId(enterpriseId);
        return iAreaService.page(areaBo);
    }

}
