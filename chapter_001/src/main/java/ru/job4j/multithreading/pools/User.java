package ru.job4j.multithreading.pools;

public class User {
	public final String email;
	public final String username;
	
	public User(String name, String mail) {
		this.email = mail;
		this.username = name;
	}
}
