package net.multiphasicapps.npsdump;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * This represents a single node in the compact tree.
 *
 * @sicne 2018/02/17
 */
public final class Node
{
	/** Offset to this node. */
	protected final int offset;
	
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
	
	/** The size of the node. */
	protected final int nodesize;
	
	/** Sub-node offsets. */
	private final int[] _suboffsets;
	
	/**
	 * Parses the compacted data information.
	 *
	 * @param __nodes Owning nodes.
	 * @param __m The instrument method list.
	 * @param __t The profiled thread.
	 * @param __in The input data stream.
	 * @param __off Offset to this node.
	 * @param __ns The size of each node.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public Node(Nodes __nodes, List<InstrumentedMethod> __m,
		ProfiledThread __t, DataInputStream __in, int __off, int __ns)
		throws IOException, NullPointerException
	{
		if (__nodes == null || __m == null || __t == null || __in == null)
			throw new NullPointerException();
		
		this.offset = __off;
		
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
		
		// Long offset?
		boolean shortoff = __t.compactLength() <= 16777215;
		
		// Read in offsets
		int[] suboffsets = new int[numsubnodes];
		for (int i = 0; i < numsubnodes; i++)
			suboffsets[i] = (shortoff ? __readThree(__in) : __in.readInt());
		this._suboffsets = suboffsets;
		
		this.nodesize = __ns + (numsubnodes * (shortoff ? 3 : 4));
	}
	
	/**
	 * Dumps a single node.
	 *
	 * @param __compact Compact dump?
	 * @param __depth How deep is the tree?
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public void dump(boolean __compact, int __depth, PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException();
		
		// Build prefix
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < __depth; i++)
			sb.append("  ");
		String p = sb.toString();
		
		if (__compact)
		{
			__out.print(p);
			__out.println(this.method);
		}
		else
		{
			__out.printf(p + "Offset   : %d%n", this.offset);
			__out.printf(p + "Size     : %d%n", this.nodesize);
			__out.printf(p + "Method   : %s%n", this.method);
			__out.printf(p + "NumCalls : %d%n", this.numcalls);
			__out.printf(p + "Time0    : %d%n", this.timezero);
			__out.printf(p + "SelfTime0: %d%n", this.selftimezero);
			__out.printf(p + "Time1    : %d%n", this.timeone);
			__out.printf(p + "SelfTime1: %d%n", this.selftimeone);
			__out.printf(p + "NumSubNo.: %d%n", this.numsubnodes);
			
			__out.print(p + "SubNodes :");
			for (int off : this._suboffsets)
				__out.printf(" %d", off);
			__out.println();
		}
	}
	
	/**
	 * Returns the node size.
	 *
	 * @return The node size.
	 * @since 2018/02/17
	 */
	public int nodeSize()
	{
		return this.nodesize;
	}
	
	/**
	 * Returns the offsets to subnodes.
	 *
	 * @return The subnode offsets.
	 * @since 2018/02/17
	 */
	public int[] subNodeOffsets()
	{
		return this._suboffsets.clone();
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

