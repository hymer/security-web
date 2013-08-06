package com.hymer.core.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SMS {

	/**
	 * 发送短信
	 * @param mobile
	 * @param content
	 * @return
	 */
	public static String sendSMS(String mobile, String content) {
		String resultCode = null;
		/**
		 * 先到http://sms.webchinese.cn/注册。
		 */
		try {
			if (mobile != null) return null;//TODO 暂时关闭短信
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(
					"http://sms.webchinese.cn/web_api/");
			post.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
			NameValuePair[] data = { new NameValuePair("Uid", "hymer"), // 注册的用户名
					new NameValuePair("Key", "Iloveyoudoyouloveme"), // 短信密钥
					new NameValuePair("smsMob", mobile), // 手机号码
					new NameValuePair("smsText", content + "【赞布村载有限公司】") };
			post.setRequestBody(data);
			client.executeMethod(post);
			// Header[] headers = post.getResponseHeaders();
			// int statusCode = post.getStatusCode();
			// System.out.println("statusCode:" + statusCode);
			// for (Header h : headers) {
			// System.out.println(h.toString());
			// }
			resultCode = new String(post.getResponseBodyAsString().getBytes(
					"gbk"));
			post.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultCode;
	}
	
	/**
	 * 查询短信剩余数量.
	 * @return
	 */
	public static int getSMSAmount() {
		String resultCode = null;
		/**
		 * 先到http://sms.webchinese.cn/注册。
		 */
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(
					"http://sms.webchinese.cn/web_api/SMS/GBK/");
			post.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
			NameValuePair[] data = { new NameValuePair("Uid", "hymer"), // 注册的用户名
					new NameValuePair("Key", "Iloveyoudoyouloveme"), // 短信密钥
					new NameValuePair("Action", "SMS_Num")};
			post.setRequestBody(data);
			client.executeMethod(post);
			resultCode = new String(post.getResponseBodyAsString().getBytes(
					"gbk"));
			post.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.parseInt(resultCode);
	}
	
}
