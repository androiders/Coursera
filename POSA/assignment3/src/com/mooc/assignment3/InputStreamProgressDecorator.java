package com.mooc.assignment3;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Decorator of an input stream to make it possible to report progress of the reading
 *
 */
public class InputStreamProgressDecorator extends FilterInputStream {

	//interface for reporting progress
	private ProgressObserver mPo;
	
	//the total number of bytes to read. Must be precalculated
	private int mMax;
	
	private int mCurrent;
	
	public InputStreamProgressDecorator(InputStream in, ProgressObserver po, int max) {
		super(in);
		mPo = po;
		mMax = max;
	}

	
	@Override
	public int read() throws IOException {	
		mCurrent++;
		mPo.update(mMax, mCurrent);
		return super.read();
	}

	@Override
	public int read(byte[] buffer, int byteOffset, int byteCount)
			throws IOException {
		mCurrent += byteCount;
		mPo.update(mMax, mCurrent);
		return super.read(buffer, byteOffset, byteCount);
	}


	
	
}
