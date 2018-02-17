package net.multiphasicapps.npsdump;

/**
 * This is thrown when the snapshot is not valid.
 *
 * @since 2018/02/17
 */
public class InvalidSnapshotException
	extends RuntimeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/02/17
	 */
	public InvalidSnapshotException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/02/17
	 */
	public InvalidSnapshotException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/02/17
	 */
	public InvalidSnapshotException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/02/17
	 */
	public InvalidSnapshotException(Throwable __c)
	{
		super(__c);
	}
}
