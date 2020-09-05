package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.CompriseBo;
import com.dotop.smartwater.project.module.core.water.bo.LadderBo;
import com.dotop.smartwater.project.module.core.water.bo.LadderPriceBo;
import com.dotop.smartwater.project.module.core.water.bo.PayTypeBo;
import com.dotop.smartwater.project.module.core.water.dto.CompriseDto;
import com.dotop.smartwater.project.module.core.water.dto.LadderDto;
import com.dotop.smartwater.project.module.core.water.dto.LadderPriceDto;
import com.dotop.smartwater.project.module.core.water.dto.PayTypeDto;
import com.dotop.smartwater.project.module.core.water.vo.CompriseVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceModelVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderPriceVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceDao;
import com.dotop.smartwater.project.module.dao.revenue.ICompriseDao;
import com.dotop.smartwater.project.module.dao.revenue.ILadderDao;
import com.dotop.smartwater.project.module.dao.revenue.ILadderPriceDao;
import com.dotop.smartwater.project.module.dao.revenue.IPayTypeDao;
import com.dotop.smartwater.project.module.dao.revenue.IPurposeDao;
import com.dotop.smartwater.project.module.service.revenue.IPayTypeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 */
@Service
public class PayTypeServiceImpl implements IPayTypeService {

	private static final Logger LOGGER = LogManager.getLogger(PayTypeServiceImpl.class);

	@Autowired
	private IPayTypeDao iPayTypeDao;

	@Autowired
	private ILadderDao iLadderDao;

	@Autowired
	private ILadderPriceDao iLadderPriceDao;

	@Autowired
	private ICompriseDao iCompriseDao;

	@Autowired
	private IPurposeDao iPurposeDao;

	@Autowired
	private IDeviceDao iDeviceDao;

