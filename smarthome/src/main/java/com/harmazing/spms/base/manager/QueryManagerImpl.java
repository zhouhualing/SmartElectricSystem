package com.harmazing.spms.base.manager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import org.dozer.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.harmazing.spms.base.dao.QueryCustomDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dto.ColumnDTO;
import com.harmazing.spms.base.dto.DictDTO;
import com.harmazing.spms.base.dto.QueryTranDTO;
import com.harmazing.spms.base.entity.QueryCustomEntity;
import com.harmazing.spms.base.manager.QueryManager;
import com.harmazing.spms.base.util.CollectionUtil;
import com.harmazing.spms.base.util.ConstantUtil;
import com.harmazing.spms.base.util.DateUtil;
import com.harmazing.spms.base.util.DictUtil;
import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.PropertyUtil;
import com.harmazing.spms.base.util.QueryUtil;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.common.entity.IEntity;
import com.harmazing.spms.common.log.ILog;
import com.harmazing.spms.common.manager.IManager;

/**
 * QueryManagerImpl.
 * @author Zhaocaipeng
 * since 2013-9-17
 */
@Service("queryManager")
@SuppressWarnings({"unchecked","rawtypes"})
public class QueryManagerImpl implements QueryManager, ILog {
	
	@Autowired
	private QueryDAO queryDAO;
	
	@Override
	public QueryTranDTO executeSearch(QueryTranDTO queryTranDTO) {
		//获取缓存中的query配置
		queryTranDTO.setQueryDTO(QueryUtil.getQueryDTOByQueryId(queryTranDTO.getQueryId()));
		
		//自定义方法
		if(doCustomManager(queryTranDTO)) {
			return queryTranDTO;
		}
		
		//获取对应实体的dao
		queryTranDTO.setSpringDataDAO((SpringDataDAO<IEntity>)getObjectDAO(queryTranDTO));
		
		//根据query配置等相关信息，拼接sql
		queryTranDTO.setPageable(getPageAble(queryTranDTO));
		
		//添加有andFiler和orFilter的search
		appendFilter(queryTranDTO);
	
		//设置搜索条件
		generateSearchFilter(queryTranDTO);
		
		//做查询获得结果集
		queryTranDTO.setPage(getDatas(queryTranDTO));
		
		//根据用户自定义数据控制显示列
		doCustomFilter(queryTranDTO);
		
		//数据增强
		enhanceData(queryTranDTO);
		
		//设置最大数量
		queryTranDTO.setAllCount(Long.valueOf(queryTranDTO.getPage().getTotalElements()));
		
		//数据自定义后续处理
		String decorationStr = queryTranDTO.getQueryDTO().getCustomerDecoration();
		if(decorationStr != null) {
			List <String> decorationList = Arrays.asList(decorationStr.split("\\."));
			IManager iManager = SpringUtil.getBeanByName(decorationList.get(0));
			Method method = ReflectionUtils.getMethod(iManager, decorationList.get(1));
			ReflectionUtils.invoke(method, iManager, new Object[]{queryTranDTO});
		}
		
		return queryTranDTO;
	}
	
	@Transactional
	public QueryTranDTO doDeleteObjectById(QueryTranDTO queryTranDTO) {
		//获取缓存中的query配置
		queryTranDTO.setQueryDTO(QueryUtil.getQueryDTOByQueryId(queryTranDTO.getQueryId()));
		
		//获取对应实体的dao
		SpringDataDAO<IEntity> springDataDAO = (SpringDataDAO<IEntity>)getObjectDAO(queryTranDTO);
		
		springDataDAO.doDeleteById(queryTranDTO.getEntityId());
		
		return queryTranDTO;
	}
	
