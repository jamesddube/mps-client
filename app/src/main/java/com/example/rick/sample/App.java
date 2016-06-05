package com.example.rick.sample;

import android.app.Application;
import android.content.Context;

import com.orm.SugarContext;

/**
 * Created by james on 5/3/16.
 */
public class App extends Application{
	private static App Instance;

	@Override
	public void onCreate()
	{
		super.onCreate();
		SugarContext.init(this);// in your Application.onCreate()

		Instance = this;
	}

	public static App getInstance()
	{
		return Instance;
	}

	@Override
	public Context getApplicationContext()
	{
		return Instance.getApplicationContext();
	}
}
