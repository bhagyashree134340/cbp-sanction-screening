package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sanctionlist")
public class SanctionList {
	
	@Id
	@Column(name = "ID")
	private int ID;
	
	@Column(name = "NAME")
	private String name;

	
	
	public SanctionList()
	{
		
	}
	public SanctionList(int iD, String name) {
		super();
		ID = iD;
		this.name = name;
	}

	@Override
	public String toString() {
		return "SanctionList [ID=" + ID + ", name=" + name + "]";
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
