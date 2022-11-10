package com.jeeok.jeeokmember.core.member.repository;

import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.member.dto.SearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeeok.jeeokmember.core.member.domain.QMember.member;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MemberQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 멤버 목록 조회
     */
    public Page<Member> findMembers(MemberSearchCondition condition, Pageable pageable) {
        List<Member> content = getMemberList(condition, pageable);

        JPAQuery<Long> count = getMemberListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 멤버 목록
     */
    private List<Member> getMemberList(MemberSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(member)
                .from(member)
                .where(
                        searchCondition(condition.getSearchCondition(), condition.getSearchKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.id.desc())
                .fetch();
    }

    /**
     * 멤버 목록 카운트
     */
    private JPAQuery<Long> getMemberListCount(MemberSearchCondition condition) {
        return queryFactory
                .select(member.count())
                .from(member)
                .where(
                        searchCondition(condition.getSearchCondition(), condition.getSearchKeyword())
                );
    }

    /**
     * where searchCondition like '%searchKeyword%'
     */
    private BooleanExpression searchCondition(SearchCondition searchCondition, String searchKeyword) {
        if (searchCondition == null || !hasText(searchKeyword)) {
            return null;
        }

        if (searchCondition.equals(SearchCondition.EMAIL)) {
            return member.email.contains(searchKeyword);
        } else if (searchCondition.equals(SearchCondition.NAME)) {
            return member.name.contains(searchKeyword);
        }

        return null;
    }
}
