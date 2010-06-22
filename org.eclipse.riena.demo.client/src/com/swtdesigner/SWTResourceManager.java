/**
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2003 - 2005, Instantiations, Inc. <br>
 * All Rights Reserved
 */
package com.swtdesigner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class for managing OS resources associated with SWT controls such as
 * colors, fonts, images, etc.
 * <p>
 * !!! IMPORTANT !!! Application code must explicitly invoke the
 * <code>dispose()</code> method to release the operating system resources
 * managed by cached objects when those objects and OS resources are no longer
 * needed (e.g. on application shutdown)
 * <p>
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2003 - 2007, Instantiations, Inc. <br>
 * All Rights Reserved
 * 
 * @author scheglov_ke
 * @author Dan Rubel
 */
@SuppressWarnings("unchecked")
public class SWTResourceManager {
	////////////////////////////////////////////////////////////////////////////
	//
	// Color
	//
	////////////////////////////////////////////////////////////////////////////
	private static Map/* <RGB,Color> */m_colorMap = new HashMap();

	/**
	 * Returns the system {@link Color} matching the specific ID.
	 * 
	 * @param systemColorID
	 *            the ID value for the color
	 * @return the system {@link Color} matching the specific ID
	 */
	public static Color getColor(final int systemColorID) {
		final Display display = Display.getCurrent();
		return display.getSystemColor(systemColorID);
	}

	/**
	 * Returns a {@link Color} given its red, green and blue component values.
	 * 
	 * @param r
	 *            the red component of the color
	 * @param g
	 *            the green component of the color
	 * @param b
	 *            the blue component of the color
	 * @return the {@link Color} matching the given red, green and blue
	 *         component values
	 */
	public static Color getColor(final int r, final int g, final int b) {
		return getColor(new RGB(r, g, b));
	}

	/**
	 * Returns a {@link Color} given its RGB value.
	 * 
	 * @param rgb
	 *            the {@link RGB} value of the color
	 * @return the {@link Color} matching the RGB value
	 */
	public static Color getColor(final RGB rgb) {
		Color color = (Color) m_colorMap.get(rgb);
		if (color == null) {
			final Display display = Display.getCurrent();
			color = new Color(display, rgb);
			m_colorMap.put(rgb, color);
		}
		return color;
	}

