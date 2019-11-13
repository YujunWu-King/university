package com.tranzvision.gd.TZCostRelatedAEBundle.impl;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;

/**
 * @Auther: ZY
 * @Date: 2019/7/23 16:12
 * @Description: 项目分摊，奖学金，缴费计划，到账，开票等 AE导入类
 */
public class TzCostRelatedBaseEngine extends BaseEngine {
    @Override
    public void OnExecute() throws Exception {
        System.out.println("----------------------TzCostRelatedBaseEngine*BEGIN----------------------");
        GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
        TzCostRelatedMainImpl tzCostRelatedMain = (TzCostRelatedMainImpl) getSpringBeanUtil
                .getSpringBeanByID("com.tranzvision.gd.TZCostRelatedAEBundle.service.impl.TzCostRelatedMainImpl");
        try{
            tzCostRelatedMain.tzCostRelatedMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------TzCostRelatedBaseEngine*END----------------------");
    }
}
