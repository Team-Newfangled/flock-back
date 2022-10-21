package com.newfangled.flockbackend.domain.project.service;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.response.CommentDto;
import com.newfangled.flockbackend.domain.project.entity.Project;
import com.newfangled.flockbackend.domain.project.entity.sub.Board;
import com.newfangled.flockbackend.domain.project.entity.sub.BoardComment;
import com.newfangled.flockbackend.domain.project.repository.BoardCommentRepository;
import com.newfangled.flockbackend.domain.project.repository.BoardRepository;
import com.newfangled.flockbackend.domain.team.entity.Team;
import com.newfangled.flockbackend.domain.team.entity.TeamMember;
import com.newfangled.flockbackend.domain.team.repository.TeamMemberRepository;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import com.newfangled.flockbackend.global.embed.TeamId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final TeamMemberRepository teamMemberRepository;

    public CommentDto writeComment(Member member, long boardId,
                                   ContentDto contentDto) {
        Board board = findBoardById(boardId);
        TeamMember teamMember = validateMember(board.getProject(), member);
        BoardComment boardComment = new BoardComment(
                null, board, teamMember, contentDto.getContent()
        );
        return new CommentDto(boardCommentRepository.save(boardComment));
    }

    public PageDto<CommentDto> findAllComments(long boardId, int page) {
        Board board = findBoardById(boardId);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<BoardComment> comments = boardCommentRepository
                .findAllByBoard(board, pageable);
        return new PageDto<>(
                comments.getNumber(), comments.getTotalPages(),
                comments.stream().map(CommentDto::new).collect(Collectors.toList())
        );
    }

    public void deleteComment(Member member, long commentId) {
        BoardComment boardComment = findById(commentId);
        validateMember(boardComment.getBoard().getProject(), member);
        boardCommentRepository.delete(boardComment);
    }

    public void deleteCommentByBoard(Board board) {
        boardCommentRepository.deleteAllByBoard(board);
    }

    public void deleteAllCommentByProject(Project project) {
        boardCommentRepository.deleteAllByBoard_Project(project);
    }

    @Transactional(readOnly = true)
    protected TeamMember validateMember(Project project, Member member) {
        Team team = project.getTeam();
        TeamMember teamMember = teamMemberRepository
                .findByTeamId(new TeamId(team, member))
                .orElseThrow(TeamMember.NoPermissionException::new);
        if (!teamMember.isApproved()) {
            // 미승인 회원이라면
            throw new TeamMember.NoPermissionException();
        }

        return teamMember;
    }

    @Transactional(readOnly = true)
    protected BoardComment findById(long id) {
        return boardCommentRepository.findById(id)
                .orElseThrow(BoardComment.NotExistsException::new);
    }

    @Transactional(readOnly = true)
    protected Board findBoardById(long id) {
        return boardRepository.findById(id)
                .orElseThrow(Board.NotExistsException::new);
    }
}
