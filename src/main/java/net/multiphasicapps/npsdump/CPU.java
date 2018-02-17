package net.multiphasicapps.npsdump;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * CPU snapshot data parser.
 *
 * @since 2018/02/17
 */
public final class CPU
	implements SnapshotData
{
	/**
	 * Parses the CPU snapshot data.
	 *
	 * @param __in Input data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public CPU(DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException();
		
		//throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/17
	 */
	@Override
	public void dump(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException();
		
		//throw new Error("TODO");
	}
}

