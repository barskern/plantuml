/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.awt.geom.XPoint2D;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseSelfArrow extends AbstractComponentRoseArrow {

	private final double arrowWidth = 45;
	private final boolean niceArrow;

	public ComponentRoseSelfArrow(Style style, Display stringsToDisplay, ArrowConfiguration arrowConfiguration,
			ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize, boolean niceArrow) {
		super(style, stringsToDisplay, arrowConfiguration, spriteContainer, maxMessageSize);
		this.niceArrow = niceArrow;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		if (getArrowConfiguration().isHidden()) {
			return;
		}
		final StringBounder stringBounder = ug.getStringBounder();
		final double textHeight = getTextHeight(stringBounder);

		ug = ug.apply(getForegroundColor());
		final double xRight = arrowWidth - 3;

		final UGraphic ug2 = getArrowConfiguration().applyStroke(ug);

		double x1 = area.getDeltaX1() < 0 ? area.getDeltaX1() : 0;
		double x2 = area.getDeltaX1() > 0 ? -area.getDeltaX1() : 0 + 1;

		final double arrowHeight = getArrowOnlyHeight(stringBounder);
		final UEllipse circle = new UEllipse(ComponentRoseArrow.diamCircle, ComponentRoseArrow.diamCircle);
		if (getArrowConfiguration().getDecoration1() == ArrowDecoration.CIRCLE) {
			ug2.apply(new UStroke(ComponentRoseArrow.thinCircle)).apply(getForegroundColor())
					.apply(new UTranslate(x1 + 1 - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle,
							textHeight - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle / 2))
					.draw(circle);
			x1 += ComponentRoseArrow.diamCircle / 2;
		}
		if (getArrowConfiguration().getDecoration2() == ArrowDecoration.CIRCLE) {
			ug2.apply(new UStroke(ComponentRoseArrow.thinCircle)).apply(getForegroundColor()).apply(new UTranslate(
					x2 - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle,
					textHeight + arrowHeight - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle / 2))
					.draw(circle);
			x2 += ComponentRoseArrow.diamCircle / 2;
		}
		final boolean hasStartingCrossX = getArrowConfiguration().getDressing1().getHead() == ArrowHead.CROSSX;
		if (hasStartingCrossX)
			x1 += 2 * ComponentRoseArrow.spaceCrossX;

		final boolean hasFinalCrossX = getArrowConfiguration().getDressing2().getHead() == ArrowHead.CROSSX;
		if (hasFinalCrossX)
			x2 += 2 * ComponentRoseArrow.spaceCrossX;

		if (getArrowConfiguration().isReverseDefine()) {
			ug2.apply(new UTranslate(-xRight, textHeight)).draw(ULine.hline(xRight - x1));
			ug2.apply(new UTranslate(-xRight, textHeight)).draw(ULine.vline(arrowHeight));
			ug2.apply(new UTranslate(-xRight, textHeight + arrowHeight)).draw(ULine.hline(xRight - x2));
			final UPolygon polygon = getPolygon().translate(0, textHeight + arrowHeight);
			ug.apply(getForegroundColor().bg()).apply(UTranslate.dx(x2)).draw(polygon);
		} else {

			ug2.apply(new UTranslate(x1, textHeight)).draw(ULine.hline(xRight - x1));
			ug2.apply(new UTranslate(xRight, textHeight)).draw(ULine.vline(arrowHeight));
			ug2.apply(new UTranslate(x2, textHeight + arrowHeight)).draw(ULine.hline(xRight - x2));

			if (hasStartingCrossX) {
				ug.apply(new UStroke(2))
						.apply(new UTranslate(ComponentRoseArrow.spaceCrossX, textHeight - getArrowDeltaX() / 2))
						.draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
				ug.apply(new UStroke(2))
						.apply(new UTranslate(ComponentRoseArrow.spaceCrossX, textHeight + getArrowDeltaX() / 2))
						.draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));
			} else if (getArrowConfiguration().getDressing1().getHead() == ArrowHead.NORMAL) {
				final UPolygon polygon = getPolygon().translate(0, textHeight);
				ug.apply(getForegroundColor().bg()).apply(UTranslate.dx(x1)).draw(polygon);
			}

			if (hasFinalCrossX) {
				ug.apply(new UStroke(2))
						.apply(new UTranslate(ComponentRoseArrow.spaceCrossX,
								textHeight - getArrowDeltaX() / 2 + arrowHeight))
						.draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
				ug.apply(new UStroke(2))
						.apply(new UTranslate(ComponentRoseArrow.spaceCrossX,
								textHeight + getArrowDeltaX() / 2 + arrowHeight))
						.draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));
			} else if (getArrowConfiguration().isAsync()) {
				if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
					getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(x2, textHeight + arrowHeight))
							.draw(new ULine(getArrowDeltaX(), -getArrowDeltaY()));
				}
				if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
					getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(x2, textHeight + arrowHeight))
							.draw(new ULine(getArrowDeltaX(), getArrowDeltaY()));
				}
			} else if (getArrowConfiguration().getDressing2().getHead() == ArrowHead.NORMAL) {
				final UPolygon polygon = getPolygon().translate(0, textHeight + arrowHeight);
				ug.apply(getForegroundColor().bg()).apply(UTranslate.dx(x2)).draw(polygon);
			}
		}

		if (getArrowConfiguration().isReverseDefine())
			getTextBlock().drawU(ug.apply(UTranslate.dx(-getPureTextWidth(stringBounder))));
		else
			getTextBlock().drawU(ug.apply(UTranslate.dx(getMarginX1())));

	}

	private UPolygon getPolygon() {
		final UPolygon polygon = new UPolygon();
		final double direction = getArrowConfiguration().isReverseDefine() ? -1 : 1;
		final double x = direction * getArrowDeltaX();
		if (getArrowConfiguration().getPart() == ArrowPart.TOP_PART) {
			polygon.addPoint(x, -getArrowDeltaY());
			polygon.addPoint(0, 0);
			polygon.addPoint(x, 0);
		} else if (getArrowConfiguration().getPart() == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(x, 0);
			polygon.addPoint(0, 0);
			polygon.addPoint(x, getArrowDeltaY());
		} else {
			polygon.addPoint(x, -getArrowDeltaY());
			polygon.addPoint(0, 0);
			polygon.addPoint(x, getArrowDeltaY());
			if (niceArrow) {
				polygon.addPoint(x - direction * 4, 0);
			}
		}
		return polygon;
	}

	public XPoint2D getStartPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		final double textHeight = getTextHeight(stringBounder);
		return new XPoint2D(getPaddingX(), textHeight + getPaddingY());
	}

	public XPoint2D getEndPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		final double textHeight = getTextHeight(stringBounder);
		final double textAndArrowHeight = textHeight + getArrowOnlyHeight(stringBounder);
		return new XPoint2D(getPaddingX(), textAndArrowHeight + getPaddingY());
	}

	@Override
	public double getYPoint(StringBounder stringBounder) {
		final double textHeight = getTextHeight(stringBounder);
		final double textAndArrowHeight = textHeight + getArrowOnlyHeight(stringBounder);
		return (textHeight + textAndArrowHeight) / 2 + getPaddingX();
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + getArrowOnlyHeight(stringBounder) + 2 * getPaddingY();
	}

	private double getArrowOnlyHeight(StringBounder stringBounder) {
		return 13;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return Math.max(getTextWidth(stringBounder), arrowWidth);
	}

	public double getPosArrow(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

}
