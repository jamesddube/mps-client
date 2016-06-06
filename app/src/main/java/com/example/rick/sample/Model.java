package com.example.rick.sample;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by james on 5/3/16.
 */
public class Model extends SugarRecord {


	@Ignore
	protected static String Endpoint = "model";
	@Ignore
	protected static String Key;


	public Model()
	{

	}

	/**
	 * @param object JSONobject
	 */
	public void make(JSONObject object)
	{
		try {

			Field[] fields = this.getClass().getFields();

			for (Field field : fields)
			{
				String fieldName = field.getName();
				if(object.has(fieldName))
				field.set(this,object.getString(fieldName));
			}

		}  catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Model(JSONObject object)
	{
		this.make(object);
	}

	/**
	 * Returns the Json object from a model class
	 *
	 * @return JSONObject
	 */
	public JSONObject getJson()
	{
		JSONObject object = new JSONObject();

		Field[] fields = this.getClass().getFields();

		for (Field field : fields)
		{
			try
			{
				object.put(field.getName(), field.get(this));
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return object;
	}

	/**
	 * @param list The list of Models to be turned into a JSONArray
	 * @return JSONArray
	 */
	public static JSONArray getJson(List<Model> list)
	{
		JSONArray jsonArray = new JSONArray();

		for (Model model : list) {
			jsonArray.put(model.getJson());
		}

		return jsonArray;
	}



	public static String getKey()
	{
		return Key;
	}
}
