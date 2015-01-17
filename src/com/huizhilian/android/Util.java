package com.huizhilian.android;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Util {

	public static void close(Object... objs) {
		for (Object c : objs) {
			if (c instanceof Closeable) {
				try {
					((Closeable) c).close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			if (c != null) {
				Method[] methods = c.getClass().getMethods();
				for (Method method : methods) {
					if (method.getName().equals("close")) {
						try {
							method.invoke(c);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				}
				c = null;
			}
		}
	}
	
	public static String leftPad(String value, int i) {
		if (value == null) {
			byte[] bytes = new byte[i];
			return new String(bytes);
		}
		int length = value.getBytes().length;
		if (length == i)
			return value;
		else if (length > i) {
			return value.substring(0, i);
		} else {
			byte[] bytes = new byte[i];
			System.arraycopy(value.getBytes(), 0, bytes, i - length, length);
			return new String(bytes);
		}
	}
	public static void main(String[] args) {
		int id = 1111111111;
		String id2 = String .format("%010d", id);  //前导置零，10位长度的字符串
		System.out.println(id2);

	}
}
