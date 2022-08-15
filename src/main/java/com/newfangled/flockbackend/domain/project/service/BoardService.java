package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.response.BoardDto;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Board;
import com.newfangled.flockbackend.domain.project.repository.BoardRepository;
import com.newfangled.flockbackend.domain.project.repository.ProjectRepository;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ProjectRepository projectRepository;

    public LinkListDto saveFile(Member member, long boardId,
                                ContentDto contentDto) {
        Board board = findById(boardId);
        validateMember(board.getProject(), member);
        board.updateFile(contentDto.getContent());

        return new LinkListDto(
                "피드에 파일을 저장하였습니다.",
                List.of(new LinkDto("self", "GET", String.format("/board/%d", boardId)))
        );
    }

    public BoardDto saveBoard(Member member, long projectId,
                              ContentDto contentDto) {
        Project project = findProjectById(projectId);
        TeamMember teamMember = validateMember(project, member);
        Board board = new Board(
                null, project, teamMember, contentDto.getContent(), null
        );
        return new BoardDto(boardRepository.save(board));
    }

    public LinkListDto modifyContent(Member member, long boardId,
                                     ContentDto contentDto) {
        Board board = findById(boardId);
        validateMember(board.getProject(), member);
        board.modifyContent(contentDto.getContent());
        return new LinkListDto(
                "피드 글을 수정하였습니다.",
                List.of(new LinkDto("self", "rel", String.format("/board/%d", boardId)))
        );
    }

    public void deleteBoard(Member member, long boardId) {
        Board board = findById(boardId);
        validateMember(board.getProject(), member);
        boardRepository.delete(board);
    }

    public BoardDto findBoard(long boardId) {
        return new BoardDto(findById(boardId));
    }

    public PageDto<BoardDto> findBoardPage(long projectId, int page) {
        Project project = findProjectById(projectId);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id"));
        Page<Board> boardPage = boardRepository.findAllByProject(project, pageable);
        return new PageDto<>(
                boardPage.getNumber(),
                boardPage.getTotalPages(),
                boardPage.stream().map(BoardDto::new)
                        .collect(Collectors.toList())
        );
    }

    public LinkListDto deleteFile(Member member, long boardId) {
        Board board = findById(boardId);
        validateMember(board.getProject(), member);
        board.updateFile(null);
        return new LinkListDto(
                "피드 파일을 삭제하였습니다.",
                List.of(new LinkDto("self", "rel", String.format("/board/%d", boardId)))
        );
    }

    @Transactional(readOnly = true)
    protected Board findById(long id) {
        return boardRepository.findById(id)
                .orElseThrow(Board.NotExistsException::new);
    }

    @Transactional(readOnly = true)
    protected Project findProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(Project.NotExistsException::new);
    }

    @Transactional(readOnly = true)
    protected TeamMember validateMember(Project project, Member member) {
        Team team = project.getTeam();
        TeamMember teamMember = teamMemberRepository
                .findByTeamId(new TeamId(team, member))
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (!teamMember.isApproved()) {
            throw new TeamMember.NoPermissionException();
        }

        return teamMember;
    }
}
