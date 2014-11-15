// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

final class RSImageProducer
		implements ImageProducer, ImageObserver
{

	public RSImageProducer(int i, int j, Component component)
	{
		width = i;
		height = j;
		myPixels = new int[i * j];
		depthBuffer = new float[i * j];
		colorModel = new DirectColorModel(32, 0xff0000, 65280, 255);
		image = component.createImage(this);
		update();
		component.prepareImage(image, this);
		update();
		component.prepareImage(image, this);
		update();
		component.prepareImage(image, this);
		initDrawingArea();
	}

	public void initDrawingArea()
	{
		DrawingArea.initDrawingArea(height, width, myPixels, depthBuffer);
	}

	public void drawGraphics(int i, Graphics g, int k)
	{
		update();
		g.drawImage(image, k, i, this);
		//resetDepthBuffer();
	}
	
	public void resetDepthBuffer() {
        if (depthBuffer == null) {
            return;
        }
        int length = depthBuffer.length;
        int loops = length - (length & 0x7);
        int position = 0;
        while (position < loops) {
            depthBuffer[position++] = 2.14748365E9F;
            depthBuffer[position++] = 2.14748365E9F;
            depthBuffer[position++] = 2.14748365E9F;
            depthBuffer[position++] = 2.14748365E9F;
            depthBuffer[position++] = 2.14748365E9F;
            depthBuffer[position++] = 2.14748365E9F;
            depthBuffer[position++] = 2.14748365E9F;
            depthBuffer[position++] = 2.14748365E9F;
        }
        while (position < length) {
            depthBuffer[position++] = 2.14748365E9F;
        }
    }

	public synchronized void addConsumer(ImageConsumer imageconsumer)
	{
		imageConsumer = imageconsumer;
		imageconsumer.setDimensions(width, height);
		imageconsumer.setProperties(null);
		imageconsumer.setColorModel(colorModel);
		imageconsumer.setHints(14);
	}

	public synchronized boolean isConsumer(ImageConsumer imageconsumer)
	{
		return imageConsumer == imageconsumer;
	}

	public synchronized void removeConsumer(ImageConsumer imageconsumer)
	{
		if(imageConsumer == imageconsumer)
			imageConsumer = null;
	}

	public void startProduction(ImageConsumer imageconsumer)
	{
		addConsumer(imageconsumer);
	}

	public void requestTopDownLeftRightResend(ImageConsumer imageconsumer)
	{
		System.out.println("TDLR");
	}

	private synchronized void update()
	{
		if(imageConsumer != null)
		{
			imageConsumer.setPixels(0, 0, width, height, colorModel, myPixels, 0, width);
			imageConsumer.imageComplete(2);
		}
	}

	public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1)
	{
		return true;
	}

	public final int[] myPixels;
	public final float[] depthBuffer;
	public final int width;
	public final int height;
	private final ColorModel colorModel;
	private ImageConsumer imageConsumer;
	private final Image image;
}
