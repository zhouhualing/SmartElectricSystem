package com.harmazing.ifttt;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.harmazing.service.impl.ServiceImpl;

public class IftttServiceImpl extends ServiceImpl implements IftttService  {

	//////////////////////////////////////////////////////
	@Override
	public List<IftttDevStrategyBindingEntigy> getAllDevStrategyBinding() {
		SqlSession session = sqlSessionFactory.openSession(false);
		try{
			IftttMapper mapper = session.getMapper(IftttMapper.class);
			return mapper.getAllDevStrategyBinding();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}

	//////////////////////////////////////////////////////
	@Override
	public List<IftttEntity> getAllEntities() {
		SqlSession session = sqlSessionFactory.openSession(false);
		try{
			IftttMapper mapper = session.getMapper(IftttMapper.class);
			return mapper.getAllEntities();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;
	}

}
