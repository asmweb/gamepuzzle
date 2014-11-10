package com.myftiu.king.service.impl;

import com.myftiu.king.service.TimeDefinition;

import java.util.Calendar;

/**
 * @author by ali myftiu.
 */
public class TimeDefinitionImpl implements TimeDefinition
{
	@Override
	public long getCurrentTime()
	{
		return Calendar.getInstance().getTimeInMillis();
	}
}
