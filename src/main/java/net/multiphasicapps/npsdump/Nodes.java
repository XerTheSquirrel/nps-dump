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
				(__t.isMeasuringThreadTime() ? 10 : 0);
		this.nodesize = nodesize;
		
		DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(__data));
		
		List<Node> nodes = new ArrayList<>();
		Map<Integer, Node> offsets = new TreeMap<>();
		
		// Read each individual node
		for (int i = 0, off = 0; i >= 0 && off < datalen; i++)
		{
			// Read in node
			Node e = new Node(this, __m, __t, in, off, nodesize);
			
			// Add node
			nodes.add(e);
			offsets.put(off, e);
			
			// Determine offset of next node
			off += e.nodeSize();
		}
		
		this.nodes = (nodes = Collections.<Node>unmodifiableList(nodes));
		this.offsets = (offsets = Collections.<Integer, Node>unmodifiableMap(
			offsets));
	}
	
	/**
	 * Dumps the node tree.
	 *
	 * @param __depth The tree depth.
	 * @param __node The current node.
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public void dump(int __depth, Node __node, PrintStream __out)
		throws NullPointerException
	{
		if (__node == null || __out == null)
			throw new NullPointerException();
		
		// Print this node
		__node.dump(true, __depth, __out);
		
		// Print sub-nodes
		Map<Integer, Node> offsets = this.offsets;
		for (int snoff : __node.subNodeOffsets())
			this.dump(__depth + 1, offsets.get(snoff), __out);
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

