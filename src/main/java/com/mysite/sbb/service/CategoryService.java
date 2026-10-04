package com.mysite.sbb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.sbb.entity.Category;
import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	public List<Category> getList(){
		return categoryRepository.findAll();
	}

    public Category getCategory(Long categoryId) {
    	
        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isPresent()) {
            return category.get();
        } else {
            throw new DataNotFoundException("category not found");
        }
    }

}