	protected <T> T getObjectDAO(QueryTranDTO queryTranDTO) {
		String entityName = StringUtil.getLastSpecifiedString(queryTranDTO.getQueryDTO().getBaseClass(), ".", StringUtil.AFTER);
		String daoBeanName = StringUtils.uncapitalize(StringUtil.getLastSpecifiedString(entityName, ConstantUtil.ENTITUPOSTFIX, StringUtil.BEFORE)) + ConstantUtil.DAOPOSTFIX;
		return (T)SpringUtil.getBeanByName(daoBeanName);
	}
	
	protected Pageable getPageAble(QueryTranDTO queryTranDTO) {
		List <String> orderLists = new ArrayList<String>();
		String orderByQuery = queryTranDTO.getQueryDTO().getOrderBy();
		Sort sort = null;
		if(orderByQuery != null) {
			List <Order> orderList = new ArrayList<Order>();
			String [] items = orderByQuery.split("##");
			for(String item : items) {
				String [] orderArr = item.split("#");
				Order order = null;
				if("desc".equals(orderArr[1])) {
					order = new Order(Direction.DESC,orderArr[0]);
				} else {
					order = new Order(Direction.ASC,orderArr[0]);
				}
				orderList.add(order);
			}
			sort = new Sort(orderList);
		} else {
			orderLists.add("id");
			sort = new Sort(Direction.DESC, orderLists);
		}
		//判断如果为-1 的话，则做特殊处理。 -1为最后一页【前台根据allCount确定最后一页不实时】
		if(queryTranDTO.getNowPage() == -1) {
			if(queryTranDTO.getAllCount()%queryTranDTO.getPageMaxCount()==0) {
				queryTranDTO.setNowPage((int)Math.floor(queryTranDTO.getAllCount()/queryTranDTO.getPageMaxCount()) -1);
			} else {
				queryTranDTO.setNowPage((int)Math.floor(queryTranDTO.getAllCount()/queryTranDTO.getPageMaxCount()));
			}
		} 
		return new PageRequest(queryTranDTO.getNowPage(), queryTranDTO.getPageMaxCount(), sort);
	}

	protected  Page<IEntity>  getDatas(QueryTranDTO queryTranDTO) {
		return queryTranDTO.getSpringDataDAO().findAll(DynamicSpecifications.bySearchFilter(queryTranDTO.getSearchFilters(), IEntity.class),queryTranDTO.getPageable());
	}
	
