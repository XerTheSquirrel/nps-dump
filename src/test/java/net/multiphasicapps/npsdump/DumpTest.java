package net.multiphasicapps.npsdump;

import java.io.InputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

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
	@Test
	public void testCpu()
	{
		try (InputStream in = DumpTest.class.getResourceAsStream("cpu.nps"))
		{
			Snapshot.parseSnapshot(in).dump(System.out);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Read failure.", e);
		}
	}
}

