package com.tranzvision.gd.TZMenuMgBundle.dao;

import com.tranzvision.gd.TZMenuMgBundle.model.PsTreeNode;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTreeNodeKey;

public interface PsTreeNodeMapper {
    int deleteByPrimaryKey(PsTreeNodeKey key);

    int insert(PsTreeNode record);

    int insertSelective(PsTreeNode record);

    PsTreeNode selectByPrimaryKey(PsTreeNodeKey key);

    int updateByPrimaryKeySelective(PsTreeNode record);

    int updateByPrimaryKey(PsTreeNode record);
}