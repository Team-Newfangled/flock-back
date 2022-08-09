package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.embed.TodoId;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Todo;
import com.newfangled.flockbackend.domain.project.entity.sub.TodoDetail;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.project.repository.TodoRepository;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.domain.team.type.Role;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TeamMemberRepository teamMemberRepository;
    private final ProjectRepository projectRepository;
    private final TodoRepository todoRepository;

    public TodoDto writeTodo(Member member, long projectId, ContentDto contentDto) {
        Project project = findById(projectId);
        TeamMember teamMember = validateLeader(project, member);
        Todo todo = new Todo(
                null,
                new TodoId(
                    project,
                    new TodoDetail(
                            null,
                            teamMember,
                            contentDto.getContent(),
                            getRandomHexColor(),
                            null,
                            null
                    )
                )
        );

        return new TodoDto(todoRepository.save(todo));
    }

    @Transactional(readOnly = true)
    protected Project findById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(Project.NotExistsException::new);
    }

    @Transactional(readOnly = true)
    protected TeamMember validateLeader(Project project, Member member) {
        Team team = project.getTeam();
        return teamMemberRepository
                .findByMember_IdAndTeamId(member.getId(), new TeamId(team))
                .orElseThrow(TeamMember.NoPermissionException::new);
    }

    private String getRandomHexColor() {
        byte[] bytes = new byte[3];
        new Random().nextBytes(bytes);
        StringBuilder result = new StringBuilder();
        for (byte temp : bytes) {
            result.append(String.format("%02x", temp));
        }

        return result.toString();
    }

}
