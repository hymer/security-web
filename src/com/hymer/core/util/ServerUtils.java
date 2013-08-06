package com.hymer.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class ServerUtils {

	public static final String BR = "<br/>";

	public static final String getServerInformation() {
		try {
			InetAddress s = InetAddress.getLocalHost();
			return "本机的机器名：" + s.getHostName() + BR;
		} catch (UnknownHostException e) {
		}
		return "";
	}

	public static final String getSystemProperties() {
		Properties props = System.getProperties();
		StringBuffer sb = new StringBuffer();
		sb.append("Java的运行环境版本：" + props.getProperty("java.version") + BR);
		sb.append("Java的运行环境供应商：" + props.getProperty("java.vendor") + BR);
		sb.append("Java供应商的URL：" + props.getProperty("java.vendor.url") + BR);
		sb.append("Java的安装路径：" + props.getProperty("java.home") + BR);
		sb.append("Java的虚拟机规范版本："
				+ props.getProperty("java.vm.specification.version") + BR);
		sb.append("Java的虚拟机规范供应商："
				+ props.getProperty("java.vm.specification.vendor") + BR);
		sb.append("Java的虚拟机规范名称："
				+ props.getProperty("java.vm.specification.name") + BR);
		sb.append("Java的虚拟机实现版本：" + props.getProperty("java.vm.version") + BR);
		sb.append("Java的虚拟机实现供应商：" + props.getProperty("java.vm.vendor") + BR);
		sb.append("Java的虚拟机实现名称：" + props.getProperty("java.vm.name") + BR);
		sb.append("Java运行时环境规范版本："
				+ props.getProperty("java.specification.version") + BR);
		sb.append("Java运行时环境规范供应商："
				+ props.getProperty("java.specification.vender") + BR);
		sb.append("Java运行时环境规范名称："
				+ props.getProperty("java.specification.name") + BR);
		sb.append("Java的类格式版本号：" + props.getProperty("java.class.version") + BR);
		sb.append("Java的类路径：" + props.getProperty("java.class.path") + BR);
		sb.append("加载库时搜索的路径列表：" + props.getProperty("java.library.path") + BR);
		sb.append("默认的临时文件路径：" + props.getProperty("java.io.tmpdir") + BR);
		sb.append("一个或多个扩展目录的路径：" + props.getProperty("java.ext.dirs") + BR);
		sb.append("操作系统的名称：" + props.getProperty("os.name") + BR);
		sb.append("操作系统的构架：" + props.getProperty("os.arch") + BR);
		sb.append("操作系统的版本：" + props.getProperty("os.version") + BR);
		sb.append("文件分隔符：" + props.getProperty("file.separator") + BR); // 在
																		// unix
																		// 系统中是＂／＂
		sb.append("路径分隔符：" + props.getProperty("path.separator") + BR); // 在
																		// unix
																		// 系统中是＂:＂
		sb.append("行分隔符：" + props.getProperty("line.separator") + BR); // 在
																		// unix系统中是＂/n＂
		sb.append("用户的账户名称：" + props.getProperty("user.name") + BR);
		sb.append("用户的主目录：" + props.getProperty("user.home") + BR);
		sb.append("用户的当前工作目录：" + props.getProperty("user.dir") + BR);
		return sb.toString();
	}

	public static String getMacAddress() {
		try {
			String result = null;
			InetAddress address = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(address);
			ni.getInetAddresses().nextElement().getAddress();
			byte[] mac = ni.getHardwareAddress();
			String sIP = address.getHostAddress();
			String sMAC = "";
			for (int i = 0; i < mac.length; i++) {
				sMAC = new Formatter().format(Locale.getDefault(), "%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : "").toString();
			}
			result = "IP：" + sIP + BR;
			result += "MAC：" + sMAC + BR;
			return result;
		} catch (Exception e) {
		}
		return null;
	}

	public static final String getServerInfo() {
		Map<String, String> map = System.getenv();
		String userName = map.get("USERNAME");// 获取用户名
		String computerName = map.get("COMPUTERNAME");// 获取计算机名
		String userDomain = map.get("USERDOMAIN");// 获取计算机域名
		return "用户名：" + userName + BR + "计算机名：" + computerName + BR + "计算机域名："
				+ userDomain + BR;
	}

	public static final String getIPInfo() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress().toString(); // 获取本机ip
			String hostName = addr.getHostName().toString(); // 获取本机计算机名称
			Properties props1 = System.getProperties();
			String result = "本机IP：" + ip + "\n本机名称:" + hostName + BR;
			result += "操作系统的名称：" + props1.getProperty("os.name") + BR;
			result += "操作系统的版本：" + props1.getProperty("os.version") + BR;
			return result;
		} catch (Exception e) {
		}
		return null;
	}

	public static final String getAllInformation() {
		return getServerInformation() + getIPInfo() + getMacAddress()
				+ getServerInfo() + getSystemProperties();
	}

}
