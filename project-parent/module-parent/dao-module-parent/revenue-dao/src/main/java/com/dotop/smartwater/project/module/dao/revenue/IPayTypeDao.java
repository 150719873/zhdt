package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.water.dto.CompriseDto;
import com.dotop.smartwater.project.module.core.water.dto.LadderDto;
import com.dotop.smartwater.project.module.core.water.dto.LadderPriceDto;
import com.dotop.smartwater.project.module.core.water.dto.PayTypeDto;
import com.dotop.smartwater.project.module.core.water.vo.CompriseVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderPriceVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IPayTypeDao extends BaseDao<PayTypeDto, PayTypeVo> {

	List<PayTypeVo> getList(PayTypeDto payTypeDto);

	void update(PayTypeDto payTypeDto);

	int delComprises(PayTypeDto payTypeDto);
	
	int delLadders(PayTypeDto payTypeDto);
	
	int delLadderPrices(PayTypeDto payTypeDto);
	
	void insert(PayTypeDto payTypeDto);
	
	List<CompriseVo> getComprises(PayTypeDto payTypeDto);
	
	List<LadderVo> getLadders(PayTypeDto payTypeDto);
	
	List<LadderPriceVo> getLadderPrices(LadderPriceDto dto);
	
	PayTypeVo getPayType(PayTypeDto payTypeDto);
	
	int checkNameIsExist(PayTypeDto payTypeDto);
	
	int batchComprises(@Param("list") List<CompriseDto> comprises);
	
	int batchLadders(@Param("list") List<LadderDto> ladders);

	int batchLadderPrices(@Param("list") List<LadderPriceDto> prices);
	
	int checkPayTypeIsUse(@Param("typeid") String typeid);

	void delete(@Param("typeid") String typeid);

	@MapKey("name")
	Map<String, PayTypeVo> getPayTypeMap(String enterpriseid);

	PayTypeVo findById(@Param("typeid") String typeid);

	@MapKey("key")
    Map<String,AreaNodeVo> getAreaMapByEid(String eId);
}
