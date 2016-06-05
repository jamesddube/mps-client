package com.example.rick.sample;

import com.orm.dsl.Ignore;

import org.json.JSONObject;

/**
 * Created by james on 5/3/16.
 */
public class User extends Model
{
	@Ignore
	public static String Key = "users";
	@Ignore
	public static String Endpoint = "users";

	protected String name;
	protected String username;
	protected String surname;
	protected String password;

	public User()
	{

	}

	public User(JSONObject object)
	{
		super(object);
	}

	public static User getCurrentUser()
	{
		User user = new User();

		user.username = "info@mps.com";
		user.password = "password";

		return user;

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
}
