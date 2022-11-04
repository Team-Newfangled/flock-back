package com.newfangled.flockbackend.domain.member.service;

import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import com.newfangled.flockbackend.domain.team.dto.response.MemberTodoRO;
import com.newfangled.flockbackend.global.dto.response.ResultListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberTodoFindService {

    private final EntityManager entityManager;

    public ResultListDto<MemberTodoRO> findTodoByDate(final long userId,
                                                      final int year, final int month, final int date) {
        final String sql = "select * from TodoDetail where TodoDetail.teamMember_id" +
                " in (select id from team_member where team_member.member_id = " +
                "(select id from Member where id = ?1)) " +
                "and TodoDetail.todo_id in (select id from Todo where completed = false) " +
                "and ?2 between startDate and endDate limit 5";
        List<TodoDetail> todoDetails = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            List<TodoDetail> details = (List<TodoDetail>) entityManager.createNativeQuery(sql, TodoDetail.class)
                    .setParameter(1, userId)
                    .setParameter(2, LocalDate.of(year, month, date))
                    .getResultList();
            System.out.printf("date: %d-%d-%d", year, month, date);
            System.out.println(details);
            todoDetails.addAll(details);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return new ResultListDto<>(todoDetails.stream()
                .map(MemberTodoRO::new)
                .collect(Collectors.toList()));
    }
}
