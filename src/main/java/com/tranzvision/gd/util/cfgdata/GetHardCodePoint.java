/**
 * 
 */
package com.tranzvision.gd.util.cfgdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZHardCodeMgBundle.dao.PsTzHardcdPntMapper;
import com.tranzvision.gd.TZHardCodeMgBundle.model.PsTzHardcdPnt;

/**
 * 获取HardCode点的配置值
 * 
 * @author SHIHUA
 * @since 2015-11-03
 */
@Service
public class GetHardCodePoint {

	@Autowired
	private PsTzHardcdPntMapper psCmbcHardcdPntMapper;

	@Transactional
	private PsTzHardcdPnt getPsCmbcHardcdPnt(String hdp) {
		return	psCmbcHardcdPntMapper.selectByPrimaryKey(hdp);
	}

	public String getHardCodePointVal(String hdp) {
		PsTzHardcdPnt psCmbcHardcdPnt = this.getPsCmbcHardcdPnt(hdp);
		return psCmbcHardcdPnt.getTzHardcodeVal();
	}

	public String getHardCodePointDescr254(String hdp) {
		PsTzHardcdPnt psCmbcHardcdPnt = this.getPsCmbcHardcdPnt(hdp);
		return psCmbcHardcdPnt.getTzDescr254();
	}

	public String getHardCodePointDescr1000(String hdp) {
		PsTzHardcdPnt psCmbcHardcdPnt = this.getPsCmbcHardcdPnt(hdp);
		return psCmbcHardcdPnt.getTzDescr1000();
	}

}
