package com.newfangled.flockbackend.domain.project.controller;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.request.TodoCompleteDto;
import com.newfangled.flockbackend.domain.project.dto.request.TodoModifyDto;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.service.TodoDetailService;
import com.newfangled.flockbackend.domain.project.service.TodoService;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import com.newfangled.flockbackend.global.dto.response.ResultListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final TodoDetailService todoDetailService;

    @PostMapping("/projects/{id}/todo")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDto createTodo(Authentication authentication,
                              @PathVariable("id") long id,
                              @RequestBody final ContentDto contentDto) {
        return todoService.writeTodo(
                (Member) authentication.getPrincipal(), id, contentDto
        );
    }

    @PatchMapping("/todo/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkListDto modifyTodo(Authentication authentication,
                                  @PathVariable("id") long id,
                                  @RequestBody final TodoModifyDto todoModifyDto) {
        return todoService.modifyTodo(
                (Member) authentication.getPrincipal(), id, todoModifyDto
        );
    }

    @DeleteMapping("/todo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(Authentication authentication,
                           @PathVariable("id") long id) {
        todoService.deleteTodo((Member) authentication.getPrincipal(), id);
    }

    @PatchMapping("/todo/complete/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkListDto completeTodo(Authentication authentication,
                                    @PathVariable("id") long id,
                                    @RequestBody @Valid TodoCompleteDto todoCompleteDto) {
        return todoService.completeTodo(
                (Member) authentication.getPrincipal(), id, todoCompleteDto
        );
    }

    @GetMapping("/projects/{project-id}/team-member/{user-id}/todo")
    public PageDto<TodoDto> findAllTodos(Authentication authentication,
                                         @PathVariable("project-id") long projectId,
                                         @PathVariable("user-id") long userId,
                                         @RequestParam int page) {
        return todoService.findAllTodos(
                (Member) authentication.getPrincipal(), projectId, userId, page
        );
    }

    @GetMapping("/todo/{id}")
    public TodoDto findTodo(@PathVariable("id") long id) {
        return todoService.findTodo(id);
    }

    @PatchMapping("/projects/{project-id}/deadline/{todo-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkListDto modifyColor(Authentication authentication,
                                   @PathVariable("project-id") long projectId,
                                   @PathVariable("todo-id") long todoId,
                                   @RequestBody @Valid ContentDto contentDto) {
        return todoDetailService.modifyColor(
                (Member) authentication.getPrincipal(),
                projectId,
                todoId,
                contentDto
        );
    }

    @GetMapping("/projects/{id}/deadline")
    public ResultListDto<TodoDto> findDeadlines(@PathVariable("id") long id,
                                                @RequestParam int year,
                                                @RequestParam int month) {
        return todoDetailService.findDetails(id, year, month);
    }
}
