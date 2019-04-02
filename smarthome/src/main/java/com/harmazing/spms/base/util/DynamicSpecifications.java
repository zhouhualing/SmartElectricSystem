package com.harmazing.spms.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * dynamic specification. 
 * @author Zhaocaipeng
 * since 2013-12-12
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class DynamicSpecifications {
	
	public static <T> Specification<T> bySearchFilter(final SearchFilter filter, final Class<T> entityClazz) {
		ArrayList<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
		searchFilters.add(filter);
		return bySearchFilter(searchFilters, entityClazz);
	}

	
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> entityClazz) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				if(!CollectionUtil.isBlank(filters)) {

					List<Predicate> predicatesAnd = Lists.newArrayList();
					List<Predicate> predicatesOr = Lists.newArrayList();
					Map <String, Join> joinCache = Maps.newHashMap();
					for(SearchFilter searchFilter : filters) {
						//main Search
						if(searchFilter.getIsAnd()) {
							predicatesAnd.add(handleSeachFilter(root,builder,searchFilter,joinCache));
						} else {
							predicatesOr.add(handleSeachFilter(root,builder,searchFilter,joinCache));
						}
						
					}
					if(predicatesOr.size() > 0) {
					    predicatesOr.add(builder.and(predicatesAnd.toArray(new Predicate[predicatesAnd.size()])));
					    return builder.or(predicatesOr.toArray(new Predicate[predicatesOr.size()]));
					} else {
						//and or
					    return builder.and(predicatesAnd.toArray(new Predicate[predicatesAnd.size()]));
					}

				}
					
				//final 组合 and
				return builder.conjunction();
			}
		};
		
	}
	
	public static Predicate handleSeachFilter(Root root, CriteriaBuilder builder, SearchFilter searchFilter, Map<String,Join> joinCache) {
		Predicate nowPredicate = null;
		List <Path> paths = Lists.newArrayList();
		paths.add(builderPath(root, searchFilter, joinCache));		
		nowPredicate = builderPredicate(builder, searchFilter, paths);
		
		if (searchFilter.getOrSearchFilters().size() > 0 || searchFilter.getAndSearchFilters().size() > 0) {
			List <Predicate> orPredicates = new ArrayList<Predicate>();
			List <Predicate> andPredicates = new ArrayList<Predicate>();
			for(SearchFilter tempSearchFilter:searchFilter.getOrSearchFilters()) {
				orPredicates.add(handleSeachFilter(root,builder,tempSearchFilter, joinCache));
			}
			for(SearchFilter tempSearchFilter:searchFilter.getAndSearchFilters()) {
				andPredicates.add(handleSeachFilter(root,builder,tempSearchFilter, joinCache));
			}
			
			Predicate orPredicate = null; 
			for(Predicate predicate : orPredicates) {
				if(orPredicate == null) {
					orPredicate = predicate;
				} else {
					orPredicate = builder.or(orPredicate, predicate);
				}
				
			}
			
			Predicate andPredicate = null; 
			for(Predicate predicate : andPredicates) {
				if(andPredicate == null) {
					andPredicate = predicate;
				} else {
					andPredicate = builder.and(andPredicate, predicate);
				}
				
			}
//			Predicate orPredicate = handleOrSearchFilter(root,builder,searchFilter, nowPredicate,joinCache);
//			Predicate andPredicate = handleAndSearchFilter(root,builder,searchFilter, nowPredicate,joinCache);		

			if((andPredicate!=null)&&(orPredicate != null)) {
				nowPredicate =	builder.or(builder.and(nowPredicate,andPredicate),orPredicate);
			} else if(andPredicate!=null) {
				nowPredicate =	builder.and(nowPredicate,andPredicate);
			} else if(orPredicate!=null) {
				nowPredicate =	builder.or(nowPredicate,orPredicate);
			} 
		} 

		
		return nowPredicate;
	}
	
	public static Path builderPath(Root root, SearchFilter searchFilter, Map<String, Join> joinCache) {
		Path path = null;
		StringBuffer stringBuffer  = new StringBuffer();
		String [] nowNamesArr = StringUtils.split(searchFilter.getFieldName(),".");
			for(int i=0; i <nowNamesArr.length; i++) {
				Join join = null;
				if(i==0) {
					path = root.get(nowNamesArr[0]);
				} else {
					/*修改一对多查询bug*/
					String beforeCacheKey = stringBuffer.toString();
					/*修改一对多查询bug*/
					
					stringBuffer.append(nowNamesArr[i-1]).append(".");
					if(joinCache.get(stringBuffer.toString()) !=null) {
						join = joinCache.get(stringBuffer.toString());
					} else {
						if((path.getJavaType() == java.util.Set.class) || (path.getJavaType() == java.util.List.class)) {
							if(join == null) {
								if(path.getJavaType() == java.util.Set.class) {
									join = root.joinSet(nowNamesArr[i-1],JoinType.LEFT);
								} else {
									join = root.joinList(nowNamesArr[i-1],JoinType.LEFT);
								}
								
							}
						} else {
							if(join == null) {
								/*修改一对多查询bug*/
//								join = root.join(nowNamesArr[i-1],JoinType.LEFT);
								if(i>=2) {
                                    join = joinCache.get(beforeCacheKey).join(nowNamesArr[i - 1], JoinType.LEFT);
                                } else {
                                    join = root.join(nowNamesArr[i - 1], JoinType.LEFT);
                                }
								/*修改一对多查询bug*/
							} 
						}
						joinCache.put(stringBuffer.toString(), join);
					}
					path = join.get(nowNamesArr[i]);
				}
			}
			return path;
	}
	
	public static Predicate builderPredicate(CriteriaBuilder builder, SearchFilter searchFilter, List<Path> paths) {
		Predicate nowPredicate = null;
		// logic operator
		switch (searchFilter.operator) {
		case EQ:
			nowPredicate = builder.equal(paths.get(0), searchFilter.value);
			break;
		case NEQ:
			nowPredicate = builder.notEqual(paths.get(0), searchFilter.value);
			break;
		case LIKE:
			nowPredicate = builder.like(paths.get(0), "%" + searchFilter.value + "%");
			break;
		case CLIKE:
			nowPredicate = builder.like(paths.get(0), searchFilter.value.toString());
			break;	
		case CLIKEIN:
			String [] likeConditionValues = searchFilter.value.toString().split(",");
			List<Predicate> pres = new ArrayList<Predicate>();
			for(int i=0; i<likeConditionValues.length; i++) {
				pres.add(builder.like(paths.get(0), likeConditionValues[i]));
			}
			nowPredicate =  builder.or(pres.toArray(new Predicate[1]));
			break;
		case CLICKIN:
			String [] likeConditionValues1 = searchFilter.value.toString().split(",");
			List<Predicate> pres1 = new ArrayList<Predicate>();
			for(int i=0; i<likeConditionValues1.length; i++) {
				pres1.add(builder.like(paths.get(0), likeConditionValues1[i]));
			}
			nowPredicate = builder.or(pres1.toArray(new Predicate[1]));
			break;
		case GT:
			nowPredicate = builder.greaterThan(paths.get(0), (Comparable) searchFilter.value);
			break;
		case LT:
			nowPredicate = builder.lessThan(paths.get(0), (Comparable) searchFilter.value);
			break;
		case GTE:
			nowPredicate = builder.greaterThanOrEqualTo(paths.get(0), (Comparable) searchFilter.value);
			break;
		case LTE:
			nowPredicate = builder.lessThanOrEqualTo(paths.get(0), (Comparable) searchFilter.value);
			break;
		case ISNULL:
			nowPredicate = builder.isNull(paths.get(0));
			break;
		case ISNOTNULL:
			nowPredicate = builder.isNotNull(paths.get(0));
			break;
		case IN:
			nowPredicate = builder.in(paths.get(0)).value(searchFilter.value);
			break;
		case NOTIN:
			nowPredicate = builder.not(builder.in(paths.get(0)).value(searchFilter.value));
			break;
		case ORIN:
			List <Object>list = (List<Object>)searchFilter.value;
			List<Predicate> presTemp = new ArrayList<Predicate>();
			for(int j=0;j<paths.size(); j++) {
				presTemp.add(builder.in(paths.get(j)).value(list.get(j)));
			}
			nowPredicate = builder.or(presTemp.toArray(new Predicate[1]));
			break;
		case OR:
			List <Object>listOr = (List<Object>)searchFilter.value;
			List<Predicate> presTempOr = new ArrayList<Predicate>();
			for(int j=0;j<paths.size(); j++) {
				presTempOr.add(builder.equal(paths.get(j), listOr.get(j)));
			}
			nowPredicate = builder.or(presTempOr.toArray(new Predicate[1]));
			break;
		case BETWDATE:
			List <Object> list1 = (List<Object>)searchFilter.value;
			nowPredicate = builder.between(paths.get(0), (Date)list1.get(0), (Date)list1.get(1));
			break;
		case BETWEEN: 
			List <Object> listB = (List<Object>)searchFilter.value;
			nowPredicate = builder.between(paths.get(0), (Comparable)listB.get(0), (Comparable)listB.get(1));
			break;			
		}		
		
		return nowPredicate;
	}
	
	public static Predicate handleOrSearchFilter(Root root, CriteriaBuilder builder,SearchFilter searchFilter, Predicate nowPredicate, Map<String, Join> joinCache) {
		List <Predicate> orPredicates = new ArrayList<Predicate>();
		for(SearchFilter orSearchFilter : searchFilter.getOrSearchFilters()) {
			List <Path> paths1 = Lists.newArrayList();
			paths1.add(builderPath(root, orSearchFilter, joinCache));
			orPredicates.add(builderPredicate(builder, orSearchFilter, paths1));
		}
		
		Predicate orPredicate = null; 
		for(Predicate predicate : orPredicates) {
			if(orPredicate == null) {
				orPredicate = predicate;
			} else {
				orPredicate = builder.or(orPredicate, predicate);
			}
			
		}
		return orPredicate;
	}
	
	public static Predicate handleAndSearchFilter(Root root, CriteriaBuilder builder,SearchFilter searchFilter, Predicate nowPredicate, Map<String, Join> joinCache) {
		List <Predicate> andPredicates = new ArrayList<Predicate>();
		for(SearchFilter andSearchFilter : searchFilter.getAndSearchFilters()) {
			List <Path> paths1 = Lists.newArrayList();
			paths1.add(builderPath(root, andSearchFilter, joinCache));
			andPredicates.add(builderPredicate(builder, andSearchFilter, paths1));
		}
		
		Predicate andPredicate = null; 
		for(Predicate predicate : andPredicates) {
			if(andPredicate == null) {
				andPredicate = predicate;
			} else {
				andPredicate = builder.and(andPredicate, predicate);
			}
			
		}
		return andPredicate;
		
	}
}