	public void generateSearchFilter(QueryTranDTO queryTranDTO) {
		Map columnCacheMap = CollectionUtil.extractToMap(queryTranDTO.getQueryDTO().getColumn(), "key");
		Iterator <SearchFilter> searchFilterIterator = queryTranDTO.getSearchFilters().iterator();
		List <SearchFilter> appendFilters = new ArrayList<SearchFilter>();
		while(searchFilterIterator.hasNext()) {
			SearchFilter tempSearchFilter = searchFilterIterator.next();
			if(columnCacheMap.containsKey(tempSearchFilter.getFieldName())) {
				ColumnDTO columnDTO = (ColumnDTO)columnCacheMap.get(tempSearchFilter.getFieldName());
				if(StringUtils.isEmpty(tempSearchFilter.getValue())&&(columnDTO.getAndFilter()==null)&&(columnDTO.getOrFilter()==null)) {
					searchFilterIterator.remove();
					continue;
				}
				if("true".equals(columnDTO.getAllowSearch())) {
					if(StringUtils.isEmpty(tempSearchFilter.getValue())) {
						searchFilterIterator.remove();
						if(columnDTO.getAndFilter() != null) {

							String andStrs = columnDTO.getAndFilter();
							String [] andStrArr = andStrs.split("##");
							for(String andStr : andStrArr) { 
								String [] andArr = andStr.split("#");
								if(SearchFilter.Operator.IN.equals(SearchFilter.Operator.valueOf(andArr[1])) || SearchFilter.Operator.NOTIN.equals(SearchFilter.Operator.valueOf(andArr[1])) || SearchFilter.Operator.BETWEEN.equals(SearchFilter.Operator.valueOf(andArr[1]))) {
									List <Object> values = new ArrayList<Object>();
									String [] valueArr = andArr[2].toString().split(",");
									for(int i=0; i <valueArr.length; i++) {
//										if("java.lang.Long".equals(columnDTO.getType())) {
//											values.add(Long.valueOf(valueArr[i]));
//										} else {
											values.add(valueArr[i]);
//										}
									}
									SearchFilter searchFilter = new SearchFilter(andArr[0],SearchFilter.Operator.valueOf(andArr[1]),values);
									appendFilters.add(searchFilter);
								}else {
									SearchFilter searchFilter = new SearchFilter(andArr[0],SearchFilter.Operator.valueOf(andArr[1]),andArr[2]);
									appendFilters.add(searchFilter);
								}

							}
						}	
						
						//TODO
						if(columnDTO.getOrFilter() != null) {
							String orStrs = columnDTO.getOrFilter();
							String [] orStrArr = orStrs.split("##");
							for(String orStr : orStrArr) {
								String [] orArr = orStr.split("#");
								SearchFilter searchFilter = new SearchFilter(orArr[0],SearchFilter.Operator.valueOf(orArr[1]),orArr[2]);
								searchFilter.setIsAnd(false);
								appendFilters.add(searchFilter);
							}
						}						
					} else {
						if(SearchFilter.Operator.IN.equals(columnDTO.getOperator()) || SearchFilter.Operator.NOTIN.equals(columnDTO.getOperator()) || SearchFilter.Operator.BETWEEN.equals(columnDTO.getOperator())) {
							List <Object> values = new ArrayList<Object>();
							String [] valueArr = tempSearchFilter.getValue().toString().split(",");
							if("java.util.Date".equals(columnDTO.getType())) {
							    if(",".equals(tempSearchFilter.getValue().toString())){
							    }else if(tempSearchFilter.getValue().toString().startsWith(",")) {
								valueArr[1] = "1977-01-01";
							    } else if(tempSearchFilter.getValue().toString().endsWith(",")){
								valueArr[1] = "2999-12-12";
							    }
							    
							}
							for(int i=0; i <valueArr.length; i++) {
								if("java.lang.Long".equals(columnDTO.getType())) {
									values.add(Long.valueOf(valueArr[i]));
								} else {
									values.add(valueArr[i]);
								}
							}
							tempSearchFilter.setValue(values);
						}
						tempSearchFilter.setOperator(columnDTO.getOperator());
						if("java.util.Date".equals(columnDTO.getType())) {
							try {
								String dateString = tempSearchFilter.getValue().toString();
								if(dateString.indexOf(",") != -1) {
								    	List <Date> dates = Lists.newArrayList();
								    	for(Object dateT : ((List)tempSearchFilter.getValue())) {
										String [] arr = dateT.toString().split("-");
										String pattern = "yyyy-MM-dd";
										if(columnDTO.getDateFormat() != null) {
											pattern = columnDTO.getDateFormat();
										}
										if(arr.length == 2) {
											dateString = dateString +"-01";
											pattern = "yyyy-MM-dd";
										}
										if(arr.length == 1) {
											dateString = dateString +"-01-01";
											pattern = "yyyy-MM-dd";
										}
										dates.add(DateUtil.parseStringToDate(dateT.toString(),pattern));
								    	}
								    	tempSearchFilter.setValue(dates);
								} else {
									String [] arr = dateString.split("-");
									String pattern = "yyyy-MM-dd";
									if(columnDTO.getDateFormat() != null) {
										pattern = columnDTO.getDateFormat();
									}
									if(arr.length == 2) {
										dateString = dateString +"-01";
										pattern = "yyyy-MM-dd";
									}
									if(arr.length == 1) {
										dateString = dateString +"-01-01";
										pattern = "yyyy-MM-dd";
									}
									tempSearchFilter.setValue(DateUtil.parseStringToDate(dateString,pattern));								    
								}
							} catch(Exception e) {
								searchFilterIterator.remove();
								logger.error("query parse search param type Date error");
							}
						}
						

						if(columnDTO.getAndFilter() != null) {

							String andStrs = columnDTO.getAndFilter();
							String [] andStrArr = andStrs.split("##");
							for(String andStr : andStrArr) { 
								String [] andArr = andStr.split("#");
								if(SearchFilter.Operator.IN.equals(SearchFilter.Operator.valueOf(andArr[1])) || SearchFilter.Operator.NOTIN.equals(SearchFilter.Operator.valueOf(andArr[1])) || SearchFilter.Operator.BETWEEN.equals(SearchFilter.Operator.valueOf(andArr[1]))) {
									List <Object> values = new ArrayList<Object>();
									String [] valueArr = andArr[2].toString().split(",");
									for(int i=0; i <valueArr.length; i++) {
//										if("java.lang.Long".equals(columnDTO.getType())) {
//											values.add(Long.valueOf(valueArr[i]));
//										} else {
											values.add(valueArr[i]);
//										}
									}
									SearchFilter searchFilter = new SearchFilter(andArr[0],SearchFilter.Operator.valueOf(andArr[1]),values);
									appendFilters.add(searchFilter);
								}else {
								    	SearchFilter searchFilter = new SearchFilter(andArr[0],SearchFilter.Operator.valueOf(andArr[1]),andArr[2]);
									tempSearchFilter.getAndSearchFilters().add(searchFilter);
								}
							}
						}	
						
						if(columnDTO.getOrFilter() != null) {
							String orStrs = columnDTO.getOrFilter();
							String [] orStrArr = orStrs.split("##");
							for(String orStr : orStrArr) {
								String [] orArr = orStr.split("#");
								SearchFilter searchFilter = new SearchFilter(orArr[0],SearchFilter.Operator.valueOf(orArr[1]),orArr[2]);
								tempSearchFilter.getOrSearchFilters().add(searchFilter);
							}
						}
												
					}
					
				} else {
					searchFilterIterator.remove();
				}
			} else {
				searchFilterIterator.remove();
			}	
		}
		
		 queryTranDTO.getSearchFilters().addAll(appendFilters);
	}
	
