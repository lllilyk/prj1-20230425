package com.example.demo.mapper;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.controller.*;
import com.example.demo.domain.*;

@Mapper
public interface BoardMapper {

	@Select("""
			SELECT id,
				   title,
				   writer,
				   inserted
			FROM Board
			ORDER BY id DESC
			""")
	List<Board> selectAll();

	@Select("""
			SELECT *
			FROM Board
			WHERE id = #{id}
			""")
	Board selectById(Integer id);

	@Update("""
			UPDATE Board
			SET title = #{title},
				body = #{body},
				writer = #{writer}
			WHERE id = #{id}
			""")
	int update(Board board);

	
	@Delete("""
			DELETE FROM Board
			WHERE id = #{id}
			""")

	int deleteById(Integer id);

	@Insert("""
			INSERT INTO Board(title, body, writer)
			VALUES (#{title}, #{body}, #{writer})
			""")
	//자동 증가하는 키값을 사용하고 싶을 때
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(Board board);
	
}