	/**
	 * Dispose of all the cached {@link Color}'s.
	 */
	public static void disposeColors() {
		for (final Iterator I = m_colorMap.values().iterator(); I.hasNext();) {
			((Color) I.next()).dispose();
		}
		m_colorMap.clear();
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Image
	//
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Maps image paths to images.
	 */
	private static Map/* <String,Image> */m_imageMap = new HashMap();

	/**
	 * Returns an {@link Image} encoded by the specified {@link InputStream}.
	 * 
	 * @param stream
	 *            the {@link InputStream} encoding the image data
	 * @return the {@link Image} encoded by the specified input stream
	 */
	protected static Image getImage(final InputStream stream) throws IOException {
		try {
			final Display display = Display.getCurrent();
			final ImageData data = new ImageData(stream);
			if (data.transparentPixel > 0) {
				return new Image(display, data, data.getTransparencyMask());
			}
			return new Image(display, data);
		} finally {
			stream.close();
		}
	}

	/**
	 * Returns an {@link Image} stored in the file at the specified path.
	 * 
	 * @param path
	 *            the path to the image file
	 * @return the {@link Image} stored in the file at the specified path
	 */
	public static Image getImage(final String path) {
		Image image = (Image) m_imageMap.get(path);
		if (image == null) {
			try {
				image = getImage(new FileInputStream(path));
				m_imageMap.put(path, image);
			} catch (final Exception e) {
				image = getMissingImage();
				m_imageMap.put(path, image);
			}
		}
		return image;
	}

	/**
	 * Returns an {@link Image} stored in the file at the specified path
	 * relative to the specified class.
	 * 
	 * @param clazz
	 *            the {@link Class} relative to which to find the image
	 * @param path
	 *            the path to the image file, if starts with <code>'/'</code>
	 * @return the {@link Image} stored in the file at the specified path
	 */
	public static Image getImage(final Class clazz, final String path) {
		final String key = clazz.getName() + '|' + path;
		Image image = (Image) m_imageMap.get(key);
		if (image == null) {
			try {
				image = getImage(clazz.getResourceAsStream(path));
				m_imageMap.put(key, image);
			} catch (final Exception e) {
				image = getMissingImage();
				m_imageMap.put(key, image);
			}
		}
		return image;
	}

	private static final int MISSING_IMAGE_SIZE = 10;

	/**
	 * @return the small {@link Image} that can be used as placeholder for
	 *         missing image.
	 */
	private static Image getMissingImage() {
		final Image image = new Image(Display.getCurrent(), MISSING_IMAGE_SIZE, MISSING_IMAGE_SIZE);
		//
		final GC gc = new GC(image);
		gc.setBackground(getColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, MISSING_IMAGE_SIZE, MISSING_IMAGE_SIZE);
		gc.dispose();
		//
		return image;
	}

	/**
	 * Style constant for placing decorator image in top left corner of base
	 * image.
	 */
	public static final int TOP_LEFT = 1;
	/**
	 * Style constant for placing decorator image in top right corner of base
	 * image.
	 */
	public static final int TOP_RIGHT = 2;
	/**
	 * Style constant for placing decorator image in bottom left corner of base
	 * image.
	 */
	public static final int BOTTOM_LEFT = 3;
	/**
	 * Style constant for placing decorator image in bottom right corner of base
	 * image.
	 */
	public static final int BOTTOM_RIGHT = 4;
	/**
	 * Internal value.
	 */
	protected static final int LAST_CORNER_KEY = 5;
	/**
	 * Maps images to decorated images.
	 */
	private static Map[]/* <Image,Map<Image,Image>> */m_decoratedImageMap = new Map[LAST_CORNER_KEY];

	/**
	 * Returns an {@link Image} composed of a base image decorated by another
	 * image.
	 * 
	 * @param baseImage
	 *            the base {@link Image} that should be decorated
	 * @param decorator
	 *            the {@link Image} to decorate the base image
	 * @return {@link Image} The resulting decorated image
	 */
	public static Image decorateImage(final Image baseImage, final Image decorator) {
		return decorateImage(baseImage, decorator, BOTTOM_RIGHT);
	}

	/**
	 * Returns an {@link Image} composed of a base image decorated by another
	 * image.
	 * 
	 * @param baseImage
	 *            the base {@link Image} that should be decorated
	 * @param decorator
	 *            the {@link Image} to decorate the base image
	 * @param corner
	 *            the corner to place decorator image
	 * @return the resulting decorated {@link Image}
	 */
	public static Image decorateImage(final Image baseImage, final Image decorator, final int corner) {
		if (corner <= 0 || corner >= LAST_CORNER_KEY) {
			throw new IllegalArgumentException("Wrong decorate corner"); //$NON-NLS-1$
		}
		Map cornerDecoratedImageMap = m_decoratedImageMap[corner];
		if (cornerDecoratedImageMap == null) {
			cornerDecoratedImageMap = new HashMap();
			m_decoratedImageMap[corner] = cornerDecoratedImageMap;
		}
		Map decoratedMap = (Map) cornerDecoratedImageMap.get(baseImage);
		if (decoratedMap == null) {
			decoratedMap = new HashMap();
			cornerDecoratedImageMap.put(baseImage, decoratedMap);
		}
		//
		Image result = (Image) decoratedMap.get(decorator);
		if (result == null) {
			final Rectangle bib = baseImage.getBounds();
			final Rectangle dib = decorator.getBounds();
			//
			result = new Image(Display.getCurrent(), bib.width, bib.height);
			//
			final GC gc = new GC(result);
			gc.drawImage(baseImage, 0, 0);
			if (corner == TOP_LEFT) {
				gc.drawImage(decorator, 0, 0);
			} else if (corner == TOP_RIGHT) {
				gc.drawImage(decorator, bib.width - dib.width, 0);
			} else if (corner == BOTTOM_LEFT) {
				gc.drawImage(decorator, 0, bib.height - dib.height);
			} else if (corner == BOTTOM_RIGHT) {
				gc.drawImage(decorator, bib.width - dib.width, bib.height - dib.height);
			}
			gc.dispose();
			//
			decoratedMap.put(decorator, result);
		}
		return result;
	}

	/**
	 * Dispose all of the cached {@link Image}'s.
	 */
	public static void disposeImages() {
		// dispose loaded images
		{
			for (final Iterator I = m_imageMap.values().iterator(); I.hasNext();) {
				((Image) I.next()).dispose();
			}
			m_imageMap.clear();
		}
		// dispose decorated images
		for (final Map cornerDecoratedImageMap : m_decoratedImageMap) {
			if (cornerDecoratedImageMap != null) {
				for (final Iterator I = cornerDecoratedImageMap.values().iterator(); I.hasNext();) {
					final Map decoratedMap = (Map) I.next();
					for (final Iterator J = decoratedMap.values().iterator(); J.hasNext();) {
						final Image image = (Image) J.next();
						image.dispose();
					}
					decoratedMap.clear();
				}
				cornerDecoratedImageMap.clear();
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Font
	//
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Maps font names to fonts.
	 */
	private static Map/* <String,Font> */m_fontMap = new HashMap();
	/**
	 * Maps fonts to their bold versions.
	 */
	private static Map/* <Font,Font> */m_fontToBoldFontMap = new HashMap();

	/**
	 * Returns a {@link Font} based on its name, height and style.
	 * 
	 * @param name
	 *            the name of the font
	 * @param height
	 *            the height of the font
	 * @param style
	 *            the style of the font
	 * @return {@link Font} The font matching the name, height and style
	 */
	public static Font getFont(final String name, final int height, final int style) {
		return getFont(name, height, style, false, false);
	}

	/**
	 * Returns a {@link Font} based on its name, height and style.
	 * Windows-specific strikeout and underline flags are also supported.
	 * 
	 * @param name
	 *            the name of the font
	 * @param size
	 *            the size of the font
	 * @param style
	 *            the style of the font
	 * @param strikeout
	 *            the strikeout flag (warning: Windows only)
	 * @param underline
	 *            the underline flag (warning: Windows only)
	 * @return {@link Font} The font matching the name, height, style, strikeout
	 *         and underline
	 */
	public static Font getFont(final String name, final int size, final int style, final boolean strikeout,
			final boolean underline) {
		final String fontName = name + '|' + size + '|' + style + '|' + strikeout + '|' + underline;
		Font font = (Font) m_fontMap.get(fontName);
		if (font == null) {
			final FontData fontData = new FontData(name, size, style);
			if (strikeout || underline) {
				try {
					final Class logFontClass = Class.forName("org.eclipse.swt.internal.win32.LOGFONT"); //$NON-NLS-1$
					final Object logFont = FontData.class.getField("data").get(fontData); //$NON-NLS-1$
					if (logFont != null && logFontClass != null) {
						if (strikeout) {
							logFontClass.getField("lfStrikeOut").set(logFont, new Byte((byte) 1)); //$NON-NLS-1$
						}
						if (underline) {
							logFontClass.getField("lfUnderline").set(logFont, new Byte((byte) 1)); //$NON-NLS-1$
						}
					}
				} catch (final Throwable e) {
					System.err
							.println("Unable to set underline or strikeout" + " (probably on a non-Windows platform). " + e); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			font = new Font(Display.getCurrent(), fontData);
			m_fontMap.put(fontName, font);
		}
		return font;
	}

	/**
	 * Returns a bold version of the given {@link Font}.
	 * 
	 * @param baseFont
	 *            the {@link Font} for which a bold version is desired
	 * @return the bold version of the given {@link Font}
	 */
	public static Font getBoldFont(final Font baseFont) {
		Font font = (Font) m_fontToBoldFontMap.get(baseFont);
		if (font == null) {
			final FontData fontDatas[] = baseFont.getFontData();
			final FontData data = fontDatas[0];
			font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.BOLD);
			m_fontToBoldFontMap.put(baseFont, font);
		}
		return font;
	}

	/**
	 * Dispose all of the cached {@link Font}'s.
	 */
	public static void disposeFonts() {
		// clear fonts
		for (final Iterator iter = m_fontMap.values().iterator(); iter.hasNext();) {
			((Font) iter.next()).dispose();
		}
		m_fontMap.clear();
		// clear bold fonts
		for (final Iterator iter = m_fontToBoldFontMap.values().iterator(); iter.hasNext();) {
			((Font) iter.next()).dispose();
		}
		m_fontToBoldFontMap.clear();
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// General
	//
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Dispose of cached objects and their underlying OS resources. This should
	 * only be called when the cached objects are no longer needed (e.g. on
	 * application shutdown).
	 */
	public static void dispose() {
		disposeColors();
		disposeImages();
		disposeFonts();
	}
}