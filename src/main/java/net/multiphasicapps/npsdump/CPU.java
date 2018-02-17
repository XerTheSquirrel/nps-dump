package net.multiphasicapps.npsdump;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
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
	
	/** Profiled threads. */
	protected final List<ProfiledThread> pthreads;
	
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
		
		// Read thread information
		List<ProfiledThread> pthreads = new ArrayList<>();
		for (int i = 0, n = __in.readInt(); i < n; i++)
			pthreads.add(new ProfiledThread(imethods, __in));
		this.pthreads = (pthreads = Collections.<ProfiledThread>
			unmodifiableList(pthreads));
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
		
		// Threads
		__out.printf("P. Threads:%n");
		List<ProfiledThread> pthreads = this.pthreads;
		for (int i = 0, n = pthreads.size(); i < n; i++)
		{
			__out.printf("  Thread %d%n", i);
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos, true))
			{
				// Write data
				pthreads.get(i).dump(ps);
				ps.flush();
			
				// Parse lines
				try (BufferedReader br = new BufferedReader(
					new InputStreamReader(
					new ByteArrayInputStream(baos.toByteArray()))))
				{
					for (;;)
					{
						String ln = br.readLine();
					
						if (ln == null)
							break;
					
						__out.print("    ");
						__out.println(ln);
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace(__out);
			}
		}
	}
}

