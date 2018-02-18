package net.multiphasicapps.npsdump;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This represents the flattened node tree.
 *
 * @since 2018/02/17
 */
public final class Nodes
	extends AbstractList<Node>
{
	/** Base size of each sub-node. */
	public static final int BASE_SIZE =
		2 + 4 + 5 + 5 + 2;
	
	/** Node size. */
	protected final int nodesize;
	
	/** Individual nodes. */
	protected final List<Node> nodes;
	
	/** Offsets to each node. */
	protected final Map<Integer, Node> offsets;
	
	/**
	 * Parses the compacted data information.
	 *
	 * @param __m The instrument method list.
	 * @param __t The profiled thread.
	 * @param __data Data buffer.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public Nodes(List<InstrumentedMethod> __m, ProfiledThread __t,
		byte[] __data)
		throws IOException, NullPointerException
	{
		if (__m == null || __t == null || __data == null)
			throw new NullPointerException();
		
		// The size of each individual node
		int datalen = __data.length,
			nodesize = BASE_SIZE +
				(__t.isMeasuringThreadTime() ? 10 : 0) +
				(datalen < 16777215 ? 3 : 4);
		this.nodesize = nodesize;
		
		DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(__data));
		
		List<Node> nodes = new ArrayList<>();
		Map<Integer, Node> offsets = new TreeMap<>();
		
		// Read each individual node
		for (int i = 0, n = datalen / nodesize, off = 0; i < n;
			i++, off += nodesize)
		{
			System.err.printf("READ %d of %d%n", i, n);
			Node e = new Node(this, __m, __t, in, off, nodesize);
			
			nodes.add(e);
			offsets.put(off, e);
		}
		
		this.nodes = (nodes = Collections.<Node>unmodifiableList(nodes));
		this.offsets = (offsets = Collections.<Integer, Node>unmodifiableMap(
			offsets));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/17
	 */
	@Override
	public Node get(int __i)
	{
		return this.nodes.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/17
	 */
	@Override
	public int size()
	{
		return this.nodes.size();
	}
}

