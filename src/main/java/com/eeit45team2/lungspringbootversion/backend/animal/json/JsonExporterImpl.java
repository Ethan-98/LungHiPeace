package com.eeit45team2.lungspringbootversion.backend.animal.json;
import java.util.List;

import com.eeit45team2.lungspringbootversion.backend.animal.model.AbDogBean;
import org.springframework.stereotype.Service;
import com.eeit45team2.lungspringbootversion.backend.animal.model.*;
import com.google.gson.Gson;
import com.eeit45team2.lungspringbootversion.backend.animal.json.JsonExporterImpl;


@Service
public class JsonExporterImpl implements JsonExporter {

	@Override
	public String export(List<AbDogBean> abdogbeans) {
		Gson gson = new Gson();
		String employeeInJson = gson.toJson(abdogbeans);
		return employeeInJson;
	}

}
