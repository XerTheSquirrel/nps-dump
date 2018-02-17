package net.multiphasicapps.npsdump;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This class represents the base for a snapshot.
 *
 * @since 2018/02/17
 */
public abstract class Snapshot
{
	/**
	 * Initializes the snapshot.
	 *
	 * @since 2018/02/17
	 */
	Snapshot()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Dumps the snapshot to the given stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public final void dump(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException();
		
		throw new Error("TODO");
	}
	
	/**
	 * Parses the specified snapshot.
	 *
	 * @param __in The snapshot to read from.
	 * @return The parsed snapshot.
	 * @throws InvalidSnapshotException If the snapshot is not valid.
	 * @throws IOException If the stream could not be read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public static Snapshot parseSnapshot(InputStream __in)
		throws InvalidSnapshotException, IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException();
		
		DataInputStream in = new DataInputStream(__in);
		
		throw new Error("TODO");
	}
}

