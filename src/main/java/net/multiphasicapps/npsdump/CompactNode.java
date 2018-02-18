package net.multiphasicapps.npsdump;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * This represents a single node in the compact tree.
 *
 * @sicne 2018/02/17
 */
public final class CompactNode
{
	/** Method used. */
	protected final InstrumentedMethod method;
	
	/** Number of calls. */
	protected final long numcalls;
	
	/** Zero time. */
	protected final long timezero;
	
	/** Self time zero. */
	protected final long selftimezero;
	
	/** Time 1, measured thread time. */
	protected final long timeone;
	
	/** Self time 1, measured thread time. */
	protected final long selftimeone;
	
	/** The number of sub nodes. */
	protected final int numsubnodes;
	
	/** Sub-node offset. */
	protected final int subnodeoffset;
	
	/**
	 * Parses the compacted data information.
	 *
	 * @param __m The instrument method list.
	 * @param __t The profiled thread.
	 * @param __in The input data stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public CompactNode(List<InstrumentedMethod> __m, ProfiledThread __t,
		DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__m == null || __t == null || __in == null)
			throw new NullPointerException();
		
		int method = __in.readUnsignedShort();
		this.method = (method >= 0 && method < __m.size() ? __m.get(method) :
			null);
		this.numcalls = Integer.toUnsignedLong(__in.readInt());
		this.timezero = __readFive(__in);
		this.selftimezero = __readFive(__in);
		
		// These are optional
		if (__t.isMeasuringThreadTime())
		{
			this.timeone = __readFive(__in);
			this.selftimeone = __readFive(__in);
		}
		else
		{
			this.timeone = -1;
			this.selftimeone = -1;
		}
		
		// Read sub-node count
		int numsubnodes = __in.readUnsignedShort();
		this.numsubnodes = numsubnodes;
		
		// Subnode offset
		if (__t.compactLength() < 16777215)
			this.subnodeoffset = __readThree(__in);
		else
			this.subnodeoffset = __in.readInt();
	}
	
	/**
	 * Dumps the snapshot information to the given stream.
	 *
	 * @param __depth How deep is the tree?
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public void dump(int __depth, PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException();
		
		// Build prefix
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < __depth; i++)
			sb.append("  ");
		String p = sb.toString();
		
		// Dump
		__out.printf(p + "Method   : %s%n", this.method);
		__out.printf(p + "NumCalls : %d%n", this.numcalls);
		__out.printf(p + "Time0    : %d%n", this.timezero);
		__out.printf(p + "SelfTime0: %d%n", this.selftimezero);
		__out.printf(p + "Time1    : %d%n", this.timeone);
		__out.printf(p + "SelfTime1: %d%n", this.selftimeone);
		__out.printf(p + "NumSubNo.: %d%n", this.numsubnodes);
		__out.printf(p + "NodeOff  : %d%n", this.subnodeoffset);
	}
	
	/**
	 * Reads a 5-sized integer.
	 *
	 * @param __in The stream to read from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	private static final long __readFive(DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException();
		
		return (__in.readUnsignedByte() << 32) |
			(__in.readUnsignedByte() << 24) |
			(__in.readUnsignedByte() << 16) |
			(__in.readUnsignedByte() << 8) |
			__in.readUnsignedByte();
	}
	
	/**
	 * Reads a 3-sized integer.
	 *
	 * @param __in The stream to read from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	private static final int __readThree(DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException();
		
		return (__in.readUnsignedByte() << 16) |
			(__in.readUnsignedByte() << 8) |
			__in.readUnsignedByte();
	}
}

