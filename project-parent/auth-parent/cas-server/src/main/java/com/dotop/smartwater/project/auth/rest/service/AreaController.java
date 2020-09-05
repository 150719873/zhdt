package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.project.auth.api.IAreaFactory;
import com.dotop.smartwater.project.auth.cache.CBaseDao;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.form.AreaForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaListVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**

 */
@RestController

@RequestMapping("/auth/area")
public class AreaController extends FoundationController implements BaseController<UserForm> {

	private static final Logger LOGGER = LogManager.getLogger(AreaController.class);

	@Autowired
	private IAreaFactory iAreaFactory;

	@Autowired
	private IDistributedLock iDistributedLock;

	@Resource
	private CBaseDao baseDao;

	@PostMapping(value = "/loadCompanyArea", produces = GlobalContext.PRODUCES)
	public String loadCompanyArea(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody AreaListVo areaVo) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		try {
			if (areaVo.getEnterpriseid() == null || "".equals(areaVo.getEnterpriseid())) {
				return resp(AuthResultCode.ParamIllegal, "水司ID不能为空", null);
			}

			String maxId = iAreaFactory.getMaxId();
			List<AreaVo> list = iAreaFactory.loadCompanyArea(areaVo.getEnterpriseid());
			List<AreaNodeVo> nodeTrees = listToListTree(list);
			return resp(AuthResultCode.Success, maxId, nodeTrees);
		} catch (Exception e) {
			LOGGER.error("loadCompanyArea", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}
	/**
	 * 将区域列表转换为树形式
	 * @param list
	 * @return
	 */
	public static List<AreaNodeVo> listToListTree(List<AreaVo> list) {
        List<AreaNodeVo> nodeTrees = new ArrayList<>(list.size());
        if (list == null || list.isEmpty()) {
            return nodeTrees;
        }
        for (int i=0;i < list.size();i++) {
            AreaVo item = list.get(i);
            String pid = item.getPId();
            if(pid == null || "0".equals(pid)) {
            	AreaNodeVo treeNode = new AreaNodeVo();
            	treeNode.setPId("0");
            	treeNode.setKey(item.getId());
            	treeNode.setTitle(item.getName()+"("+item.getCode()+")");
            	treeNode.setExpanded(true);
            	treeNode.setIsLeaf(true);
            	treeNode.setChildren(new ArrayList<AreaNodeVo>());
            	nodeTrees.add(treeNode);
            	list.remove(i);
            	i--;
            }            
        }
        return listToListTreeIteration(nodeTrees, list);
    }
	/**
	 * 迭代方法
	 * @param nodeTrees
	 * @param list
	 * @return
	 */
	public static List<AreaNodeVo> listToListTreeIteration(List<AreaNodeVo> nodeTrees, List<AreaVo> list){
		if (list == null || list.isEmpty()) {
            return nodeTrees;
        }
		if(nodeTrees == null || nodeTrees.isEmpty()) {
			return new ArrayList<AreaNodeVo>();
		}
        for (int i=0;i < list.size();i++) {
            AreaVo item = list.get(i);
            String pid = item.getPId();
            if(pid != null && pid != "0") {
            	for(int j=0;j < nodeTrees.size();j++) {
            		AreaNodeVo data = nodeTrees.get(j);
            		if(data.getKey() != null && pid.equals(data.getKey())) {
            			AreaNodeVo treeNode = new AreaNodeVo();
            			data.setIsLeaf(false);
            			treeNode.setPId(item.getPId());
            			treeNode.setKey(item.getId());
            			treeNode.setTitle(item.getName()+"("+item.getCode()+")");
            			treeNode.setExpanded(false);
            			treeNode.setIsLeaf(true);
            			treeNode.setChildren(new ArrayList<AreaNodeVo>());
            			data.getChildren().add(treeNode);
            			list.remove(i);
            			i--;
            			break;
            		}
            	}
            }
        }
        if(list != null && !list.isEmpty()) {
        	for(int j=0;j < nodeTrees.size();j++) {
    			nodeTrees.get(j).setChildren(listToListTreeIteration(nodeTrees.get(j).getChildren(), list));
    		}
        }
        return nodeTrees;
	}
	
	@PostMapping(value = "/addNode", produces = GlobalContext.PRODUCES)
	public String insertAreaNode(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody AreaForm areaForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		try {
			if(areaForm.getEnterpriseid() == null || "".equals(areaForm.getEnterpriseid())) {
				areaForm.setEnterpriseid(user.getEnterpriseid());
			}
			Integer num = iAreaFactory.insertAreaNode(areaForm);
			if(num == 1) {
				auditLog(OperateTypeEnum.AREA_MANAGEMENT,"新增","区域名称",areaForm.getName());
				return resp(AuthResultCode.Success, "Success", null);
			}else {
				return resp(AuthResultCode.Fail, "该区域编号已存在，不可使用", null);
			}
		} catch (Exception e) {
			LOGGER.error("saveCompanyArea", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}
	
	@PostMapping(value = "/updateNode", produces = GlobalContext.PRODUCES)
	public String updateAreaNode(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody AreaForm areaForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		try {
			if(areaForm.getEnterpriseid() == null || "".equals(areaForm.getEnterpriseid())) {
				areaForm.setEnterpriseid(user.getEnterpriseid());
			}
			Integer num = iAreaFactory.updateAreaNode(areaForm);
			if(num == 1) {
				auditLog(OperateTypeEnum.AREA_MANAGEMENT,"编辑","区域ID",areaForm.getId());
				return resp(AuthResultCode.Success, "Success", null);
			}else {
				return resp(AuthResultCode.Fail, "该区域编号已存在，不可使用", null);
			}
		} catch (Exception e) {
			LOGGER.error("saveCompanyArea", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}
	
	@PostMapping(value = "/deleteNode", produces = GlobalContext.PRODUCES)
	public String deleteAreaNode(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody AreaForm areaForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		try {
			if(areaForm.getEnterpriseid() == null || "".equals(areaForm.getEnterpriseid())) {
				areaForm.setEnterpriseid(user.getEnterpriseid());
			}
			Integer num = iAreaFactory.deleteAreaNode(areaForm);
			if(num == 1) {
				auditLog(OperateTypeEnum.AREA_MANAGEMENT,"删除","区域ID",areaForm.getId());
				return resp(AuthResultCode.Success, "Success", null);
			}else if(num == -1){
				return resp(AuthResultCode.Fail, "该区域正在使用中，不可删除", null);
			}else if(num == -2){
				return resp(AuthResultCode.Fail, "该区域下存在报表，不可删除", null);
			}else {
				return resp(AuthResultCode.Fail, "该区域节点下存在子节点，不可删除", null);
			}
		} catch (Exception e) {
			LOGGER.error("saveCompanyArea", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}

	@PostMapping(value = "/saveCompanyArea", produces = GlobalContext.PRODUCES)
	public String saveCompanyArea(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody AreaListVo areaVo) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		if (areaVo.getEnterpriseid() == null) {
			return resp(AuthResultCode.ParamIllegal, "水司ID不能为空", null);
		}
		String key = "saveCompanyArea_" + areaVo.getEnterpriseid();
		boolean flag = iDistributedLock.lock(key, 2);
		try {
			if (flag) {
				iAreaFactory.saveCompanyArea(areaVo);
				auditLog(OperateTypeEnum.AREA_MANAGEMENT,"保存水司区域");
				return resp(AuthResultCode.Success, "Success", null);
			} else {
				return resp(AuthResultCode.Fail, "locking", null);
			}
		} catch (Exception e) {
			LOGGER.error("saveCompanyArea", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		} finally {
			if (flag) {
				iDistributedLock.releaseLock(key);
			}
		}

	}

	@PostMapping(value = "/isUsedNode", produces = GlobalContext.PRODUCES)
	public String isUsedNode(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody AreaVo area) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		try {
			if (area.getEnterpriseid() == null) {
				return resp(AuthResultCode.ParamIllegal, "水司ID不能为空", null);
			}
			if (area.getId() == null) {
				return resp(AuthResultCode.ParamIllegal, "ID不能为空", null);
			}
			return resp(AuthResultCode.Success, "Success", iAreaFactory.isUsedNode(area));
		} catch (Exception e) {
			LOGGER.error("isUsedNode", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}
}
