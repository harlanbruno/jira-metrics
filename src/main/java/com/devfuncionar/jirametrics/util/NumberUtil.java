package com.devfuncionar.jirametrics.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author harlan bruno
 * @since 19/03/2018
 * @version 1.0
 *
 */
public class NumberUtil {

	public static BigDecimal convertToBigDecimal(Double value) {
		return BigDecimal.valueOf(value);
	}

	public static BigDecimal convertToBigDecimal(String value) {
		return new BigDecimal(value);
	}
	
//	public static void main(String[] args) {
//		
//		String value = "1.060,00 D";
//		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(new Locale ("pt", "BR"));
//		DecimalFormat df = new DecimalFormat("###", unusualSymbols);
//		df.
//	}

}
