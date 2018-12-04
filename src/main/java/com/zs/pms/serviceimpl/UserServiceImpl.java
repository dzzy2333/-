package com.zs.pms.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zs.pms.dao.UserDao;
import com.zs.pms.exception.AppException;
import com.zs.pms.po.TUSer;
import com.zs.pms.service.UserService;
import com.zs.pms.utils.Constants;
import com.zs.pms.utils.MD5;
import com.zs.pms.vo.QueryUser;

@Service
@Transactional // 该业务支持事务
public class UserServiceImpl implements UserService {
	@Autowired // 注入
	private UserDao udao;

	@Override
	public TUSer chklogin(QueryUser query) throws AppException {
		// TODO Auto-generated method stub
		// 将明码变成密码
		MD5 md5 = new MD5();
		// 加密后的密码
		String p1 = md5.getMD5ofStr(query.getPassword());
		// 将密码放入query中
		query.setPassword(p1);
		List<TUSer> list = udao.queryByCon(query);
		// 没有用户
		if (list == null || list.size() != 1) {
			throw new AppException(1000, "用户名或者密码输入有误，请重新输入");
		}
		// 获得第一条数据
		TUSer user = list.get(0);
		// 关联权限列表返回
		return udao.queryById(user.getId());
	}

	@Override
	public List<TUSer> queryByCon(QueryUser query) {
		// TODO Auto-generated method stub
		return udao.queryByCon(query);
	}

	@Override
	@Transactional(rollbackFor = Exception.class) // 有异常就回滚否则提交
	public void update(TUSer user) throws AppException {
		// TODO Auto-generated method stub
		if (user.getPassword().length() <= 0) {
			throw new AppException(1004, "密码不能为空");
		}

		// TODO Auto-generated method stub
		// 如果密码被改了才重新加密 不等于32
		if (user.getPassword().length() != 32) {
			// 明码到密码的转化

			MD5 m = new MD5();
			user.setPassword(m.getMD5ofStr(user.getPassword()));

		}

		udao.update(user);

	}

	@Override
	@Transactional(rollbackFor = Exception.class) // 有异常就回滚否则提交
	public void deleteByIds(int[] ids) {
		// TODO Auto-generated method stub
		udao.deleteByIds(ids);
	}

	@Override
	@Transactional(rollbackFor = Exception.class) // 有异常就回滚否则提交
	public int insert(TUSer user) throws AppException {
		// TODO Auto-generated method stub
		if (user.getPassword() != null && (!"".equals(user.getPassword()))) {
			//密码加密
			MD5 md5 = new MD5();
			user.setPassword(md5.getMD5ofStr(user.getPassword()));
		}
		if("admin".equals(user.getLoginname())){
			throw new AppException(1002, "登录名不能为admin");
		}
		return udao.insert(user);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)//有异常就回滚否则提交
	public void delete(int id) throws AppException {
		// TODO Auto-generated method stub
		//
		udao.delete(id);
	}

	@Override
	/**
	 * query:条件
	 * page:当前页
	 * */
	public List<TUSer> queryByPage(QueryUser query, int page) {
		// TODO Auto-generated method stub
		//将当前页设置到条件中
		query.setPage(page);
		return udao.queryByPage(query);
	}

	@Override
	public TUSer queryById(int id) {
		// TODO Auto-generated method stub
		return udao.queryById(id);
	}

	@Override
	/**
	 * 计算总条数
	 * */
	public int queryPageCount(QueryUser query) {
		// TODO Auto-generated method stub
		//获取总条数
		int count=udao.queryConunt(query);
		//能整除
		if(count%Constants.PAGECOUNT==0){
			return count/Constants.PAGECOUNT;
		}
		//不能整除
		else{
		return count/Constants.PAGECOUNT+1;
				}
	}

}
