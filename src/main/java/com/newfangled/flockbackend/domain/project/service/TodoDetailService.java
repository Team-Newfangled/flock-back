package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.embed.DetailId;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoDetailRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoRepository;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.ResultListDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoDetailService {

    private final TodoRepository todoRepository;
    private final TodoDetailRepository todoDetailRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ProjectRepository projectRepository;

    public LinkListDto modifyColor(Member member, long projectId,
                                   long todoId, ContentDto contentDto) {
        Project project = findProjectById(projectId);
        validateMember(project, member);
        Todo todo = findById(todoId);
        TodoDetail todoDetail = todo.getTodoId().getTodoDetail();
        todoDetail.modifyColor(contentDto.getContent());

        return new LinkListDto(
                "색을 변경하였습니다.",
                List.of(new LinkDto("self", "GET", String.format("/todo/%d", todoId)))
        );
    }

    public ResultListDto<TodoDto> findDetails(long projectId, int year, int month) {
        Project project = findProjectById(projectId);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = getDayOfMonth(year, month);

        List<TodoDetail> todoDetails = todoDetailRepository
                .findByStartDateBetween(
                        startDate, endDate
        );

        return new ResultListDto<>(
                todoDetails.stream().map(TodoDetail::getDetailId)
                        .map(DetailId::getTodo)
                        .map(TodoDto::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    protected Project findProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(Project.NotExistsException::new);
    }

    @Transactional(readOnly = true)
    protected Todo findById(long id) {
        return todoRepository.findById(id)
                .orElseThrow(Todo.NotExistsException::new);
    }

    @Transactional(readOnly = true)
    protected void validateMember(Project project, Member member) {
        Team team = project.getTeam();
        teamMemberRepository
                .findByMember_IdAndTeamId(member.getId(), new TeamId(team))
                .orElseThrow(TeamMember.NoPermissionException::new);
    }

    private LocalDate getDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.set(year, month - 1, 1);
        System.out.println(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return LocalDate.of(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }
}
