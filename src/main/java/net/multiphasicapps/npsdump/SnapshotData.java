package net.multiphasicapps.npsdump;

import java.io.PrintStream;

/**
 * Base interface for snapshot storage information.
 *
 * @since 2018/02/17
 */
public interface SnapshotData
{
	/**
	 * Dumps the snapshot information to the given stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public abstract void dump(PrintStream __out)
		throws NullPointerException;
}

