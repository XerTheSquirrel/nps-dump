package net.multiphasicapps.npsdump;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This is the main entry point for the dumper.
 *
 * @since 2018/02/17
 */
public class Main
{
	/**
	 * Initializes the dumper.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On null arguments.
	 * @sicne 2018/02/17
	 */
	public static void main(String... __args)
		throws IOException
	{
		if (__args == null)
			__args = new String[0];
		
		// Parse all files
		for (String a : __args)
			try (InputStream in = Files.newInputStream(
				Paths.get(a), StandardOpenOption.READ))
			{
				new Snapshot(in).dump(System.out);
			}
	}
}
