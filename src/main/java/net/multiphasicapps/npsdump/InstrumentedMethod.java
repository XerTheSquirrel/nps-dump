package net.multiphasicapps.npsdump;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents an instrumented method.
 *
 * @since 2018/02/17
 */
public final class InstrumentedMethod
{
	/** Class name. */
	protected final String classname;
	
	/** Method name. */
	protected final String methodname;
	
	/** Signature. */
	protected final String signature;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the instrumented method.
	 *
	 * @param __in The input stream data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/17
	 */
	public InstrumentedMethod(DataInputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException();
		
		this.classname = __in.readUTF();
		this.methodname = __in.readUTF();
		this.signature = __in.readUTF();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/17
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = String.format("%s.%s%s", this.classname, this.methodname,
					this.signature)));
		
		return rv;
	}
}
