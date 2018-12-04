package com.zs.pms.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zs.pms.dao.TarticleDao;
import com.zs.pms.exception.AppException;
import com.zs.pms.po.Tarticle;
import com.zs.pms.service.TarticleService;
import com.zs.pms.utils.Constants;
import com.zs.pms.vo.QueryArticle;
@Service
@Transactional // 该业务支持事务
public class TarticleServiceImpl implements TarticleService{
     @Autowired
     private TarticleDao adao;

	@Override
	public List<Tarticle> queryByCon(QueryArticle article) {
		// TODO Auto-generated method stub
		return adao.queryByCon(article);
	}

	@Override
	@Transactional(rollbackFor = Exception.class) // 有异常就回滚否则提交
	public void update(Tarticle article) throws AppException {
		// TODO Auto-generated method stub
		adao.update(article);
	}

	@Override
	public List<Tarticle> queryByPage(QueryArticle article, int page) {
		// TODO Auto-generated method stub
		article.setPage(page);
		return adao.queryByPage(article);
	}

	@Override
	public void deleteById(int[] ids) {
		// TODO Auto-generated method stub
		adao.deleteById(ids);
	}

	@Override
	public Tarticle queryById(int id) {
		// TODO Auto-generated method stub
		return adao.queryById(id);
	}

	@Override
	public int insert(Tarticle article) throws AppException {
		// TODO Auto-generated method stub
		return adao.insert(article);
	}

	@Override
	public void delete(int id) throws AppException {
		// TODO Auto-generated method stub
		adao.delete(id);
	}

	@Override
	public int queryPageCount(QueryArticle article) {
		// TODO Auto-generated method stub
		int count=adao.queryConunt(article);
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
