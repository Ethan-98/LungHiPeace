package com.eeit45team2.lungspringbootversion.backend.order.pay;

import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EcpayPayment {
	public static AllInOne all;

	private static void initial(){
		all = new AllInOne("");
	}
	
	public static String genAioCheckOutALL(Order order, String url){

		initial();

		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(order.getOrderNo());
		obj.setMerchantTradeDate(df.format(order.getOrderDate()));
		obj.setTotalAmount(order.getTotalPrice().toString());
		
		obj.setTradeDesc("浪還和平");
		obj.setItemName("罐頭X1");
		
		obj.setReturnURL("http://211.23.128.214:5000");
		
		obj.setOrderResultURL(url);
		obj.setNeedExtraPaidInfo("N");
		String form = all.aioCheckOut(obj, null);
		return form;
	}
}