	@Override
	public LadderVo getMaxLadder(LadderBo ladderBo) {
		try {
			// 参数转换
			LadderDto ladderDto = new LadderDto();
			ladderDto.setTypeid(ladderBo.getTypeid());
			return iLadderDao.getMaxLadder(ladderDto);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PayTypeVo> noPagingFind(PayTypeBo payTypeBo) {
		try {
			PayTypeDto payTypeDto = new PayTypeDto();
			payTypeDto.setEnterpriseid(payTypeBo.getEnterpriseid());
			payTypeDto.setTaskSwitch(payTypeBo.getTaskSwitch());
			return iPayTypeDao.getList(payTypeDto);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<PayTypeVo> find(PayTypeBo payTypeBo) {
		try {
			PayTypeDto payTypeDto = new PayTypeDto();
			payTypeDto.setEnterpriseid(payTypeBo.getEnterpriseid());

			if (StringUtils.isNotBlank(payTypeBo.getName())) {
				payTypeDto.setName("%" + payTypeBo.getName() + "%");
			}

			Page<Object> pageHelper = PageHelper.startPage(payTypeBo.getPage(), payTypeBo.getPageCount());

			List<PayTypeVo> list = iPayTypeDao.getList(payTypeDto);
			// 拼接数据返回
			return new Pagination<>(payTypeBo.getPage(), payTypeBo.getPageCount(), list, pageHelper.getTotal());

		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void updatePayType(PayTypeBo payTypeBo) {
		try {
			PayTypeDto payTypeDto = new PayTypeDto();
			BeanUtils.copyProperties(payTypeBo, payTypeDto);
			iPayTypeDao.update(payTypeDto);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PayTypeVo addPayType(PayTypeBo payTypeBo) {
		try {
			PayTypeDto payTypeDto = new PayTypeDto();
			BeanUtils.copyProperties(payTypeBo, payTypeDto);

			payTypeDto.setTypeid(UuidUtils.getUuid());
			iPayTypeDao.insert(payTypeDto);

			PayTypeVo payTypeVo = new PayTypeVo();
			BeanUtils.copyProperties(payTypeDto, payTypeVo);
			return payTypeVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public PayTypeVo getPayType(PayTypeBo payTypeBo) {
		try {
			PayTypeDto payTypeDto = new PayTypeDto();
			BeanUtils.copyProperties(payTypeBo, payTypeDto);

			PayTypeVo vo = iPayTypeDao.getPayType(payTypeDto);
			if (vo != null) {
				List<CompriseVo> comprises = iPayTypeDao.getComprises(payTypeDto);
				vo.setComprises(comprises);
				
				List<LadderVo> ladders = iPayTypeDao.getLadders(payTypeDto);
				for (LadderVo ladder : ladders) {
					LadderPriceDto priceDto = new LadderPriceDto();
					priceDto.setLadderid(ladder.getId());
					List<LadderPriceVo> ladderPrices = iPayTypeDao.getLadderPrices(priceDto);
					ladder.setLadderPrices(ladderPrices);
				}
				vo.setLadders(ladders);
			}
			return vo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public boolean checkNameIsExist(PayTypeBo payTypeBo) {
		try {
			PayTypeDto payTypeDto = new PayTypeDto();
			BeanUtils.copyProperties(payTypeBo, payTypeDto);
			return iPayTypeDao.checkNameIsExist(payTypeDto) > 0 ? true : false;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public void editPayType(PayTypeBo payTypeBo) {
		try {
			PayTypeDto payTypeDto = new PayTypeDto();
			BeanUtils.copyProperties(payTypeBo, payTypeDto);
			iPayTypeDao.update(payTypeDto);

			if (payTypeDto.getComprises() != null) {
				//先删除 后保存
				iPayTypeDao.delComprises(payTypeDto);
				List<CompriseDto> comprises = new ArrayList<CompriseDto>();
				for (CompriseBo compriseBo : payTypeBo.getComprises()) {
					CompriseDto compriseDto = new CompriseDto();
					BeanUtils.copyProperties(compriseBo, compriseDto);
					comprises.add(compriseDto);
				}
				iPayTypeDao.batchComprises(comprises);
			}
			
			if (payTypeDto.getLadders() != null) {
				//删除阶梯收费后保存
				iPayTypeDao.delLadders(payTypeDto);
				iPayTypeDao.delLadderPrices(payTypeDto);
				
				List<LadderDto> ladders = new ArrayList<LadderDto>();
				for (LadderBo ladderBo : payTypeBo.getLadders()) {
					LadderDto ladderDto = new LadderDto();
					BeanUtils.copyProperties(ladderBo, ladderDto);
					ladders.add(ladderDto);
					
					List<LadderPriceDto> prices = new ArrayList<LadderPriceDto>();
					for (LadderPriceBo price : ladderBo.getLadderPrices()) {
						LadderPriceDto ladderPriceDto = new LadderPriceDto();
						BeanUtils.copyProperties(price, ladderPriceDto);
						prices.add(ladderPriceDto);
					}
					iPayTypeDao.batchLadderPrices(prices);
				}
				iPayTypeDao.batchLadders(ladders);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public PayTypeVo savePayType(PayTypeBo payTypeBo) {
		try {
			PayTypeDto payTypeDto = new PayTypeDto();
			BeanUtils.copyProperties(payTypeBo, payTypeDto);

			iPayTypeDao.insert(payTypeDto);

			if (payTypeDto.getComprises() != null) {
				List<CompriseDto> comprises = new ArrayList<CompriseDto>();
				for (CompriseBo compriseBo : payTypeBo.getComprises()) {
					CompriseDto compriseDto = new CompriseDto();
					BeanUtils.copyProperties(compriseBo, compriseDto);
					comprises.add(compriseDto);
				}
				iPayTypeDao.batchComprises(comprises);
			}
			
			if (payTypeDto.getLadders() != null) {
				List<LadderDto> ladders = new ArrayList<LadderDto>();
				for (LadderBo ladderBo : payTypeBo.getLadders()) {
					LadderDto ladderDto = new LadderDto();
					BeanUtils.copyProperties(ladderBo, ladderDto);
					ladders.add(ladderDto);
					
					List<LadderPriceDto> prices = new ArrayList<LadderPriceDto>();
					for (LadderPriceBo price : ladderBo.getLadderPrices()) {
						LadderPriceDto ladderPriceDto = new LadderPriceDto();
						BeanUtils.copyProperties(price, ladderPriceDto);
						prices.add(ladderPriceDto);
					}
					iPayTypeDao.batchLadderPrices(prices);
				}
				iPayTypeDao.batchLadders(ladders);
			}
			
			
			PayTypeVo payTypeVo = new PayTypeVo();
			BeanUtils.copyProperties(payTypeDto, payTypeVo);
			return payTypeVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	

	@Override
	public void updatePayTypeComprise(CompriseBo compriseBo) {
		try {
			CompriseDto compriseDto = new CompriseDto();
			BeanUtils.copyProperties(compriseBo, compriseDto);
			iCompriseDao.update(compriseDto);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public CompriseVo addPayTypeComprise(CompriseBo compriseBo) {
		try {
			CompriseDto compriseDto = new CompriseDto();
			BeanUtils.copyProperties(compriseBo, compriseDto);

			compriseDto.setId(UuidUtils.getUuid());
			iCompriseDao.insert(compriseDto);

			CompriseVo compriseVo = new CompriseVo();
			BeanUtils.copyProperties(compriseDto, compriseVo);

			return compriseVo;

		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void saveLadder(LadderBo ladderBo) {
		try {
			LadderDto ladderDto = new LadderDto();
			BeanUtils.copyProperties(ladderBo, ladderDto);

			ladderDto.setId(UuidUtils.getUuid());
			iLadderDao.insert(ladderDto);

			addLadderPrice(ladderBo, ladderDto);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void editLadder(LadderBo ladderBo) {
		try {
			LadderDto ladderDto = new LadderDto();
			BeanUtils.copyProperties(ladderBo, ladderDto);
			iLadderDao.update(ladderDto);
			iLadderPriceDao.deleteCompriseLadder(ladderDto);

			addLadderPrice(ladderBo, ladderDto);
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	private void addLadderPrice(LadderBo ladderBo, LadderDto ladderDto) {
		if (ladderBo.getComprises() != null && !ladderBo.getComprises().isEmpty()) {
			for (CompriseBo cp : ladderBo.getComprises()) {
				LadderPriceDto lp = new LadderPriceDto();
				lp.setId(UuidUtils.getUuid());
				lp.setTypeid(ladderDto.getTypeid());
				lp.setLadderid(ladderDto.getId());
				lp.setPrice(cp.getUnitprice());
				lp.setCreatetime(DateUtils.formatDatetime(new Date()));
				iLadderPriceDao.insert(lp);
			}
		}
	}

	@Override
	public List<CompriseVo> findComprise(CompriseBo compriseBo) {
		try {
			CompriseDto compriseDto = new CompriseDto();

			compriseDto.setTypeid(compriseBo.getTypeid());
			compriseDto.setEnable(compriseBo.getEnable());

			return iCompriseDao.findComprise(compriseDto);

		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<LadderVo> findLadders(LadderBo ladderBo) {
		try {
			List<LadderVo> list = iLadderDao.getLadders(ladderBo.getTypeid());
			if (list != null && !list.isEmpty()) {
				for (LadderVo ld : list) {
					List<LadderPriceVo> prices = iLadderPriceDao.findPrices(ld.getId());
					List<LadderPriceVo> data = new ArrayList<>();
					for (LadderPriceVo lp : prices) {
						if (ld.getId().equals(lp.getLadderid())) {
							data.add(lp);
						}
					}
					ld.setLadderPrices(data);
				}
			}
			return list;

		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int checkPayTypeIsUse(PayTypeBo payTypeBo) {
		try {
			return iPayTypeDao.checkPayTypeIsUse(payTypeBo.getTypeid());
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void delete(PayTypeBo payTypeBo) {
		try {

			/* 删除收费组成 */
			iCompriseDao.deleteComprises(payTypeBo.getTypeid());

			/* 删除收费阶梯 */
			iLadderDao.deleteLadders(payTypeBo.getTypeid());

			/* 删除收费阶梯费用组成 */
			iLadderPriceDao.deleteLadderPrice(payTypeBo.getTypeid());

			/* 删除收费种类信息 */
			iPayTypeDao.delete(payTypeBo.getTypeid());

		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void deleteComprise(CompriseBo compriseBo) {
		try {
			iCompriseDao.delete(compriseBo.getId());
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void deleteLadder(LadderBo ladderBo) {
		try {
			iLadderPriceDao.deletePrices(ladderBo.getId());
			iLadderDao.delete(ladderBo.getId());

		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PayTypeVo get(String typeid) {
		try {
			PayTypeVo payType = iPayTypeDao.findById(typeid);
			if (payType != null) {

				CompriseDto compriseDto = new CompriseDto();
				compriseDto.setTypeid(typeid);
				payType.setComprises((iCompriseDao.findComprise(compriseDto)));

				LadderBo ladderBo = new LadderBo();
				ladderBo.setTypeid(typeid);
				payType.setLadders(findLadders(ladderBo));

			}
			return payType;
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<LadderVo> getLadders(String typeId) {
		try {
			LadderBo ladderBo = new LadderBo();
			ladderBo.setTypeid(typeId);
			return findLadders(ladderBo);

		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, PayTypeVo> getPayTypeMap(String enterpriseid) {
		try {
			return iPayTypeDao.getPayTypeMap(enterpriseid);
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, DeviceModelVo> getDeviceModelMap(String enterpriseid) {
		try {
			return iDeviceDao.getDeviceModelMap(enterpriseid);
		}  catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, PurposeVo> getPurposeMap(String enterpriseid) {
		try {
			return iPurposeDao.getPurposeMap(enterpriseid);
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, AreaNodeVo> getAreasForSchedule(String eId) throws FrameworkRuntimeException {
		try {
			return iPayTypeDao.getAreaMapByEid(eId);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
