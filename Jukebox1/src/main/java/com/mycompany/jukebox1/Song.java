package com.mycompany.jukebox1;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
public class Song {
	Long currentFrame;
	Clip clip;
	
	String status;
	
	AudioInputStream inputStream;
	
	
	String songID;
	String songName;
	String songArtist;
	String songGenre;
	String songAlbum;
	String songDuration;
	
	public Song(String songID, String songName, String songArtist, String songGenre, String songAlbum,
			String songDuration) {
		super();
		this.songID = songID;
		this.songName = songName;
		this.songArtist = songArtist;
		this.songGenre = songGenre;
		this.songAlbum = songAlbum;
		this.songDuration = songDuration;
	}
	
	public Song() {
		
	}
	
	public String getSongID() {
		return songID;
	}

	public void setSongID(String songID) {
		this.songID = songID;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSongArtist() {
		return songArtist;
	}

	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}

	public String getSongGenre() {
		return songGenre;
	}

	public void setSongGenre(String songGenre) {
		this.songGenre = songGenre;
	}

	public String getSongAlbum() {
		return songAlbum;
	}

	public void setSongAlbum(String songAlbum) {
		this.songAlbum = songAlbum;
	}

	public String getSongDuration() {
		return songDuration;
	}

	public void setSongDuration(String songDuration) {
		this.songDuration = songDuration;
	}
	
	
	public List<Song> storeSongsInArrayList(Connection conn) throws SQLException {
		Statement statementObj=conn.createStatement();
		ResultSet rs=statementObj.executeQuery("select * from song");
		
		ArrayList<Song> songList=new ArrayList<Song>();
		while(rs.next()) {
			songList.add(new Song(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
		}
		
		return songList;
	}
	
	
	public void displaySongs(Connection conn) throws SQLException {
		Statement statement=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=statement.executeQuery("select * from song");
		System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
		System.out.println("\n************************************************************************************************************");
		while(rs.next()) {
			System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
		}
		playSongs();
	
	}
	
	public void playSongs() {
		try {
			Scanner scanObj=new Scanner(System.in);
			System.out.println("\n\t\tEnter SongID:  ");
			String songID=scanObj.next();
			
			System.out.println("\t\tPlaying");
			String url="F:\\songs\\"+songID+".wav";
			clip=AudioSystem.getClip();
			File file=new File("C:\\Users\\KJS\\IdeaProjects\\src\\main\\java\\com\\mycompany\\jukebox1");
			
			inputStream=AudioSystem.getAudioInputStream(file.getAbsoluteFile());
			
			clip.open(inputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
			status = "play";
			
			while(true) {
                               
				System.out.println("\n\t\t1. Pause");
				System.out.println("\t\t2. Resume");
				System.out.println("\t\t3. Restart");
				System.out.println("\t\t4. Stop");
			
				
				int choice=scanObj.nextInt();
				operations(choice, url);
				if(choice==4) {
					System.out.println("\n\t\tDo You Wish Continue?(Y/N) ");
					String plays=scanObj.next();
					if(plays.equalsIgnoreCase("Y")) {
						Main main=new Main();
						main.songMain();
					}
					else if(plays.equalsIgnoreCase("N")) {
						System.out.println("Thank You");
						System.exit(0);
					}
				}
			}
		}
		catch(Exception e) {
			System.out.print("\n\t\tTHANK YOU");
		}
	}

	public void operations(int choice, String url) {
		try {
			switch(choice) {
			case 1:
				this.currentFrame=this.clip.getMicrosecondPosition();
				clip.stop();
				status="paused";
				break;
			case 2:
			
				clip.setMicrosecondPosition(currentFrame);
				clip.start();
				status="play";
				break;
				
			case 3:
				clip.stop();
				clip.close();
				
				clip=AudioSystem.getClip();
				File file=new File(url);
				
				inputStream=AudioSystem.getAudioInputStream(file.getAbsoluteFile());
				
				clip.open(inputStream);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.start();
				status = "play";
				break;
			case 4:
				currentFrame=0L;
				clip.stop();
				clip.close();
				break;
			
			}
		}
		catch(Exception e) {
			e.getMessage();
		}
	}

	public void searchSongs(List<Song> songList) {
		try {
			Scanner scanObj=new Scanner(System.in);
			System.out.println("\n\t\tSearch:\n\t\t1. By Artist\n\t\t2. By Genre\n\t\t3. By Album");
			int choice=scanObj.nextInt();
			List<Song> list=new ArrayList<Song>();
			switch(choice) {
				case 1:
					System.out.print("\n\t\tEnter Artist to Search: ");
					String artistname=scanObj.next();
					System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
					System.out.println("\n************************************************************************************************************");
					list=searchByArtist(songList,artistname);
					list.forEach(s->{
						System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());
					});
					playSongs();
					break;
				case 2:
					System.out.print("\n\t\tEnter Genre to Search: ");
					String genreName=scanObj.next();
					System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
					System.out.println("\n************************************************************************************************************");
					list=searchByGenre(songList,genreName);
					list.forEach(s->{
						System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());
					});
					playSongs();
					break;
				case 3:
					System.out.print("\n\t\tEnter Album to Search: ");
					String albumName=scanObj.next();
					System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
					System.out.println("\n************************************************************************************************************");

					list=searchByAlbum(songList,albumName);
					list.forEach(s->{
						System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());
					});
					playSongs();
					break;
				default:
					System.out.print("\n\t\tInvalid choice");
			}
		}
		catch(Exception e) {
			System.out.print(e);
		}
	}
	public List<Song> searchByArtist(List<Song> songList, String artistName) {
		List<Song> list=new ArrayList<Song>();
		list=songList.stream().filter(s->s.getSongArtist().contains(artistName)).toList();
		return list;
			
	}
	public List<Song> searchByGenre(List<Song> songList, String genreName) {
		List<Song> list=new ArrayList<Song>();
		list=songList.stream().filter(s->s.getSongGenre().contains(genreName)).toList();
		return list;
			
	}
	public List<Song> searchByAlbum(List<Song> songList, String albumName) {
		List<Song> list=new ArrayList<Song>();
		list=songList.stream().filter(s->s.getSongAlbum().contains(albumName)).toList();
		return list;	
		
	}

    void playSongs(Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
	
	}