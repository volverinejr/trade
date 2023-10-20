package br.com.claudemirojr.trade.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import lombok.Data;

@Data
public class ParamsRequestModel {

	private int page = 0;
	private int size = 10;
	private String sort = "id";

	public ParamsRequestModel(Map<String, String> params) {
		if (params.containsKey("page"))
			page = Integer.parseInt(params.get("page"));

		if (params.containsKey("size"))
			size = Integer.parseInt(params.get("size"));

		if (params.containsKey("sort"))
			sort = params.get("sort");
	}

	public PageRequest toSpringPageRequest() {
		List<Order> orders = new ArrayList<Order>();

		String[] campos = sort.split(",");

		for (String campo : campos) {
			if (campo.trim().length() > 0) {
				String column = campo.trim();

				if (column.startsWith("-")) {
					column = column.replace("-", "");

					orders.add(Order.desc(column));
				} else {
					orders.add(Order.asc(column));
				}
			}
		}

		return PageRequest.of(page, size, Sort.by(orders));
	}

}
