package net.multiphasicapps.npsdump;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Profiled thread information.
 *
 * @since 2018/02/17
 */
public final class ProfiledThread
{
	/** Thread information. */
	protected final int id;
	
	/** Name of thread. */
	protected final String name;
	
	/** Measuring thread time? */
	protected final boolean measurethreadtime;
	
	/** Node size. */
	protected final int nodesize;
	
	/** Whole graph time absolute time?. */
	protected final long wholegraphtimeabs;
	
	/** Whole graph time thread CPU time? */
	protected final long wholegraphtimethreadcpu;
	
	/** Time spent in injected code. */
	protected final double timeininjectedabscounts;
	
	/** Time spent in inhected code by cpu count. */
	protected final double timeininjectedthreadcpucounts;
	
	/** Whole graph pure time absolute. */
	protected final long wholegraphpuretimeabs;
	
	/** Whole graph pure time thread cpu. */
	protected final long wholegraphpuretimethreadcpu;
	
	/** Whole graph time net time zero. */
	protected final long wholegraphnettimezero;
	
	/** Whole graph time net time one. */
	protected final long wholegraphnettimeone;
	
	/** Total invocation number. */
	protected final long totalinvnumber;
	
	/** Display whole thread CPU time? */
	protected final boolean displaywholethreadcputime;
	
	/** Compact length data. */
	protected final int compactlen;
	
	/** Nodes within the tree. */
	protected final Nodes nodes;
	
	/**
	 * Parses the thread information.
	 *
	 * @param __m Instrumented methods.
	 * @param __in The data to parse.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public ProfiledThread(List<InstrumentedMethod> __m, DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__m == null || __in == null)
			throw new NullPointerException();
		
		// Read basic thread information
		this.id = __in.readInt();
		this.name = __in.readUTF();
		this.measurethreadtime = __in.readBoolean();
		
		// Parse compact data later
		int compactlen = __in.readInt();
		byte[] compact = new byte[compactlen];
		__in.readFully(compact);
		
		// Node size
		int nodesize;
		this.compactlen = compactlen;
		this.nodesize = (nodesize = __in.readInt());
		this.wholegraphtimeabs = __in.readLong();
		this.wholegraphtimethreadcpu = __in.readLong();
		this.timeininjectedabscounts = __in.readDouble();
		this.timeininjectedthreadcpucounts = __in.readDouble();
		this.wholegraphpuretimeabs = __in.readLong();
		this.wholegraphpuretimethreadcpu = __in.readLong();
		this.wholegraphnettimezero = __in.readLong();
		this.wholegraphnettimeone = __in.readLong();
		this.totalinvnumber = __in.readLong();
		this.displaywholethreadcputime = __in.readBoolean();
		this.nodes = new Nodes(__m, this, compact);
	}
	
	/**
	 * Returns the length of the compact data.
	 *
	 * @return The compact data length.
	 * @since 2018/02/17
	 */
	public int compactLength()
	{
		return this.compactlen;
	}
	
	/**
	 * Dumps the snapshot information to the given stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public void dump(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException();
		
		__out.printf("ID          : %d%n", this.id);
		__out.printf("Name        : %s%n", this.name);
		__out.printf("Measure TT? : %b%n", this.measurethreadtime);
		__out.printf("Node Size   : %d%n", this.nodesize);
		__out.printf("WG Gross Abs: %d%n", this.wholegraphtimeabs);
		__out.printf("WG Gross Thr: %d%n", this.wholegraphtimethreadcpu);
		__out.printf("TIIC Abs Cnt: %g%n", this.timeininjectedabscounts);
		__out.printf("TIIC Abs Thr: %g%n", this.timeininjectedthreadcpucounts);
		__out.printf("WG Pure Abs : %d%n", this.wholegraphpuretimeabs);
		__out.printf("WG Pure Thr : %d%n", this.wholegraphpuretimethreadcpu);
		__out.printf("WG Time Zero: %d%n", this.wholegraphnettimezero);
		__out.printf("WG Time One : %d%n", this.wholegraphnettimeone);
		__out.printf("Total Inv # : %d%n", this.totalinvnumber);
		__out.printf("DWholeTCT?  : %b%n", this.displaywholethreadcputime);
		
		__out.printf("Compact Len.: %d bytes%n", this.compactlen);
		
		// Print node list
		__out.printf("Single Nodes:%n");
		Nodes nodes = this.nodes;
		for (int i = 0, n = nodes.size(); i < n; i++)
		{
			__out.printf("  #%-3d%n", i);
			nodes.get(i).dump(false, 2, __out);
		}
		__out.printf("-------------%n");
		
		// Print node tree
		if (nodes.size() > 0)
		{
			__out.printf("Node Tree   :%n");
			//nodes.dump(1, nodes.get(0), __out);
			__out.printf("-------------%n");
		}
	}
	
	/**
	 * Is thread time being measured?
	 *
	 * @return If it is being measured.
	 * @since 2018/02/17
	 */
	public boolean isMeasuringThreadTime()
	{
		return this.measurethreadtime;
	}
	
	/**
	 * Returns the size of nodes.
	 *
	 * @return The node size.
	 * @since 2018/02/17
	 */
	public int nodeSize()
	{
		return this.nodesize;
	}
}

