package net.multiphasicapps.npsdump;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.zip.InflaterInputStream;

/**
 * This class represents the base for a snapshot.
 *
 * @since 2018/02/17
 */
public final class Snapshot
{
	/** Magic number for the snapshot. */
	public static final String MAGIC_NUMBER =
		"nBpRoFiLeR";
	
	/** Major version. */
	public static final int MAJOR_VERSION =
		1;
	
	/** Minor version. */
	public static final int MINOR_VERSION =
		2;
	
	/** CPU snapshot type. */
	public static final int TYPE_CPU =
		1;
	
	/** Snapshot data. */
	protected final SnapshotData data;
	
	/** Magic number. */
	protected final String magic;
	
	/** Major version. */
	protected final int majver;
	
	/** Minor version. */
	protected final int minver;
	
	/** Properties data. */
	protected final String properties;
	
	/** Comment. */
	protected final String comment;
	
	/** Compressed length. */
	protected final int complen;
	
	/** Uncompressed length. */
	protected final int uncomplen;
	
	/** Properties length. */
	protected final int proplen;
	
	/**
	 * Initializes the snapshot by parsing the given input.
	 *
	 * @param __data Snapshot data.
	 * @param __c Snapshot comment.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public Snapshot(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException();
		
		DataInputStream in = new DataInputStream(__in);
		
		// Read magic number
		byte[] magic = new byte[MAGIC_NUMBER.length()];
		in.readFully(magic);
		String smagic = new String(magic, "ascii");
		if (!MAGIC_NUMBER.equals(smagic))
			throw new InvalidSnapshotException("Invalid magic: " + smagic);
		this.magic = smagic;
		
		// Major version
		int majver = in.readByte();
		if (majver != MAJOR_VERSION)
			throw new InvalidSnapshotException("Invalid major: " + majver);
		this.majver = majver;
			
		// Minor version
		int minver = in.readByte();
		if (minver != MINOR_VERSION)
			throw new InvalidSnapshotException("Invalid minor: " + minver);
		this.minver = minver;
		
		// Read type
		int type = in.readInt();
		
		// Read data length
		int complen = in.readInt();
		int uncomplen = in.readInt();
		this.complen = complen;
		this.uncomplen = uncomplen;
		
		// Read in data
		byte[] compdata = new byte[complen];
		in.readFully(compdata);
		
		// Parse data depending on the type information
		SnapshotData sdata;
		try (ByteArrayInputStream bain = new ByteArrayInputStream(compdata);
			InflaterInputStream iis = new InflaterInputStream(bain);
			DataInputStream din = new DataInputStream(iis))
		{
			switch (type)
			{
				case TYPE_CPU:
					sdata = new CPU(din);
					break;
			
				default:
					throw new InvalidSnapshotException("Invalid type: " +
						type);
			}
		}
		this.data = sdata;
		
		// Read properties data
		int proplen = in.readInt();
		byte[] propdata = new byte[proplen];
		in.readFully(propdata);
		this.proplen = proplen;
		this.properties = new String(propdata);
		
		// Read comment
		this.comment = in.readUTF();
	}
	
	/**
	 * Dumps the snapshot information to the given stream.
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
		
		// Header information
		__out.printf("Magic     : %s%n", this.magic);
		__out.printf("Version   : %d.%d%n", this.majver, this.minver);
		__out.printf("Comment   : %s%n", this.comment);
		
		// Data
		__out.printf("Data      : (%d bytes in %d bytes)%n",
			this.uncomplen, this.complen);
		this.data.dump(__out);
		__out.println("-----------");
		
		// Properties
		__out.printf("Properties: (%d bytes)%n", this.proplen);
		__out.println(this.properties);
		__out.println("-----------");
	}
}

