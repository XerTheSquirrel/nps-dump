package net.multiphasicapps.npsdump;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CPU snapshot data parser.
 *
 * @since 2018/02/17
 */
public final class CPU
	implements SnapshotData
{
	/** Version. */
	public static final int VERSION =
		1;
	
	/** Version. */
	protected final int version;
	
	/** Timestamp. */
	protected final LocalDateTime timestamp;
	
	/** Duration of the execution. */
	protected final long duration;
	
	/** Measure thread time? */
	protected final boolean measurethreadtime;
	
	/** Instrumented methods. */
	protected final List<InstrumentedMethod> imethods;
	
	/**
	 * Parses the CPU snapshot data.
	 *
	 * @param __in Input data.
	 * @throws InvalidSnapshotException If the snapshot is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public CPU(DataInputStream __in)
		throws InvalidSnapshotException, IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException();
		
		// Read version
		int version = __in.readInt();
		if (version != VERSION)
			throw new InvalidSnapshotException("Invalid version: " + version);
		this.version = version;
		
		// Read timestamp
		this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(
			__in.readLong()), ZoneId.of("UTC"));
		
		// Read duration
		this.duration = __in.readLong();
		
		// Measure thread time?
		this.measurethreadtime = __in.readBoolean();
		
		// Read instrumented methods
		List<InstrumentedMethod> imethods = new ArrayList<>();
		for (int i = 0, n = __in.readInt(); i < n; i++)
			imethods.add(new InstrumentedMethod(__in));
		this.imethods = (imethods = Collections.<InstrumentedMethod>
			unmodifiableList(imethods));
		
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
		
		// Header
		__out.printf("Version   : %s%n", this.version);
		__out.printf("Timestamp : %s%n", this.timestamp);
		__out.printf("Duration  : %sns%n", this.duration);
		__out.printf("MeasureTT?: %b%n", this.measurethreadtime);
		
		// Instrumented methods
		__out.printf("I. Methods:%n");
		List<InstrumentedMethod> imethods = this.imethods;
		for (int i = 0, n = imethods.size(); i < n; i++)
			__out.printf("  #%-4d %s%n", i, imethods.get(i));
		
		//throw new Error("TODO");
	}
}

