package com.eeit45team2.lungspringbootversion.backend.animal.json;

import com.eeit45team2.lungspringbootversion.backend.animal.model.AbDogBean;

import java.util.List;


public interface JsonExporter {

	String export(List<AbDogBean> abdogbeans);
	
}