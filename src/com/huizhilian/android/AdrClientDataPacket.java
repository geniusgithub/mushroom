package com.huizhilian.android;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class AdrClientDataPacket {
	
	private String cmd = "";
	private short length;
	private int seqNo;
	private static final int headLength = 6;
	private String body;
	private String crc;
	private byte[]bytes;
	
	public AdrClientDataPacket(String cmd)
	{
		this.cmd = cmd;
	}
	
	public AdrClientDataPacket()
	{

	}
	
	public String getCMD()
	{
		return cmd;
	}
	
	public void read(byte[] values) throws BadDataPacketException, UnsupportedEncodingException
	{
		this.bytes=new byte[values.length-4];
		System.arraycopy(values, 0, bytes, 0, bytes.length);
		ByteBuffer buf=ByteBuffer.wrap(values);
		int len = buf.limit();
		if (len < 10) {			
			throw new BadDataPacketException("readable bytes is less than 10");
		}
		length = buf.getShort();
		if (buf.limit()-buf.position() < length - 2) {		
			throw new BadDataPacketException(
					"readable bytes is less than  the length value in head");
		}
		setSeqNo(buf.getInt());	
		byte[] bytes = new byte[length-4-6];
		buf.get(bytes);
		body = new String(bytes, "UTF-8");
		bytes = new byte[4];
		buf.get(bytes);
		crc = new String(bytes);
	}
	public byte[] write() throws UnsupportedEncodingException
	{
		
		length=(short) (6+body.getBytes("utf-8").length+4);		
		ByteBuffer buf = ByteBuffer.allocate(length);			
		buf.putShort(length);
		buf.putInt(seqNo);
		buf.put(body.getBytes("utf-8"));
		
		ByteBuffer buf1=buf.duplicate();
		buf1.flip();
		byte []bytes = new byte[buf1.limit()];
		buf1.get(bytes);
		crc = CRCCheck.crc16str(bytes, bytes.length);
		buf.put(crc.getBytes());
		return buf.array();
		
	}
	
	public void setLength(short length) {
		this.length = length;
	}
	public short getLength() {
		return length;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public static int getHeadlength() {
		return headLength;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBody() {
		return body;
	}
	public void setCrc(String crc) {
		this.crc = crc;
	}
	public String getCrc() {
		return crc;
	}	

	/**
	 * crc 校验
	 * 
	 * @return
	 */
	public boolean verify() {
		String ret = CRCCheck.crc16str(bytes, bytes.length);
		return ret.equals(crc);
	}
}
