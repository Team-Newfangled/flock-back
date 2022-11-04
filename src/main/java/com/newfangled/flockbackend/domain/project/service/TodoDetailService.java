package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
import com.newfangled.flockbackend.domain.project.dto.request.TodoPercentDto;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoDetailRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoRepository;
import com.newfangled.flockbackend.domain.team.dto.request.JoinMemberDto;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.ResultListDto;
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
    private final MemberRepository memberRepository;

    public LinkListDto modifyColor(Member member, long projectId,
                                   long todoId, ContentDto contentDto) {
        Project project = findProjectById(projectId);
        validateMember(project, member, true);
        Todo todo = findById(todoId);
        TodoDetail todoDetail = todo.getTodoDetail();
        todoDetail.modifyColor(contentDto.getContent());
        todoDetailRepository.save(todoDetail);

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
                        projectId, startDate, endDate
        );

        return new ResultListDto<>(
                todoDetails.stream().map(TodoDetail::getTodo)
                        .map(TodoDto::new)
                        .collect(Collectors.toList())
        );
    }

    // 담당자 지정
    public LinkListDto modifyManager(Member leader, long todoId,
                                     final JoinMemberDto joinMemberDto) {
        Todo todo = findById(todoId);
        Project project = todo.getProject();
        validateMember(project, leader, true);
        Member member = memberRepository.findById(joinMemberDto.getMemberId())
                .orElseThrow(Member.NotExistsException::new);
        TeamMember teamMember = validateMember(project, member, false);

        TodoDetail todoDetail = todo.getTodoDetail();
        todoDetail.setTeamMember(teamMember);
        todoDetailRepository.save(todoDetail);
        return new LinkListDto(
                "담당자를 변경하였습니다.",
                List.of(new LinkDto("self", "GET", String.format("/todo/%d", todoId)))
        );
    }

    // 진행도 (퍼센트) 수정
    public LinkListDto modifyPercent(Member member, long todoId,
                                     TodoPercentDto todoPercentDto) {
        Todo todo = findById(todoId);
        Project project = todo.getProject();
        TeamMember teamMember = validateMember(project, member, false);
        TodoDetail todoDetail = todo.getTodoDetail();
        final long manager = todoDetail.getTeamMember().getId();
        if (teamMember.getId() != manager && teamMember.getRole() != Role.Leader) {
            // 담당자 혹은 팀장 둘 다 아니라면 예외
            throw new TeamMember.NoPermissionException();
        }

        todoDetail.setPercent(todoPercentDto.getPercent());
        todoDetailRepository.save(todoDetail);
        return new LinkListDto(
                "진행도를 수정하였습니다.",
                List.of(new LinkDto("self", "GET", String.format("/todo/%d", todoId)))
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
    protected TeamMember validateMember(Project project, Member member, boolean requireLeader) {
        Team team = project.getTeam();
        TeamMember teamMember = teamMemberRepository
                .findByTeamAndMember(team, member)
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (!teamMember.isApproved()) {
            throw new TeamMember.NoPermissionException();
        }

        if (requireLeader && teamMember.getRole() != Role.Leader) {
            throw new TeamMember.NoPermissionException();
        }

        return teamMember;
    }

    private LocalDate getDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.set(year, month - 1, 1);
        System.out.println(month);
        System.out.println(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return LocalDate.of(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }
}