	public void doCustomFilter(QueryTranDTO queryTranDTO) {
//		QueryCustomDAO queryCustomDAO = (QueryCustomDAO)SpringUtil.getBeanByName("queryCustomDAO");
//		QueryCustomEntity queryCustomEntity =  queryCustomDAO.findByQueryIDANDUserCode(queryTranDTO.getQueryId(), UserUtil.getCurrentUserCode());
		List <ColumnDTO> resultColumnDTOs = new ArrayList<ColumnDTO>();
//		if((queryCustomEntity != null)&&!StringUtils.isEmpty(queryCustomEntity.getColumnNames())) {
//			Map columnCacheMap = CollectionUtil.extractToMap(queryTranDTO.getQueryDTO().getColumn(), "key");
//			List <String> columns = Arrays.asList(queryCustomEntity.getColumnNames().split(","));
//			for(String columnKey : columns) {
//				if(columnCacheMap.containsKey(columnKey)) {
//					resultColumnDTOs.add((ColumnDTO)columnCacheMap.get(columnKey));
//				}
//			}
//			
//		} else {
		    if(queryTranDTO.getQueryDTO() != null) {
			for(ColumnDTO columnDTO : queryTranDTO.getQueryDTO().getColumn()) {
				if("true".equals(columnDTO.getDisplay())){
					resultColumnDTOs.add(columnDTO);
				}
			}
		    }
//		}
		queryTranDTO.setShowColumnDTOs(resultColumnDTOs);
	}
	
	
	public void enhanceData(QueryTranDTO queryTranDTO) {
		Map <String,List<DictDTO>> dictMap = new HashMap<String, List<DictDTO>>();
		for(ColumnDTO columnDTO :queryTranDTO.getShowColumnDTOs()) {
			if(!StringUtils.isEmpty(columnDTO.getDict())) {
				dictMap.put(columnDTO.getDict(), DictUtil.getDictValues(columnDTO.getDict()));
			}
		}
		queryTranDTO.setDictMap(dictMap);
	}
	
