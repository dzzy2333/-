package com.zs.pms.dao;

import java.util.List;

import com.zs.pms.po.TDept;

public interface DepDao {
	//根据上级id取部门
public List<TDept> queryByPid(int pid);
}
