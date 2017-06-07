/**
 * NOTE: portions of this code (Base64 encoding/decoding) use code
 * licensed under the BSD agreement.
 * 
 * Licensed under Creative Commons Attribution 3.0 Unported license.
 * http://creativecommons.org/licenses/by/3.0/
 * You are free to copy, distribute and transmit the work, and 
 * to adapt the work.  You must attribute android-plist-parser 
 * to Free Beachler (http://www.freebeachler.com).
 * 
 * The Android PList parser (android-plist-parser) is distributed in 
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.
 */
package cn.sampson.android.xiandou.utils.plist.domain;


import cn.sampson.android.xiandou.utils.Base64;
import cn.sampson.android.xiandou.utils.plist.Stringer;

public class Data extends PListObject implements
		IPListSimpleObject<java.lang.String> {

	protected Stringer dataStringer;
	protected byte[] rawData;

	private static final long serialVersionUID = -3101592260075687323L;

	public Data() {
		setType(PListObjectType.DATA);
		dataStringer = new Stringer();
	}

	@Override
	public java.lang.String getValue() {
		return getValue(true);
	}

	public java.lang.String getValue(boolean decode) {
		dataStringer.newBuilder();
		if (decode) {
			return dataStringer.getBuilder()
					.append(new java.lang.String(Base64.decodeFast(rawData)))
					.toString();
		} else {
			return dataStringer.getBuilder().append(rawData).toString();
		}
	}

	@Override
	public void setValue(java.lang.String val) {
		setValue(val, true);
	}

	public void setValue(java.lang.String val, boolean encoded) {
		if (!encoded) {
			rawData = Base64.encodeToByte(val.getBytes(), false);
		} else {
			rawData = val.getBytes();
		}
	}

	public void setValue(byte[] val, boolean encoded) {
		if (!encoded) {
			rawData = Base64.encodeToByte(val, false);
		} else {
			rawData = val;
		}
	}

}