package com.myftiu.king.service.impl;

import com.myftiu.king.service.TimeDefinition;

import java.util.Calendar;

/**
 * @author by ali myftiu on 10/11/14.
 */
public class TimeDefinitionImpl implements TimeDefinition
{
	@Override
	public long getCurrentTime()
	{
		return Calendar.getInstance().getTimeInMillis();
	}
}