	protected Boolean doCustomManager(QueryTranDTO queryTranDTO) {
		if(queryTranDTO.getQueryDTO() == null) {
			return false;
		}
		if(queryTranDTO.getQueryDTO().getQueryManager() == null) {
			return false;
		}
		
		IManager iManager = SpringUtil.getBeanByName(queryTranDTO.getQueryDTO().getQueryManager());
		try {
			Method method = iManager.getClass().getMethod(queryTranDTO.getQueryDTO().getQueryMethod(), QueryTranDTO.class);
			method.invoke(iManager, queryTranDTO);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	
	
	public QueryTranDTO customerQuery(QueryTranDTO queryTranDTO) throws IOException {
		
		List <Object> countArr = queryDAO.getObjects(queryTranDTO.getCountSql());
		if(countArr.size() == 0) {
		    queryTranDTO.setAllCount(0L);
		} else {
		    if(countArr.size() >1) {
			  queryTranDTO.setAllCount(Long.valueOf(countArr.size()));
		    } else {
			  queryTranDTO.setAllCount(Long.valueOf(countArr.get(0).toString()));
		    }
		  
		}
		
		
		if(queryTranDTO.getNowPage() == -1) {
			queryTranDTO.setNowPage((int)Math.floor(queryTranDTO.getAllCount()/queryTranDTO.getPageMaxCount()));
		} 	
		
		List <Map<String,Object>> objects = queryDAO.getMapObjects(createPageSql(queryTranDTO.getSourceSql(),queryTranDTO.getNowPage(), queryTranDTO.getPageMaxCount()));
		queryTranDTO.setCustomDatas(objects);
		
		doCustomFilter(queryTranDTO);
		enhanceData(queryTranDTO);
		return queryTranDTO;
	}
	
	/**
	 * 原生sql分页拼写
	 * @param sourcesql
	 * @param nowPage
	 * @param maxCount
	 * @return
	 * @throws IOException
	 */
	protected String createPageSql(String sourcesql,Integer nowPage, Integer maxCount) throws IOException {
		Long fromIndex = 0L;
		Integer toIndex = maxCount;
		if(nowPage==0) {
			fromIndex = 0L;
			toIndex = maxCount;
		}else {
			fromIndex = Long.valueOf(maxCount*nowPage);
			toIndex = maxCount*nowPage + maxCount;
		}
		
		switch (PropertyUtil.getPropertyInfo("hibernate.dialect")) {
		case "org.hibernate.dialect.MySQLDialect":{
			sourcesql = sourcesql +" limit "+ fromIndex+", "+maxCount;	
		} break;
		case "org.hibernate.dialect.Oracle10gDialect": {
			sourcesql = "select * from ("+
			          "select t.ID_, rownum rm from ("
			          +sourcesql+
			          ") t) where rm >"+fromIndex+" and rm<"+(toIndex+1);				
		} break;
		default:
			break;
		}
		
		return sourcesql;
	}
	
	public void appendFilter(QueryTranDTO queryTranDTO) {
		List <SearchFilter> searchFilters = queryTranDTO.getSearchFilters();
		List <SearchFilter> appendFilters = Lists.newArrayList();
		List <String> fileds = CollectionUtil.extractToList(searchFilters, "fieldName");
		if(fileds ==null) {
			return;
		}
		for(ColumnDTO columnDTO : queryTranDTO.getQueryDTO().getColumn()) {
			if(columnDTO.getAndFilter()!=null || columnDTO.getOrFilter() != null){
				if(!fileds.contains(columnDTO.getKey())) {
					appendFilters.add(new SearchFilter(columnDTO.getKey(),null));
				}
			}
		}
		queryTranDTO.getSearchFilters().addAll(appendFilters);
	}
}
