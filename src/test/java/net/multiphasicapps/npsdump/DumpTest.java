package net.multiphasicapps.npsdump;

import java.io.InputStream;
import java.io.IOException;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Tests dumping of the NPS format
 *
 * @since 2018/02/17
 */
public class DumpTest
	extends TestCase
{
	/**
	 * Dumps the CPU test.
	 *
	 * @since 2018/02/17
	 */
	public void testCpu()
	{
		try (InputStream in = DumpTest.class.getResourceAsStream("cpu.nps"))
		{
			new Snapshot(in).dump(System.out);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Read failure.", e);
		}
	}
}

