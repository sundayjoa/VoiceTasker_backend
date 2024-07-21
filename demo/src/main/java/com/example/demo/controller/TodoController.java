package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(){
		String temporaryUserId = "temporary-user";
		
		//Todo 리스트 가져오기
		List<TodoEntity> entities = service.retrieve(temporaryUserId);
		
		//리턴된 엔티티 리스트 TodoDTO 리스트로 변환
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		//ResponseDTO 리턴
		return ResponseEntity.ok().body(response);		
	}
	
	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto){
		try {
			String temporaryUserId = "temporary-user";
			
			//TodoEntity로 변환
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			//id null로 초기화
			entity.setId(null);
			
			//임시 유저 아이지 설정.(이후 수정)
			entity.setUserId(temporaryUserId);
			
			//서비스 이용해 Todo 엔티티 생성
			List<TodoEntity> entities = service.create(entity);
			
			//리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//ResponseDTO 리턴
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			//예외 경우 error 메시지 리턴
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
			
		}
	}
}