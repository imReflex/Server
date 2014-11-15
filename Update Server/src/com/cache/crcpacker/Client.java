package com.cache.crcpacker;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Client {

	
	
	public static void main(String args[])
	{
		new Client();
	}
	public Decompressor[] cacheIndices;
	public Client() 
	{
		SignLink.storeid = 32;
		try {
			SignLink.startpriv(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		cacheIndices = new Decompressor[7];
		startup();
	}
	public void startup()
	{
		if (SignLink.cache_dat != null) {
			for (int i = 0; i <= 6; i++)
				cacheIndices[i] = new Decompressor(SignLink.cache_dat, SignLink.cache_idx[i], i + 1);
		}
		OnDemandFetcher onDemandFetcher = new OnDemandFetcher();
		onDemandFetcher.start(this);
	}
	
}
