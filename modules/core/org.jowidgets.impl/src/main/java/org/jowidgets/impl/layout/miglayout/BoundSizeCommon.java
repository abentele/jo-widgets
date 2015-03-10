/*
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com), 
 * modifications by Nikolaus Moll
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * Neither the name of the MiG InfoCom AB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * @version 1.0
 * @author Mikael Grev, MiG InfoCom AB
 *         Date: 2006-sep-08
 */
package org.jowidgets.impl.layout.miglayout;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * A size that contains minimum, preferred and maximum size of type {@link UnitValueCommon}.
 * <p>
 * This class is a simple value container and it is immutable.
 * <p>
 * If a size is missing (i.e., <code>null</code>) that boundary should be considered "not in use".
 * <p>
 * You can create a BoundSize from a String with the use of
 * {@link ConstraintParserCommon#parseBoundSize(String, boolean, boolean)}
 */
class BoundSizeCommon implements Serializable {
	public static final BoundSizeCommon NULL_SIZE = new BoundSizeCommon(null, null);
	public static final BoundSizeCommon ZERO_PIXEL = new BoundSizeCommon(
		MigLayoutToolkitImpl.getMigUnitValueToolkit().ZERO,
		"0px");

	private static final long serialVersionUID = 1L;

	private final transient UnitValueCommon min;
	private final transient UnitValueCommon pref;
	private final transient UnitValueCommon max;
	private final transient boolean gapPush;

	private final LayoutUtilCommon layoutUtil;

	/**
	 * Constructor that use the same value for min/preferred/max size.
	 * 
	 * @param minMaxPref The value to use for min/preferred/max size.
	 * @param createString The string used to create the BoundsSize.
	 */
	BoundSizeCommon(final UnitValueCommon minMaxPref, final String createString) {
		this(minMaxPref, minMaxPref, minMaxPref, createString);
	}

	/**
	 * Constructor. <b>This method is here for serilization only and should normally not be used. Use
	 * {@link ConstraintParserCommon#parseBoundSize(String, boolean, boolean)} instead.
	 * 
	 * @param min The minimum size. May be <code>null</code>.
	 * @param preferred The preferred size. May be <code>null</code>.
	 * @param max The maximum size. May be <code>null</code>.
	 * @param createString The string used to create the BoundsSize.
	 */
	BoundSizeCommon(
		final UnitValueCommon min,
		final UnitValueCommon preferred,
		final UnitValueCommon max,
		final String createString) // Bound to old delegate!!!!!
	{
		this(min, preferred, max, false, createString);
	}

	/**
	 * Constructor. <b>This method is here for serilization only and should normally not be used. Use
	 * {@link ConstraintParserCommon#parseBoundSize(String, boolean, boolean)} instead.
	 * 
	 * @param min The minimum size. May be <code>null</code>.
	 * @param preferred The preferred size. May be <code>null</code>.
	 * @param max The maximum size. May be <code>null</code>.
	 * @param gapPush If the size should be hinted as "pushing" and thus want to occupy free space if no one else is claiming it.
	 * @param createString The string used to create the BoundsSize.
	 */
	BoundSizeCommon(
		final UnitValueCommon min,
		final UnitValueCommon preferred,
		final UnitValueCommon max,
		final boolean gapPush,
		final String createString) {
		this.layoutUtil = MigLayoutToolkitImpl.getMigLayoutUtil();

		this.min = min;
		this.pref = preferred;
		this.max = max;
		this.gapPush = gapPush;

		layoutUtil.putCCString(this, createString); // this escapes!!
	}

	/**
	 * Returns the minimum size as sent into the constructor.
	 * 
	 * @return The minimum size as sent into the constructor. May be <code>null</code>.
	 */
	public final UnitValueCommon getMin() {
		return min;
	}

	/**
	 * Returns the preferred size as sent into the constructor.
	 * 
	 * @return The preferred size as sent into the constructor. May be <code>null</code>.
	 */
	public final UnitValueCommon getPreferred() {
		return pref;
	}

	/**
	 * Returns the maximum size as sent into the constructor.
	 * 
	 * @return The maximum size as sent into the constructor. May be <code>null</code>.
	 */
	public final UnitValueCommon getMax() {
		return max;
	}

	/**
	 * If the size should be hinted as "pushing" and thus want to occupy free space if noone else is claiming it.
	 * 
	 * @return The value.
	 */
	public boolean getGapPush() {
		return gapPush;
	}

	/**
	 * Returns if this bound size has no min, preferred and maximum size set (they are all <code>null</code>)
	 * 
	 * @return If unset.
	 */
	public boolean isUnset() {
		// Most common case by far is this == ZERO_PIXEL...
		return this == ZERO_PIXEL || (pref == null && min == null && max == null && gapPush == false);
	}

	/**
	 * Makes sure that <code>size</code> is within min and max of this size.
	 * 
	 * @param size The size to constrain.
	 * @param refValue The reference to use for relative sizes.
	 * @param parent The parent container.
	 * @return The size, constrained within min and max.
	 */
	public int constrain(int size, final float refValue, final IContainerWrapperCommon parent) {
		if (max != null) {
			size = Math.min(size, max.getPixels(refValue, parent, parent));
		}
		if (min != null) {
			size = Math.max(size, min.getPixels(refValue, parent, parent));
		}
		return size;
	}

	/**
	 * Returns the minimum, preferred or maximum size for this bounded size.
	 * 
	 * @param sizeType The type. <code>LayoutUtil.MIN</code>, <code>LayoutUtil.PREF</code> or <code>LayoutUtil.MAX</code>.
	 * @return
	 */
	final UnitValueCommon getSize(final int sizeType) {
		if (sizeType == LayoutUtilCommon.MIN) {
			return min;
		}
		else if (sizeType == LayoutUtilCommon.PREF) {
			return pref;
		}
		else if (sizeType == LayoutUtilCommon.MAX) {
			return max;
		}
		else {
			throw new IllegalArgumentException("Unknown size: " + sizeType);
		}
	}

	/**
	 * Convert the bound sizes to pixels.
	 * <p>
	 * <code>null</code> bound sizes will be 0 for min and preferred and {@link LayoutUtilCommon.miginfocom.layout.LayoutUtil#INF}
	 * for max.
	 * 
	 * @param refSize The reference size.
	 * @param parent The parent. Not <code>null</code>.
	 * @param comp The component, if applicable, can be <code>null</code>.
	 * @return An array of lenth three (min,pref,max).
	 */
	final int[] getPixelSizes(final float refSize, final IContainerWrapperCommon parent, final IComponentWrapperCommon comp) {
		return new int[] {
				min != null ? min.getPixels(refSize, parent, comp) : 0, pref != null ? pref.getPixels(refSize, parent, comp) : 0,
				max != null ? max.getPixels(refSize, parent, comp) : LayoutUtilCommon.INF};
	}

	/**
	 * Returns the a constraint string that can be re-parsed to be the exact same UnitValue.
	 * 
	 * @return A String. Never <code>null</code>.
	 */
	String getConstraintString() {
		final String cs = layoutUtil.getCCString(this);
		if (cs != null) {
			return cs;
		}

		if (min == pref && pref == max) {
			return min != null ? (min.getConstraintString() + "!") : "null";
		}

		final StringBuilder sb = new StringBuilder(16);

		if (min != null) {
			sb.append(min.getConstraintString()).append(':');
		}

		if (pref != null) {
			if (min == null && max != null) {
				sb.append(":");
			}
			sb.append(pref.getConstraintString());
		}
		else if (min != null) {
			sb.append('n');
		}

		if (max != null) {
			sb.append(sb.length() == 0 ? "::" : ":").append(max.getConstraintString());
		}

		if (gapPush) {
			if (sb.length() > 0) {
				sb.append(':');
			}
			sb.append("push");
		}

		return sb.toString();
	}

	void checkNotLinked() {
		if (min != null && min.isLinkedDeep() || pref != null && pref.isLinkedDeep() || max != null && max.isLinkedDeep()) {
			throw new IllegalArgumentException("Size may not contain links");
		}
	}

	static {
		final LayoutUtilCommon lUtil = MigLayoutToolkitImpl.getMigLayoutUtil();
		if (lUtil.hasBeans()) {
			lUtil.setDelegate(BoundSizeCommon.class, new PersistenceDelegate() {
				@Override
				protected Expression instantiate(final Object oldInstance, final Encoder out) {
					final BoundSizeCommon bs = (BoundSizeCommon) oldInstance;
					if (GridCommon.TEST_GAPS) {
						return new Expression(oldInstance, BoundSizeCommon.class, "new", new Object[] {
								bs.getMin(), bs.getPreferred(), bs.getMax(), bs.getGapPush(), bs.getConstraintString()});
					}
					else {
						return new Expression(oldInstance, BoundSizeCommon.class, "new", new Object[] {
								bs.getMin(), bs.getPreferred(), bs.getMax(), bs.getConstraintString()});
					}
				}
			});
		}
	}

	// ************************************************
	// Persistence Delegate and Serializable combined.
	// ************************************************
	protected Object readResolve() throws ObjectStreamException {
		return layoutUtil.getSerializedObject(this);
	}

	private void writeObject(final ObjectOutputStream out) throws IOException {
		if (getClass() == BoundSizeCommon.class) {
			layoutUtil.writeAsXML(out, this);
		}
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		layoutUtil.setSerializedObject(this, layoutUtil.readAsXML(in));
	}
}