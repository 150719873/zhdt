/**

 * @description : 业务公用方法
 * @date : 2017年12月25日 上午9:58:51
 */
package com.dotop.smartwater.project.module.core.water.utils;

import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.CompriseBo;
import com.dotop.smartwater.project.module.core.water.bo.LadderBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.LadderPriceDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.PricetypeSetBo;
import com.dotop.smartwater.project.module.core.water.vo.LadderPriceVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**

 */
public class BusinessUtil {

	/**
	 * 分段式计费（输入用水量最后得出结果）
	 * @param pricetypeSet
	 * @param realwater
	 * @return
	 */
	public static Double getRealPay(PricetypeSetBo pricetypeSet, Double realwater) {
		Double realpay = 0.0;
		Double Leijia = 0.0;

		// 超出梯度的水量计算
		Double finalwater = 0.0;
		Double finalprice = 0.0;

		if (pricetypeSet.getLimitone() != null) {
			if (realwater <= pricetypeSet.getLimitone()) {
				realpay = realwater * pricetypeSet.getPriceone();
				return realpay;
			} else {
				Leijia += pricetypeSet.getPriceone() * pricetypeSet.getLimitone();
			}
			finalwater = realwater - pricetypeSet.getLimitone();
			finalprice = pricetypeSet.getPriceone();
		}

		if (pricetypeSet.getLimittwo() != null) {
			if (realwater <= pricetypeSet.getLimittwo()) {
				realpay = (realwater - pricetypeSet.getLimitone()) * pricetypeSet.getPricetwo();
				realpay += Leijia;
				return realpay;
			} else {
				Leijia += (pricetypeSet.getLimittwo() - pricetypeSet.getLimitone()) * pricetypeSet.getPricetwo();
			}
			finalwater = realwater - pricetypeSet.getLimittwo();
			finalprice = pricetypeSet.getPricetwo();
		}

		if (pricetypeSet.getLimitthree() != null) {
			if (realwater <= pricetypeSet.getLimitthree()) {
				realpay = (realwater - pricetypeSet.getLimittwo()) * pricetypeSet.getPricethree();
				realpay += Leijia;
				return realpay;
			} else {
				Leijia += (pricetypeSet.getLimitthree() - pricetypeSet.getLimittwo()) * pricetypeSet.getPricethree();
			}
			finalwater = realwater - pricetypeSet.getLimitthree();
			finalprice = pricetypeSet.getPricethree();
		}

		if (pricetypeSet.getLimitfour() != null) {
			if (realwater <= pricetypeSet.getLimitfour()) {
				realpay = (realwater - pricetypeSet.getLimitthree()) * pricetypeSet.getPricefour();
				realpay += Leijia;
				return realpay;
			} else {
				Leijia += (pricetypeSet.getLimitfour() - pricetypeSet.getLimitthree()) * pricetypeSet.getPricefour();
			}
			finalwater = realwater - pricetypeSet.getLimitfour();
			finalprice = pricetypeSet.getPricefour();
		}

		if (pricetypeSet.getLimitfive() != null) {
			if (realwater <= pricetypeSet.getLimitfive()) {
				realpay = (realwater - pricetypeSet.getLimitfour()) * pricetypeSet.getPricefive();
				realpay += Leijia;
				return realpay;
			} else {
				Leijia += (pricetypeSet.getLimitfive() - pricetypeSet.getLimitfour()) * pricetypeSet.getPricefive();
			}
			finalwater = realwater - pricetypeSet.getLimitfive();
			finalprice = pricetypeSet.getPricefive();
		}

		if (pricetypeSet.getLimitsix() != null) {
			if (realwater <= pricetypeSet.getLimitsix()) {
				realpay = (realwater - pricetypeSet.getLimitfive()) * pricetypeSet.getPricesix();
				realpay += Leijia;
				return realpay;
			} else {
				Leijia += (realwater - pricetypeSet.getLimitfive()) * pricetypeSet.getPricesix();
			}
			finalwater = realwater - pricetypeSet.getLimitsix();
			finalprice = pricetypeSet.getPricesix();
		}

		realpay = Leijia;
		realpay += finalwater * finalprice;

		return realpay;
	}


	/**
	 * 新水务的分段式计费（输入用水量最后得出结果）
	 *
	 * @param ladders
	 * @param realwater
	 * @return
	 */
	public static Double getRealPayV2(List<LadderBo> ladders, Double realwater) {
		if (CollectionUtils.isEmpty(ladders) || realwater == null) {
			return 0.0;
		}


		Double realpay = 0.0;
		Double leijia = 0.0;

		// 超出梯度的水量计算
		Double finalwater = 0.0;
		Double finalprice = 0.0;
		Double lastMaxval = 0.0;

		for (LadderBo ladder : ladders) {
			if (ladder.getMaxval() != null && ladder.getPrice() != null) {
				if (ladder.getLadderno() == 1) {
					if (realwater <= ladder.getMaxval()) {
						realpay = CalUtil.mul(realwater, ladder.getPrice());
						return realpay;
					} else {
						leijia += CalUtil.mul(ladder.getPrice(), ladder.getMaxval());
					}
				} else {
					if (realwater <= ladder.getMaxval()) {
						realpay = CalUtil.sub(realwater, lastMaxval) * ladder.getPrice();
						realpay += leijia;
						return realpay;
					} else {
						leijia += CalUtil.sub(ladder.getMaxval(), lastMaxval) * ladder.getPrice();
					}
				}
				finalwater = CalUtil.sub(realwater, ladder.getMaxval());
				finalprice = ladder.getPrice();
				lastMaxval = ladder.getMaxval();
			}
		}

		realpay = leijia;
		realpay += CalUtil.mul(finalwater, finalprice);

		return realpay;
	}

