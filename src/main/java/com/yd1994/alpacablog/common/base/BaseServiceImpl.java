package com.yd1994.alpacablog.common.base;

import com.yd1994.alpacablog.common.exception.RequestParamErrorException;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基础 ServiceImpl 类
 *
 * 通用实现
 *
 * @param <E>
 */
public abstract class BaseServiceImpl<E> {

    protected Specification<E> getRestSpecification(RestRequestParam requestParam) {
        return (Specification<E>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (requestParam.getBefore() != null) {
                if (!StringUtils.isEmpty(requestParam.getBeforeBy())) {
                    if ("created".equals(requestParam.getBeforeBy())) {
                        requestParam.setBeforeBy("gmtCreated");
                    }
                    else if ("modified".equals(requestParam.getBeforeBy())) {
                        requestParam.setBeforeBy("gmtModified");
                    } else {
                        throw new RequestParamErrorException("beforeBy参数不可为：" + requestParam.getBeforeBy() +  "。");
                    }
                    // 在 该时间 之前（包含该时间）
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get(requestParam.getBeforeBy()).as(Date.class), requestParam.getBefore()));
                } else {
                    throw new RequestParamErrorException("beforeBy参数不可空。");
                }
            }
            if (requestParam.getAfter() != null) {
                if (!StringUtils.isEmpty(requestParam.getAfterBy())) {
                    if ("created".equals(requestParam.getAfterBy())) {
                        requestParam.setAfterBy("gmtCreated");
                    }
                    else if ("modified".equals(requestParam.getAfterBy())) {
                        requestParam.setAfterBy("gmtModified");
                    } else {
                        throw new RequestParamErrorException("afterBy参数不可为：" + requestParam.getBeforeBy() +  "。");
                    }
                    // 在 该时间 之后（包含该时间）
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get(requestParam.getAfterBy()).as(Date.class), requestParam.getAfter()));
                } else {
                    throw new RequestParamErrorException("afterBy参数不可空。");
                }
            }
            // 筛选出未删除
            predicateList.add(criteriaBuilder.equal(root.get("delete").as(Boolean.class), false));
            this.addRestSpecificationPredicateList(predicateList, root, criteriaBuilder, requestParam);
            Predicate[] p = new Predicate[predicateList.size()];
            query.where(criteriaBuilder.and(predicateList.toArray(p)));
            if (!StringUtils.isEmpty(requestParam.getSortByAsc())) {
                if ("created".equals(requestParam.getSortByAsc())) {
                    requestParam.setSortByAsc("gmtCreated");
                }
                else if ("modified".equals(requestParam.getSortByAsc())) {
                    requestParam.setSortByAsc("gmtModified");
                }
                query.orderBy(criteriaBuilder.asc(root.get(requestParam.getSortByAsc())));
            }
            if (!StringUtils.isEmpty(requestParam.getSortByDesc())) {
                if ("created".equals(requestParam.getSortByDesc())) {
                    requestParam.setSortByDesc("gmtCreated");
                }
                else if ("modified".equals(requestParam.getSortByDesc())) {
                    requestParam.setSortByDesc("gmtModified");
                }
                query.orderBy(criteriaBuilder.desc(root.get(requestParam.getSortByDesc())));
            }
            return query.getRestriction();
        };
    }

    /**
     *
     *
     * @param predicateList
     * @param root
     * @param criteriaBuilder
     */
    protected abstract void addRestSpecificationPredicateList(List<Predicate> predicateList, Root<E> root, CriteriaBuilder criteriaBuilder, RestRequestParam requestParam);

}
