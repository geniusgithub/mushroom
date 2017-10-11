//package com.huizhilian.android;
//
//public class MainTest {
//	public static void main(String[] args) {
//		System.setProperty("client.ip", "127.0.0.1");
//		System.setProperty("client.port","20001");
//		Receiver receiver = new Receiver(Caller.getInstance());
//		receiver.start();
//
//		for (int i = 0; i < 1; i++)
//			new Thread() {
//				public void run() {
//					try {
//						long time = System.currentTimeMillis();
//						Device device = new Device();
//						for (int i = 0; i < 1; i++) {
//
//							// System.out.println(device.keyset("3",
//							// "13761237652",
//							// "李波12", "2",
//							// "13411111111", "Peter1"));
//							//
//							// System.out.println(device.getKeyset());
//							//
//							// System.out.println(device.calltime(22));
//							// System.out.println(device.getCalltime());
//							//
//							// System.out.println(device.whitelistset("13761237652",
//							// "李波12", "13411111111", "Peter1"));
//							// System.out.println(device.getWhitelist());
//							//
//							// System.out.println(device.scenemode("shake"));
//							// System.out.println(device.getSceneMode());
//							//
//							// System.out.println(device.powerset("sleep"));
//							// System.out.println(device.getPowerstatus());
//							// try {
//							// System.out.println(device.clockset("1221", 1,
//							// "1,2,3,4", 1, "0121", 1, "6,2,3,4", 1));
//							// System.out.println(device.getClock());
//							// } catch (Exception ex) {
//							//
//							// }
//							//
//							// try {
//							// System.out.println(device.lowpower(1));
//							// System.out.println(device.getLowpower());
//							// } catch (Exception ex) {
//							//
//							// }
//							//
//							// try {
//							// System.out.println(device.gpsinterval(21));
//							// System.out.println(device.getGpsinterval());
//							// } catch (Exception ex) {
//							//
//							// }
//							//
//							// try {
//							// System.out.println(device.gpstimetable("1,2,3,4","1111","1232"));
//							// System.out.println(device.getGpstimetable());
//							// } catch (Exception ex) {
//							//
//							// }
//							//
//							// try {
//							// System.out.println(device.addarea("permit",
//							// 11.1,
//							// 11.2, 123, "学校222"));
//							// System.out.println(device.delArea(1));
//							// System.out.println(device.modifyArea(1,
//							// "forbid", 99.1, 88.2, 33, "学校XXXX"));
//							// System.out.println(device.getArea("permit"));
//							// } catch (Exception ex) {
//							//
//							// }
//
//							User user = new User();
//							// 登录
//							// user.login("peter", "adfe2!");
//							// 注册
//							// System.out.println(user.register("peter",
//							// "sdfasd", "13111111111", "email@gmail.com",
//							// "张三"));
//							System.out.println(user.login("peter", "sdfasd"));
//							System.out.println(user.logout());
//						}
//						System.out
//								.println((System.currentTimeMillis() - time) / 1000.0);
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//					System.exit(1);
//				}
//			}.start();
//
//	}
//}