	/**
	 * 新水务的计算水费组成
	 *
	 * @param ladders
	 * @param realwater
	 * @return
	 */
	public static List<LadderPriceDetailVo> getLadderPriceDetailVo(List<LadderVo> ladders, Double realwater) {
		if (CollectionUtils.isEmpty(ladders) || realwater == null) {
			return null;
		}


		Double leijia = 0.0;
		Map<String, Double> map = new HashMap<>();
		List<LadderPriceDetailBo> list = new ArrayList<>();

		// 超出梯度的水量计算
		Double finalwater = 0.0;
		LadderVo finalLadder = null;
		Double lastMaxval = 0.0;

		for (LadderVo ladder : ladders) {
			if (ladder.getMaxval() != null && ladder.getPrice() != null) {
				if (ladder.getLadderno() == 1) {
					if (realwater <= ladder.getMaxval()) {
						for (LadderPriceVo ladderPrice : ladder.getLadderPrices()) {
							LadderPriceDetailBo vo = new LadderPriceDetailBo();
							vo.setName(ladderPrice.getName());
							vo.setAmount(CalUtil.mul(realwater, ladderPrice.getPrice()));
							list.add(vo);
						}
						return BeanUtils.copy(list, LadderPriceDetailVo.class);
					} else {
						for (LadderPriceVo ladderPrice : ladder.getLadderPrices()) {
							map.put(ladderPrice.getName(), CalUtil.mul(ladderPrice.getPrice(), ladder.getMaxval()));
						}
					}
				} else {
					if (realwater <= ladder.getMaxval()) {
						for (LadderPriceVo ladderPrice : ladder.getLadderPrices()) {
							leijia = map.get(ladderPrice.getName());
							map.put(ladderPrice.getName(), CalUtil.add(leijia,
									CalUtil.mul(CalUtil.sub(realwater, lastMaxval), ladderPrice.getPrice())));
						}
						for (Map.Entry<String, Double> entry : map.entrySet()) {
							LadderPriceDetailBo vo = new LadderPriceDetailBo();
							vo.setName(entry.getKey());
							vo.setAmount(entry.getValue());
							list.add(vo);
						}
						return BeanUtils.copy(list, LadderPriceDetailVo.class);
					} else {
						for (LadderPriceVo ladderPrice : ladder.getLadderPrices()) {
							leijia = map.get(ladderPrice.getName());
							map.put(ladderPrice.getName(), CalUtil.add(leijia,
									CalUtil.mul(CalUtil.sub(ladder.getMaxval(), lastMaxval), ladderPrice.getPrice())));
						}
					}
				}
				finalwater = CalUtil.sub(realwater, ladder.getMaxval());
				finalLadder = ladder;
				lastMaxval = ladder.getMaxval();
			}
		}

		for (LadderPriceVo ladderPrice : finalLadder.getLadderPrices()) {
			leijia = map.get(ladderPrice.getName());
			map.put(ladderPrice.getName(), CalUtil.add(leijia, CalUtil.mul(finalwater, ladderPrice.getPrice())));
		}

		for (Map.Entry<String, Double> entry : map.entrySet()) {
			LadderPriceDetailBo vo = new LadderPriceDetailBo();
			vo.setName(entry.getKey());
			vo.setAmount(entry.getValue());
			list.add(vo);
		}
		return BeanUtils.copy(list, LadderPriceDetailVo.class);
	}

	// 新水务的计算滞纳金
    // 滞纳金已经不再这里计算了,在定时任务里面
	/**
	 * @param pd         各个费用明细总额
	 * @param comprises  组成费用明细
	 * @param createdate 账单生成日
	 * @param days       预期日数
	 * @return
	 */
	@Deprecated
	public static Double getPenalty(List<LadderPriceDetailBo> pd, List<CompriseBo> comprises, String createdate,
	                                int days) {
		/*if (pd == null || comprises == null) {
			return 0.0;
		}

		Map<String, Double> pdMap = pd.stream().collect(Collectors.toMap(x -> x.getName(), x -> x.getAmount()));
		Date date = new Date();
		double penalty = 0.0;
		double temp = 0.0;

		Date create = DateUtils.parseDatetime(createdate);

		for (CompriseBo c : comprises) {
			if (c.getStarttime() != null) {
				Date start = DateUtils.parseDatetime(c.getStarttime());
				// 1是否启用滞纳金 2是否在启用日期后3是否大于预期日数
				if (c.getEnable().equals(1) && DateUtils.daysBetween(start, date) > 0
						&& DateUtils.daysBetween(create, date) > days) {
					if (pdMap.get(c.getName()) != null) {
						temp = CalUtil.mul(CalUtil.div(Double.parseDouble(c.getRatio()), 100, 2),
								pdMap.get(c.getName()));
						penalty = CalUtil.add(penalty, temp);
						penalty = CalUtil.add(penalty, c.getPrice());
					}
				}
			}
		}

		BigDecimal bg = BigDecimal.valueOf(penalty).setScale(2, RoundingMode.UP);

		return bg.doubleValue();*/
		return 0.0;
	}

}
