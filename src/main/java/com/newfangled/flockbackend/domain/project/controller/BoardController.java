package com.newfangled.flockbackend.domain.project.controller;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.response.BoardDto;
import com.newfangled.flockbackend.domain.project.service.BoardService;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards/{id}/files")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkListDto saveFile(Authentication authentication,
                                @PathVariable("id") long id,
                                @RequestBody @Valid final ContentDto contentDto) {
        return boardService.saveFile(
                (Member) authentication.getPrincipal(),
                id, contentDto
        );
    }

    @PostMapping("/projects/{id}/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public BoardDto saveBoard(Authentication authentication,
                              @PathVariable("id") long id,
                              @RequestBody @Valid final ContentDto contentDto) {
        return boardService.saveBoard(
                (Member) authentication.getPrincipal(),
                id, contentDto
        );
    }

    @PatchMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkListDto modifyBoard(Authentication authentication,
                                   @PathVariable("id") long id,
                                   @RequestBody @Valid final ContentDto contentDto) {
        return boardService.modifyContent(
                (Member) authentication.getPrincipal(),
                id, contentDto
        );
    }

    @DeleteMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(Authentication authentication,
                            @PathVariable("id") long id) {
        boardService.deleteBoard((Member) authentication.getPrincipal(), id);
    }

    @GetMapping("/boards/{id}")
    public BoardDto findBoard(@PathVariable("id") long id) {
        return boardService.findBoard(id);
    }

    @GetMapping("/projects/{id}/boards")
    public PageDto<BoardDto> findBoardPage(@PathVariable("id") long id,
                                           @RequestParam("page") int page) {
        return boardService.findBoardPage(id, page);
    }

    @DeleteMapping("/boards/{id}/files")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public LinkListDto deleteFile(Authentication authentication,
                                  @PathVariable("id") long id) {
        return boardService.deleteFile((Member) authentication.getPrincipal(), id);
    }
}
