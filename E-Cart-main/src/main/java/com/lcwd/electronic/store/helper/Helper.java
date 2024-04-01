package com.lcwd.electronic.store.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.models.User;

public class Helper {
	
	public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){
		
        List<U> users = page.getContent(); 
		
		
		List<V> dtolist = users.stream().map(object-> new ModelMapper().map(object, type)).collect(Collectors.toList());
		
		PageableResponse<V> pageableResponse = new PageableResponse<>();
		pageableResponse.setContent(dtolist);
		pageableResponse.setLastPage(page.isLast());
		pageableResponse.setPageNumber(page.getNumber());
		pageableResponse.setTotalElements(page.getTotalElements());
		pageableResponse.setPageSize(page.getSize());
		pageableResponse.setTotalPages(page.getTotalPages());
		
	 return pageableResponse;
	}

}
