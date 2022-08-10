package com.newfangled.flockbackend.domain.project.controller;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.project.dto.request.TodoModifyDto;
import com.newfangled.flockbackend.domain.project.dto.response.TodoDto;
import com.newfangled.flockbackend.domain.project.service.TodoService;
import com.newfangled.flockbackend.global.dto.request.ContentDto;
import com.newfangled.flockbackend.global.dto.response.LinkListDto;
import com.newfangled.flockbackend.global.dto.response.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/projects/{id}/todo")
    public TodoDto createTodo(Authentication authentication,
                              @PathVariable("id") long id,
                              @RequestBody final ContentDto contentDto) {
        return todoService.writeTodo(
                (Member) authentication.getPrincipal(), id, contentDto
        );
    }

    @PutMapping("/todo/{id}")
    public LinkListDto modifyTodo(Authentication authentication,
                                  @PathVariable("id") long id,
                                  @RequestBody final TodoModifyDto todoModifyDto) {
        return todoService.modifyTodo(
                (Member) authentication.getPrincipal(), id, todoModifyDto
        );
    }

    @DeleteMapping("/todo/{id}")
    public void deleteTodo(Authentication authentication,
                           @PathVariable("id") long id) {
        todoService.deleteTodo((Member) authentication.getPrincipal(), id);
    }

    @PatchMapping("/todo/{id}")
    public LinkListDto completeTodo(Authentication authentication,
                                    @PathVariable("id") long id) {
        return todoService.completeTodo((Member) authentication.getPrincipal(), id);
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
}
