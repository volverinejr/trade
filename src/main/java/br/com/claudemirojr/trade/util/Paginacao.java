package br.com.claudemirojr.trade.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public class Paginacao {

	public Pageable getPageable(Pageable pageable) {
		int maxSize = 100;
		
		if (pageable.getPageSize() > maxSize) {
			pageable = PageRequest.of(pageable.getPageNumber(), maxSize, pageable.getSort());
		}

		return pageable;
	}
	
	
	public boolean isStringNumeric(String number)
	{
		boolean isNumeric;
		String regex = "-?\\d+(\\.\\d+)?";		
		if(number == null)
			isNumeric = false;
		else if(number.matches(regex))
			isNumeric = true;
		else
			isNumeric = false;		
		return isNumeric;
	}	
	

}
