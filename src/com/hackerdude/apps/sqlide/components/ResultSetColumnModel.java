package com.hackerdude.apps.sqlide.components;

import javax.swing.table.*;
import java.awt.font.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import com.hackerdude.apps.sqlide.dataaccess.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class ResultSetColumnModel extends DefaultTableColumnModel {

	QueryResults queryResults;
	Font intendedFont;
	JComponent renderComponent;
	int avgCharWidth;
	int aggregatedWidths;

	public ResultSetColumnModel(QueryResults results, Font fontToUse, JComponent renderComponent) {
		super();
		intendedFont = fontToUse;
		queryResults = results;
		this.renderComponent = renderComponent;
		addResultSetColumns();
		recalculateWidths();

	}


	private void setIntendedFont(Font fontToUse) {
		intendedFont = fontToUse;
		avgCharWidth = getAverageCharacterWidth();
	}

	private void addResultSetColumns() {
		for ( int i=0; i<queryResults.getColumnCount(); i++) {
			TableColumn newColumn = new TableColumn(i);
			newColumn.setHeaderValue(queryResults.getColumnName(i));
			boolean shouldShowClobEditor = queryResults.getColumnSQLTypes()[i] == java.sql.Types.CLOB;
			if  ( shouldShowClobEditor ) {
				/** @todo add clob editor!! */
				System.out.println("ResultSetColumnModel:50: Add clob  editor!");

			}
			addColumn(newColumn);
	   }
	}


	private void recalculateWidths() {
		aggregatedWidths = 0;
		for ( int i=0; i<queryResults.getColumnCount(); i++) {
			TableColumn column  = getColumn(i);
			int columnSize = queryResults.getColumnSize(i);
			int dataWidth = getAverageCharacterWidth() *columnSize;
			int titleWidth = getColumnTitlePixelWidth(i);

			int actualWidth = dataWidth;
			if ( dataWidth < titleWidth ) actualWidth = titleWidth;

			column.setMaxWidth(actualWidth*2);
			column.setPreferredWidth(actualWidth);
			column.setMinWidth(actualWidth / 2);

			aggregatedWidths = aggregatedWidths + actualWidth;
		}

	}

	/**
	 * This function returns the pixel width of the average character as
	 * would be rendered using the intended font into the render component.
	 */
	private int getAverageCharacterWidth() {
		Graphics2D g2 = (Graphics2D)renderComponent.getGraphics();
		int result = 8; // Default
		FontUIResource fui = new FontUIResource(intendedFont);
		FontRenderContext frc = g2.getFontRenderContext();
		int widestLetterWidth    = (int)fui.getStringBounds("W", frc).getWidth();
		int narrowestLetterWidth = (int)fui.getStringBounds("I", frc).getWidth();
		result = ( widestLetterWidth + narrowestLetterWidth ) / 2;
		return result;
	}


	/**
	 * This function returns the pixel width of the average character as
	 * would be rendered using the intended font into the render component.
	 */
	private int getColumnTitlePixelWidth(int columnIndex) {
		Graphics2D g2 = (Graphics2D)renderComponent.getGraphics();
		int result = 8; // Default
		FontUIResource fui = new FontUIResource(intendedFont);
		FontRenderContext frc = g2.getFontRenderContext();
		result   = (int)fui.getStringBounds(getColumn(columnIndex).getHeaderValue().toString(), frc).getWidth();
		return result;
	}

	/**
	 * This function returns the aggregated widths for all the columns.
	 */
	public int getAggregatedWidths() { return aggregatedWidths;	}

}
