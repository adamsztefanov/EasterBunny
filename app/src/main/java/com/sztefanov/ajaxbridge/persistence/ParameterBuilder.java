package com.sztefanov.ajaxbridge.persistence;

import com.sztefanov.ajaxbridge.helper.DateNN;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ParameterBuilder {

	private ArrayList<String[]> params;

	public ParameterBuilder() {
		this.params = new ArrayList<>();
	}

	public ArrayList<String[]> getParams() {
		return params;
	}

	public void setParams(ArrayList<String[]> params) {
		this.params = params;
	}

	public void addParam(String type, String value) {
		String[] param = new String[2];
		param[0] = type;
		param[1] = value;
		this.params.add(param);
	}

	public PreparedStatement addParamsToStmt(PreparedStatement stmt)
		throws SQLException {
		int i = 1;
		for (String[] param : getParams()) {
			String type = param[0];
			String value = param[1];
			InputStream is;
			switch (type) {
				case "int":
					if (value == null) {
						value = "0";
					}
					stmt.setInt(i, Integer.parseInt(value));
					break;
				case "double":
					if (value == null) {
						value = "0";
					}
					stmt.setDouble(i, Double.parseDouble(value));
					break;
				case "text":
					if (value == null) {
						value = "";
					}
					is = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
					stmt.setBinaryStream(i, is);
					break;
				case "blob":
					if (value == null) {
						value = "";
					}
					is = new ByteArrayInputStream(value
						.getBytes(StandardCharsets.UTF_8));
					stmt.setBinaryStream(i, is);
					break;
				case "date":
					if (value == null) {
						value = DateNN.now();
					}
					stmt.setTimestamp(i, Timestamp.valueOf(value));
					break;
				case "boolean":
					if (value == null) {
						value = "0";
					}
					stmt.setBoolean(i, Boolean.parseBoolean(value));
					break;
				default:
					if (value == null) {
						value = "";
					}
					stmt.setString(i, value);
					break;
			}
			i++;
		}
		return stmt;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String[] param : params) {
			int i = 0;
			sb.append("[");
			for (String string : param) {
				sb.append(string);
				if (i == 0) {
					sb.append(", ");
				}
				i++;
			}
			sb.append("] ");
		}
		return "ParameterBuilder{" + "params=" + sb.toString() + '}';
	}

}
